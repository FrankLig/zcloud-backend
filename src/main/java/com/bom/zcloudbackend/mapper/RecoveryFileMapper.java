package com.bom.zcloudbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bom.zcloudbackend.entity.RecoveryFile;
import com.bom.zcloudbackend.vo.RecoveryFileListVO;

import java.util.List;

/**
 * @author Frank Liang
 */
public interface RecoveryFileMapper extends BaseMapper<RecoveryFile> {

    List<RecoveryFileListVO> selectRecoveryFileList();
}
