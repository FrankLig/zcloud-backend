package com.bom.zcloudbackend.operation;

import com.bom.zcloudbackend.operation.delete.BaseDeleter;
import com.bom.zcloudbackend.operation.download.BaseDownloader;
import com.bom.zcloudbackend.operation.upload.BaseUploader;


/**
 * @author Frank Liang
 */
public interface FileOperationFactory {

    /**
     * 获取上传类
     * @return
     */
    BaseUploader getUploader();

    /**
     * 获取下载类
     * @return
     */
    BaseDownloader getDownloader();

    /**
     * 获取删除类
     * @return
     */
    BaseDeleter getDeleter();

}
