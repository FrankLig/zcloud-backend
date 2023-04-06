package com.bom.zcloudbackend.service;

import com.bom.zcloudbackend.dto.DownloadFileDTO;
import com.bom.zcloudbackend.dto.UploadFileDTO;
import com.bom.zcloudbackend.dto.EncUploadFileDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileTransferService {

    //上传文件（分片普通上传)
    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDTO, Long userId);

    //下载文件(普通文件)
    void downloadFile(HttpServletResponse response, DownloadFileDTO downloadFileDTO);

    void uploadEncFile(HttpServletRequest request, EncUploadFileDTO encUploadFileDTO,Long userId);

    Long selectStorageSizeByUserId(Long userId);

}
