package com.bom.zcloudbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@TableName("storage")
public class Storage {

    @TableId(type = IdType.AUTO)
    private Long storageId;

    private Long userId;

    private Long storageSize;
}
