package com.bom.zcloudbackend.operation.delete;

import com.bom.zcloudbackend.operation.delete.domain.DeleteFile;

public abstract class Deleter {

    public abstract void delete(DeleteFile deleteFile);
}
