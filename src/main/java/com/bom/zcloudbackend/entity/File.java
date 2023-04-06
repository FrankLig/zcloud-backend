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

    @ApiModelProperty(name = "存储类型", notes = "0-本地存储，1-阿里云，2-FastDFS")
    private Integer storageType;

    @ApiModelProperty("md5唯一标识")
    private String identifier;

    @ApiModelProperty(value = "引用数量", notes = "上传文件服务器已存在，pc+1,删除时-1,大于0文件逻辑删除，等于0彻底物理删除")
    private Integer pointCount;
}
