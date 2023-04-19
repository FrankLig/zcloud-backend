package com.bom.zcloudbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bom.zcloudbackend.entity.RecoveryFile;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.mapper.RecoveryFileMapper;
import com.bom.zcloudbackend.mapper.UserFileMapper;
import com.bom.zcloudbackend.service.RecoveryFileService;
import com.bom.zcloudbackend.vo.RecoveryFileListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Frank Liang
 */
@Service
public class RecoveryFileServiceImpl extends ServiceImpl<RecoveryFileMapper, RecoveryFile> implements
    RecoveryFileService {

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private RecoveryFileMapper recoveryFileMapper;



    @Override
    public void deleteUserFileByDeleteBatchNum(String deleteBatchNum) {
        LambdaQueryWrapper<UserFile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userFileLambdaQueryWrapper.eq(UserFile::getDeleteBatchNum,deleteBatchNum);
        userFileMapper.delete(userFileLambdaQueryWrapper);
    }

    @Override
    public void restoreFile(String deleteBatchNum, String filePath, Long sessionUserId) {
        LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userFileLambdaUpdateWrapper.set(UserFile::getDeleteTag,0)
            .set(UserFile::getDeleteBatchNum,"")
            .eq(UserFile::getDeleteBatchNum,deleteBatchNum);
        userFileMapper.update(null,userFileLambdaUpdateWrapper);


    }

    @Override
    public List<RecoveryFileListVO> selectRecoveryFileList(Long userId) {
        return recoveryFileMapper.selectRecoveryFileList(userId);
    }
}
