package com.bom.zcloudbackend.service.impl;

import com.bom.zcloudbackend.common.util.DateUtil;
import com.bom.zcloudbackend.common.util.PropertiesUtil;
import com.bom.zcloudbackend.dto.DownloadFileDTO;
import com.bom.zcloudbackend.dto.UploadFileDTO;
import com.bom.zcloudbackend.entity.File;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.mapper.FileMapper;
import com.bom.zcloudbackend.mapper.UserFileMapper;
import com.bom.zcloudbackend.operation.FileOperationFactory;
import com.bom.zcloudbackend.operation.download.Downloader;
import com.bom.zcloudbackend.operation.download.domain.DownloadFile;
import com.bom.zcloudbackend.operation.upload.Uploader;
import com.bom.zcloudbackend.dto.EncUploadFileDTO;
import com.bom.zcloudbackend.operation.upload.domain.UploadFile;
import com.bom.zcloudbackend.service.FileTransferService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class FileTransferServiceImpl implements FileTransferService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private FileOperationFactory localStorageOperationFactory;

    /**
     * 分片上传文件
     *
     * @param request       请求
     * @param uploadFileDTO dto
     * @param userId        userId
     */
    @Override
    public void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDTO, Long userId) {
        Uploader uploader = null;
        UploadFile uploadFile = new UploadFile();
        uploadFile.setChunkNumber(uploadFileDTO.getChunkNumber());
        uploadFile.setChunkSize(uploadFileDTO.getChunkSize());
        uploadFile.setTotalChunks(uploadFileDTO.getTotalChunks());
        uploadFile.setIdentifier(uploadFileDTO.getIdentifier());
        uploadFile.setTotalSize(uploadFileDTO.getTotalSize());
        uploadFile.setCurrentChunkSize(uploadFileDTO.getCurrentChunkSize());

        //获取配置设置的存储类型
        String storageType = PropertiesUtil.getProperty("file.storage-type");
        //获取全局唯一uploader
        synchronized (FileTransferService.class) {
            if ("0".equals(storageType)) {
                uploader = localStorageOperationFactory.getUploader();
            }
        }
        List<UploadFile> uploadFileList = uploader.upload(request, uploadFile);
        for (int i = 0; i < uploadFileList.size(); i++) {
            uploadFile = uploadFileList.get(i);
            File file = new File();

            file.setIdentifier(uploadFileDTO.getIdentifier());
            file.setStorageType(Integer.parseInt(storageType));
            file.setTimeStampName(uploadFile.getTimeStampName());
            //上传成功
            if (uploadFile.getSuccess() == 1) {
                file.setFileUrl(uploadFile.getUrl());
                file.setFileSize(uploadFile.getFileSize());
                file.setPointCount(1);
                fileMapper.insert(file);

                UserFile userFile = new UserFile();
                userFile.setFileId(file.getFileId());
                userFile.setExtendName(uploadFile.getFileType());
                userFile.setFileName(uploadFile.getFileName());
                userFile.setFilePath(uploadFileDTO.getFilePath());
                userFile.setDeleteTag(0);
                userFile.setUserId(userId);
                userFile.setIsDir(0);
                userFile.setUploadTime(DateUtil.getCurrentTime());
                userFileMapper.insert(userFile);
            }
        }
    }


    @Override
    public void downloadFile(HttpServletResponse response, DownloadFileDTO downloadFileDTO) {
        UserFile userFile = userFileMapper.selectById(downloadFileDTO.getUserFileId());

        String fileName = userFile.getFileName() + "." + userFile.getExtendName();
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        // 设置强制下载不打开
        response.setContentType("application/force-download");
        // 设置文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

        File file = fileMapper.selectById(userFile.getFileId());
        Downloader downloader = null;
        if (file.getStorageType() == 0) {
            downloader = localStorageOperationFactory.getDownloader();
        }
        DownloadFile uploadFile = new DownloadFile();
        uploadFile.setFileUrl(file.getFileUrl());
        uploadFile.setTimeStampName(file.getTimeStampName());
        downloader.download(response, uploadFile);
    }


    @Override
    public Long selectStorageSizeByUserId(Long userId) {
        return userFileMapper.selectStorageSizeByUserId(userId);
    }

    @Override
    public void uploadEncFile(HttpServletRequest request, EncUploadFileDTO encUploadFileDTO, Long userId) {
        Uploader uploader = null;
        UploadFile uploadFile = new UploadFile();
        uploadFile.setIdentifier(UUID.randomUUID().toString());     //设置唯一标识
        uploadFile.setFileSize(encUploadFileDTO.getFileSize());     //设置文件大小

        String storageType = PropertiesUtil.getProperty("file.storage-type");
        synchronized (FileTransferService.class) {
            if ("0".equals(storageType)) {
                uploader = localStorageOperationFactory.getUploader();
            }
        }
        List<UploadFile> uploadFileList = uploader.encUpload(request, uploadFile, userId);//真正上传文件
        for (int i = 0; i < uploadFileList.size(); i++) {
            uploadFile = uploadFileList.get(i);

            File file = new File();
            file.setIdentifier(uploadFile.getIdentifier().substring(0,32));
            file.setStorageType(Integer.parseInt(storageType));
            file.setTimeStampName(uploadFile.getTimeStampName());

            if (uploadFile.getSuccess() == 1) {
                file.setFileUrl(uploadFile.getUrl());
                file.setFileSize(uploadFile.getFileSize());
                file.setPointCount(1);
                fileMapper.insert(file);

                UserFile userFile = new UserFile();
                userFile.setFileId(file.getFileId());
                userFile.setFileName(uploadFile.getFileName());
                userFile.setExtendName(uploadFile.getFileType());
                userFile.setFilePath(encUploadFileDTO.getFilePath());
                userFile.setIsDir(0);
                userFile.setDeleteTag(0);
                userFile.setUserId(userId);
                userFile.setUploadTime(DateUtil.getCurrentTime());
                userFileMapper.insert(userFile);
            }
        }
    }
}
