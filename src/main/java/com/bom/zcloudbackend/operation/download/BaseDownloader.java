package com.bom.zcloudbackend.operation.download;

import com.bom.zcloudbackend.operation.download.domain.DownloadFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Frank Liang
 */
public abstract class BaseDownloader {

    public static final String ALGORITHM="AES/ECB/PKCS5Padding";

    /**
     * 下载文件
     * @param response
     * @param downloadFile
     */
    public abstract void download(HttpServletResponse response, DownloadFile downloadFile);

    /**
     * 下载加密文件
     * @param response
     * @param downloadFile
     * @param userId
     */
    public abstract void downloadEncFile(HttpServletResponse response,DownloadFile downloadFile,Long userId);


}
