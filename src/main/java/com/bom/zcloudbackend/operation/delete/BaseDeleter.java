package com.bom.zcloudbackend.operation.delete;

import com.bom.zcloudbackend.operation.delete.domain.DeleteFile;

/**
 * @author Frank Liang
 */
public abstract class BaseDeleter {

    /**
     * 删除文件
     * @param deleteFile
     */
    public abstract void delete(DeleteFile deleteFile);
}
