package com.bom.zcloudbackend.operation.download;

import com.bom.zcloudbackend.operation.download.domain.DownloadFile;

import javax.servlet.http.HttpServletResponse;

public abstract class Downloader {

    public static final String ALGORITHM="AES/ECB/PKCS5Padding";

    public abstract void download(HttpServletResponse response, DownloadFile downloadFile);

    public abstract void downloadEncFile(HttpServletResponse response,DownloadFile downloadFile,Long userId);

}
