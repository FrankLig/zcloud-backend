package com.bom.zcloudbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("回收站文件列表VO")
public class RecoveryFileListVO {

    @ApiModelProperty("回收文件Id")
    private Long recoveryFileId;

    @ApiModelProperty("用户文件Id")
    private Long userFileId;

    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("文件Id")
    private Long fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件拓展名")
    private String extendName;

    @ApiModelProperty("是否文件夹")
    private Integer isDir;

    @ApiModelProperty("上传时间")
    private String uploadTime;

    @ApiModelProperty("删除标志")
    private Integer deleteTag;

    @ApiModelProperty("删除时间")
    private String deleteTime;

    @ApiModelProperty("删除批次号")
    private String deleteBatchNum;


}
