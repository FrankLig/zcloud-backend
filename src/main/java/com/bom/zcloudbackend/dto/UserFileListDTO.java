package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@ApiModel("文件列表DTO")
@Data
public class UserFileListDTO {

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("当前页")
    private Long currentPage;

    @ApiModelProperty("每页记录数")
    private Long pageCount;

}
