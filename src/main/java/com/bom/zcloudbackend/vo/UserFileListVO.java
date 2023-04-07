package com.bom.zcloudbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@ApiModel("UserFileListVO")
@Data
public class UserFileListVO {

    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件url")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private String fileSize;

    @ApiModelProperty("时间戳名称")
    private String timeStampName;

    @ApiModelProperty("是否OSS存储")
    private Boolean isOSS;

    @ApiModelProperty("引用数量")
    private Integer pointCount;

    @ApiModelProperty("md5")
    private String identifier;

    @ApiModelProperty("用户文件Id")
    private Long userFileId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("扩展名")
    private String extendName;

    @ApiModelProperty("是否为目录")
    private Integer isDir;

    @ApiModelProperty("上传时间")
    private String uploadTime;
}
