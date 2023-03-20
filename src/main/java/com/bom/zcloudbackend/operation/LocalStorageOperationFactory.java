package com.bom.zcloudbackend.operation;

import com.bom.zcloudbackend.operation.delete.Deleter;
import com.bom.zcloudbackend.operation.delete.product.LocalStorageDeleter;
import com.bom.zcloudbackend.operation.download.Downloader;
import com.bom.zcloudbackend.operation.download.product.LocalStorageDownloader;
import com.bom.zcloudbackend.operation.upload.Uploader;
import com.bom.zcloudbackend.operation.upload.product.LocalStorageUploader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LocalStorageOperationFactory implements FileOperationFactory {

    @Resource
    LocalStorageUploader localStorageUploader;

    @Resource
    LocalStorageDeleter localStorageDeleter;

    @Resource
    LocalStorageDownloader localStorageDownloader;

    @Override
    public Uploader getUploader() {
        return localStorageUploader;
    }

    @Override
    public Downloader getDownloader() {
        return localStorageDownloader;
    }

    @Override
    public Deleter getDeleter() {
        return localStorageDeleter;
    }
}
