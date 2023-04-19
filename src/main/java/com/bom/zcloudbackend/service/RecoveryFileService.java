package com.bom.zcloudbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bom.zcloudbackend.entity.RecoveryFile;
import com.bom.zcloudbackend.vo.RecoveryFileListVO;

import java.util.List;

/**
 * @author Frank Liang
 */

public interface RecoveryFileService extends IService<RecoveryFile> {

    /**
     * 永久删除文件
     * @param deleteBatchNum
     */
    void deleteUserFileByDeleteBatchNum(String deleteBatchNum);

    /**
     * 恢复文件
     * @param deleteBatchNum
     * @param filePath
     * @param sessionUserId
     */
    void restoreFile(String deleteBatchNum, String filePath,Long sessionUserId);

    /**
     * 查询回收站文件列表
     * @param userId
     * @return
     */
    List<RecoveryFileListVO> selectRecoveryFileList(Long userId);

}
