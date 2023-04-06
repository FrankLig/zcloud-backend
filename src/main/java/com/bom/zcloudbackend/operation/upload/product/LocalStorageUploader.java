package com.bom.zcloudbackend.operation.upload.product;

import com.bom.zcloudbackend.common.exception.NotSameFileException;
import com.bom.zcloudbackend.common.exception.UploadException;
import com.bom.zcloudbackend.common.util.EncryptUserUtil;
import com.bom.zcloudbackend.common.util.FileUtil;
import com.bom.zcloudbackend.common.util.PathUtil;
import com.bom.zcloudbackend.dto.EncUploadFileDTO;
import com.bom.zcloudbackend.operation.upload.Uploader;
import com.bom.zcloudbackend.operation.upload.domain.UploadFile;
import com.bom.zcloudbackend.service.UserService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 本地上传实现类
 * </p>
 */
@Component
public class LocalStorageUploader extends Uploader {

    @Resource
    private UserService userService;

    public LocalStorageUploader() {

    }

    @Override
    public List<UploadFile> upload(HttpServletRequest request, UploadFile uploadFile) {
        List<UploadFile> uploadFileList = new ArrayList<>();
        StandardMultipartHttpServletRequest standardMultipartHttpServletRequest = (StandardMultipartHttpServletRequest) request;
        boolean isMultiPart = ServletFileUpload.isMultipartContent(standardMultipartHttpServletRequest);    //判断是否文件上传请求
        if (!isMultiPart) {
            throw new UploadException("未包含文件上传域");
        }

        //文件存放路径
        String savePath = getSaveFilePath();
        try {
            Iterator<String> iter = standardMultipartHttpServletRequest.getFileNames();     //获取所有表单项，并不是原文件名
            while (iter.hasNext()) {    //TODO  是否有文件
                //真正上传文件
                uploadFileList = doUpload(standardMultipartHttpServletRequest, savePath, iter, uploadFile);
            }
        } catch (IOException e) {
            throw new UploadException("未包含文件上传域");
        } catch (NotSameFileException notSameFileException) {
            notSameFileException.printStackTrace();
        }
        return uploadFileList;

    }


    /**
     * 真正上传文件接口
     *
     * @param standardMultipartHttpServletRequest
     * @param savePath
     * @param iter
     * @param uploadFile
     * @return
     * @throws IOException
     * @throws NotSameFileException
     */
    private List<UploadFile> doUpload(StandardMultipartHttpServletRequest standardMultipartHttpServletRequest,
        String savePath, Iterator<String> iter, UploadFile uploadFile) throws IOException, NotSameFileException {

        List<UploadFile> saveUploadFileList = new ArrayList<UploadFile>();
        MultipartFile multipartfile = standardMultipartHttpServletRequest.getFile(iter.next());

        String timeStampName = uploadFile.getIdentifier();      //上传文件的唯一标识

        String originalName = multipartfile.getOriginalFilename();      //获取源文件名

        String fileName = getFileName(originalName);
        String fileType = FileUtil.getFileExtendName(originalName);     //获取文件类型
        uploadFile.setFileName(fileName);
        uploadFile.setFileType(fileType);
        uploadFile.setTimeStampName(timeStampName);             //设置文件唯一标识

        String saveFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + fileType;
        String tempFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + fileType + "_tmp";
        String minFilePath = savePath + FILE_SEPARATOR + timeStampName + "_min" + "." + fileType;
        String confFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + "conf";
        File file = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + saveFilePath);         //最终生成文件
        File tempFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + tempFilePath);     //临时写入文件
        File minFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + minFilePath);       //图片压缩文件
        File confFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + confFilePath);     //记录文件分片上传记录文件
        // uploadFile.setIsOSS(0);
        // uploadFile.setStorageType(0);
        uploadFile.setUrl(saveFilePath);        //设置文件保存路径

        if (StringUtils.isEmpty(uploadFile.getTaskId())) {
            uploadFile.setTaskId(UUID.randomUUID().toString());     //设置文件上传任务ID
        }

        //第一步 打开将要写入的文件
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        //第二步 获取可读写文件通道
        FileChannel fileChannel = raf.getChannel();
        //第三步 计算偏移量
        long position = (uploadFile.getChunkNumber() - 1) * uploadFile.getChunkSize();
        //第四步 获取分片数据
        byte[] fileData = multipartfile.getBytes();
        //第五步 写入数据
        fileChannel.position(position);                     //channel位移
        fileChannel.write(ByteBuffer.wrap(fileData));       //写入数据
        fileChannel.force(true);                   //强制将数据刷出磁盘，避免系统崩溃导致数据丢失
        fileChannel.close();                                //关闭通道
        raf.close();                                        //关闭文件

        //判断是否完成文件的传输并进行校验与重命名
        boolean isComplete = checkUploadStatus(uploadFile, confFile);

        //文件上传完成
        if (isComplete) {
            //计算文件md5
            FileInputStream fileInputStream = new FileInputStream(tempFile.getPath());
            String md5 = DigestUtils.md5DigestAsHex(fileInputStream);
            fileInputStream.close();

            //校验文件完整性
            if (StringUtils.isNotBlank(md5) && !md5.equals(uploadFile.getIdentifier())) {
                throw new NotSameFileException();
            }

            tempFile.renameTo(file);    //重命名文件

            //判断是否图片文件
            if (FileUtil.isImageFile(uploadFile.getFileType())) {
                Thumbnails.of(file).size(300, 300).toFile(minFile);     //创建压缩后的图片文件
            }

            uploadFile.setSuccess(1);
            uploadFile.setMessage("上传成功");
        } else {
            uploadFile.setSuccess(0);
            uploadFile.setMessage("未完成");
        }
        uploadFile.setFileSize(uploadFile.getTotalSize());
        saveUploadFileList.add(uploadFile);

        return saveUploadFileList;
    }

    @Override
    public List<UploadFile> encUpload(HttpServletRequest request, UploadFile uploadFile, Long userId) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

        List<UploadFile> uploadFileList = new ArrayList<>();

        boolean isMultiPart = ServletFileUpload.isMultipartContent(multipartHttpServletRequest);    //是否是文件上传请求
        if (!isMultiPart) {
            throw new UploadException("未包含文件上传作用域");
        }

        String savePath = getSaveFilePath();         //获取文件存放路径
        Iterator<String> iter = multipartHttpServletRequest.getFileNames();
        if (iter.hasNext()) {
            MultipartFile multipartFile = multipartHttpServletRequest.getFile(iter.next());

            String timeStampName = uploadFile.getIdentifier();
            String originalFilename = multipartFile.getOriginalFilename();
            String fileName = getFileName(originalFilename);
            String fileType = FileUtil.getFileExtendName(originalFilename);

            uploadFile.setFileName(fileName);
            uploadFile.setFileType(fileType);
            uploadFile.setTimeStampName(timeStampName);

            String saveFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + fileType;
            File encFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + saveFilePath);      //最终生成文件

            uploadFile.setUrl(saveFilePath);    //设置文件保存路径


            try {
                String secretKey=EncryptUserUtil.aesEncrypt(userService.getById(userId).getEncryptKey()).substring(0,32);   //获取用户加密密钥
                byte[] fileContent = multipartFile.getBytes();
                SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
                byte[] encryptedContent = cipher.doFinal(fileContent);
                Files.write(Paths.get(PathUtil.getStaticPath()+FILE_SEPARATOR+saveFilePath),encryptedContent, StandardOpenOption.CREATE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            uploadFile.setSuccess(1);
            uploadFile.setMessage("加密上传成功");
            uploadFile.setFileSize(uploadFile.getFileSize());
            uploadFileList.add(uploadFile);
        }
        return uploadFileList;
    }
}
