package com.bom.zcloudbackend.service;

import com.bom.zcloudbackend.dto.DownloadFileDTO;
import com.bom.zcloudbackend.dto.UploadFileDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileTransferService {

    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDTO, Long userId);

    void downloadFile(HttpServletResponse response, DownloadFileDTO downloadFileDTO);

    Long selectStorageSizeByUserId(Long userId);

}
