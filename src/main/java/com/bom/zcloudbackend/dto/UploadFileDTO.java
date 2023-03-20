package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UploadFileDTO {

    private String filePath;

    private String uploadTime;

    private String extendTime;

    private String fileName;

    private Long fileSize;

    private int chunkNumber;

    private Long chunkSize;

    private Integer totalChunks;

    private long totalSize;

    private long currentChunkSize;

    @ApiModelProperty("md5")
    private String identifier;
}
