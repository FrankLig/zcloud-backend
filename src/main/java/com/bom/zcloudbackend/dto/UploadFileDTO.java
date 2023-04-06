package com.bom.zcloudbackend.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("上传文件DTO")
public class UploadFileDTO {

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("上传时间")
    private String uploadTime;

    @ApiModelProperty("文件扩展名")
    private String extendName;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "分块编号",notes = "从1开始计数")
    private int chunkNumber;

    @ApiModelProperty(value = "分块大小")
    private Long chunkSize;

    @ApiModelProperty("文件被分成的块数")
    private Integer totalChunks;

    @ApiModelProperty("文件总大小")
    private long totalSize;

    @ApiModelProperty("当前块实际大小")
    private long currentChunkSize;

    @ApiModelProperty(value = "md5",notes = "文件唯一标识")
    private String identifier;
}
