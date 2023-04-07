package com.bom.zcloudbackend.operation.upload.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("文件上传分片实体")
public class UploadFile {

    private String fileName;

    private String fileType;

    private long fileSize;

    private String timeStampName;

    private int success;

    private String message;

    private String url;

    //切片上传相关参数
    @ApiModelProperty("文件上传任务ID")
    private String taskId;

    @ApiModelProperty("当前第几分片")
    private int chunkNumber;

    @ApiModelProperty("每个分片的大小")
    private long chunkSize;

    @ApiModelProperty("分片总数")
    private int totalChunks;

    @ApiModelProperty("文件唯一标识")
    private String identifier;

    @ApiModelProperty("总大小")
    private long totalSize;

    @ApiModelProperty("当前分片大小")
    private long currentChunkSize;


}
