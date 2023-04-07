package com.bom.zcloudbackend.operation.delete.product;

import com.bom.zcloudbackend.common.util.FileUtil;
import com.bom.zcloudbackend.common.util.PathUtil;
import com.bom.zcloudbackend.operation.delete.BaseDeleter;
import com.bom.zcloudbackend.operation.delete.domain.DeleteFile;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * <p>
 * 本地删除实现类
 * </p>
 * @author Frank Liang
 */
@Component
public class LocalStorageDeleter extends BaseDeleter {

    @Override
    public void delete(DeleteFile deleteFile) {
        File file = new File(PathUtil.getStaticPath() + deleteFile.getFileUrl());
        if (file.exists()) {
            file.delete();
        }
        if (FileUtil.isImageFile(FileUtil.getFileExtendName(deleteFile.getFileUrl()))) {
            File minFile = new File(
                PathUtil.getStaticPath() + deleteFile.getFileUrl().replace(deleteFile.getTimeStampName(),
                    deleteFile.getTimeStampName() + "_min"));
            if (minFile.exists()) {
                minFile.delete();
            }
        }
    }
}
