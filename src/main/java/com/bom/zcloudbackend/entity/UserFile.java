package com.bom.zcloudbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户文件", description = "用户与文件的关联")
@Data
@TableName("userfile")
public class UserFile {

    @ApiModelProperty("用户文件ID")
    @TableId(type = IdType.AUTO)
    private Long userFileId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("扩展名")
    private String extendName;

    @ApiModelProperty("是否目录")
    //是否为目录，0-否，1-是
    private Integer isDir;

    @ApiModelProperty("上传时间")
    private String uploadTime;
}
