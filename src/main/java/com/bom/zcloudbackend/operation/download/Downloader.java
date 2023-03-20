package com.bom.zcloudbackend.operation.download;

import com.bom.zcloudbackend.operation.download.domain.DownloadFile;

import javax.servlet.http.HttpServletResponse;

public abstract class Downloader {

    public abstract void download(HttpServletResponse response, DownloadFile downloadFile);

}
