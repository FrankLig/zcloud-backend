package com.bom.zcloudbackend.operation;

import com.bom.zcloudbackend.operation.delete.Deleter;
import com.bom.zcloudbackend.operation.download.Downloader;
import com.bom.zcloudbackend.operation.upload.Uploader;

public interface FileOperationFactory {

    Uploader getUploader();

    Downloader getDownloader();

    Deleter getDeleter();

}
