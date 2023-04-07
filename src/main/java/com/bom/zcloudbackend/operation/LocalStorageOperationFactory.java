package com.bom.zcloudbackend.operation;

import com.bom.zcloudbackend.operation.delete.BaseDeleter;
import com.bom.zcloudbackend.operation.delete.product.LocalStorageDeleter;
import com.bom.zcloudbackend.operation.download.BaseDownloader;
import com.bom.zcloudbackend.operation.download.product.LocalStorageDownloader;
import com.bom.zcloudbackend.operation.upload.BaseUploader;
import com.bom.zcloudbackend.operation.upload.product.LocalStorageUploader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Frank Liang
 */
@Component
public class LocalStorageOperationFactory implements FileOperationFactory {

    @Resource
    LocalStorageUploader localStorageUploader;

    @Resource
    LocalStorageDeleter localStorageDeleter;

    @Resource
    LocalStorageDownloader localStorageDownloader;

    @Override
    public BaseUploader getUploader() {
        return localStorageUploader;
    }

    @Override
    public BaseDownloader getDownloader() {
        return localStorageDownloader;
    }

    @Override
    public BaseDeleter getDeleter() {
        return localStorageDeleter;
    }
}
