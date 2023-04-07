package com.bom.zcloudbackend.operation.upload;

import com.bom.zcloudbackend.common.util.PathUtil;
import com.bom.zcloudbackend.operation.upload.domain.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Frank Liang
 */
@Slf4j
public abstract class BaseUploader {

    public static final String ROOT_PATH = "upload";
    public static final String FILE_SEPARATOR = "/";
    public static final int maxSize = 10000000;
    public static final String ALGORITHM="AES/ECB/PKCS5Padding";

    /**
     * 上传文件（分片上传)
     * @param request
     * @param uploadFile
     * @return
     */
    public abstract List<UploadFile> upload(HttpServletRequest request, UploadFile uploadFile);

    /**
     * 上传加密文件
     * @param request
     * @param uploadFile
     * @param userId
     * @return
     */
    public abstract List<UploadFile> encUpload(HttpServletRequest request, UploadFile uploadFile,Long userId);

    /**
     * 根据字符串创建本地目录，并根据日期生成子目录
     *
     * @return 文件存放目录
     */
    protected String getSaveFilePath() {
        String path = ROOT_PATH;
        System.out.println(path);
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        path = FILE_SEPARATOR + path + FILE_SEPARATOR + formater.format(new Date());

        String staticPath = PathUtil.getStaticPath();

        File dir = new File(staticPath + path);
        if (!dir.exists()) {
            try {
                boolean isSuccessMakeDir = dir.mkdirs();
                if (!isSuccessMakeDir) {
                    log.error("目录创建失败:" + PathUtil.getStaticPath() + path);
                }
            } catch (Exception e) {
                log.error("目录创建失败" + PathUtil.getStaticPath() + path);
                return "";
            }
        }
        return path;
    }

    protected String getTimeStampName() {
        try {
            //因为Math.random生成的是伪随机数（可以计算）,nextInt表示生的int类型数字的上限为100000
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            return "" + number.nextInt(10000) + System.currentTimeMillis();
        } catch (NoSuchAlgorithmException e) {
            log.error("生成安全随机数失败");
        }
        return "" + System.currentTimeMillis();
    }

    /**
     * 检查文件上传进度
     * @param param
     * @param confFile
     * @return
     * @throws IOException
     */
    public synchronized boolean checkUploadStatus(UploadFile param, File confFile) throws IOException {
        RandomAccessFile confAccessFile = new RandomAccessFile(confFile, "rw");

        //设置文件长度
        confAccessFile.setLength(param.getTotalChunks());

        //设置起始偏移量
        confAccessFile.seek(param.getChunkNumber() - 1);

        //将特定的一个字节(127)写入文件中 ，
        confAccessFile.write(Byte.MAX_VALUE);
        byte[] completeStatusList = FileUtils.readFileToByteArray(confFile);
        //不关闭会造成无法占用
        confAccessFile.close();

        //创建conf文件文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认的0,已上传的就是127
        for (int i = 0; i < completeStatusList.length; i++) {
            if (completeStatusList[i] != Byte.MAX_VALUE) {
                //如果byte数组并未全部写入，说明同一文件的所有分片还没有全部上传
                return false;
            }
        }

        //全部文件上传完成，删除conf文件
        confFile.delete();
        return true;
    }

    /**
     * 获取不带拓展名的文件名
     * @param fileName
     * @return
     */
    protected String getFileName(String fileName) {
        if (!fileName.contains(".")) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
