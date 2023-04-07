package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("下载文件DTO")
public class DownloadFileDTO {

    @ApiModelProperty("用户文件ID")
    private Long userFileId;

}
