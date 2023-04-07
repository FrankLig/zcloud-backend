package com.bom.zcloudbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Frank Liang
 */
@ApiModel("回收站文件")
@Data
@TableName("recoveryfile")
public class RecoveryFile {

    @TableId(type = IdType.AUTO)
    private Long recoveryFileId;

    private Long userFileId;

    private String deleteTime;

    private String deleteBatchNum;
}
