package com.bom.zcloudbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("文件")
@Data
@TableName("file")
public class File {

    @ApiModelProperty("文件ID")
    @TableId(type = IdType.AUTO)
    private Long fileId;

    @ApiModelProperty("时间戳名称")
    private String timeStampName;

    @ApiModelProperty("文件url")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;
}
