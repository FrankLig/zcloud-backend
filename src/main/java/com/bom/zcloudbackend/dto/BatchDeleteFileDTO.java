package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("批量删除文件DTO")
public class BatchDeleteFileDTO {

    @ApiModelProperty("文件集合")
    private String files;

}
