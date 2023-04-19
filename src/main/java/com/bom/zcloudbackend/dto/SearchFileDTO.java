package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("搜索文件DTO")
public class SearchFileDTO {

    @ApiModelProperty("查询文件关键字")
    String fileName;

    @ApiModelProperty("当前页")
    private Long currentPage;

    @ApiModelProperty("每页记录数")
    private Long pageCount;

}
