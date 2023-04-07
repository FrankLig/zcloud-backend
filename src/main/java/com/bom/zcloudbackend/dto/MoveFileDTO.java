package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("移动文件DTO")
public class MoveFileDTO {

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("旧文件路径")
    private String oldFilePath;

    @ApiModelProperty("扩展名")
    private String extendName;
}
