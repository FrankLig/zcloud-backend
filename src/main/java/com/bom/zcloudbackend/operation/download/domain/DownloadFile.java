package com.bom.zcloudbackend.operation.download.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("下载文件")
public class DownloadFile {

    @ApiModelProperty("文件路径")
    private String fileUrl;

    @ApiModelProperty("文件唯一标识")
    private String timeStampName;

}
