package com.bom.zcloudbackend.service;

import com.bom.zcloudbackend.dto.DownloadFileDTO;
import com.bom.zcloudbackend.dto.UploadFileDTO;
import com.bom.zcloudbackend.dto.EncUploadFileDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Frank Liang
 */
public interface FileTransferService {

    /**
     * 上传文件（分片普通上传)
     * @param request
     * @param uploadFileDTO
     * @param userId
     */
    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDTO, Long userId);

    /**
     * //下载文件(普通文件)
     * @param response
     * @param downloadFileDTO
     */
    void downloadFile(HttpServletResponse response, DownloadFileDTO downloadFileDTO);

    /**
     * 上传加密文件
     * @param request
     * @param encUploadFileDTO
     * @param userId
     */
    void uploadEncFile(HttpServletRequest request, EncUploadFileDTO encUploadFileDTO,Long userId);

    /**
     * 获取用户存储空间
     * @param userId
     * @return
     */
    Long selectStorageSizeByUserId(Long userId);

}
