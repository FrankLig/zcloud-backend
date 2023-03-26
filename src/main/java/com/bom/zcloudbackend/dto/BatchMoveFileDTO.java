package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("批量移动文件DTO")
public class BatchMoveFileDTO {

    @ApiModelProperty("文件集合")
    private String files;

    @ApiModelProperty("文件路径")
    private String filePath;
}
