package com.bom.zcloudbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("文件上传VO")
public class UploadFileVO {

    @ApiModelProperty("时间戳")
    private String timeStampName;

    @ApiModelProperty("跳过上传")
    private boolean skipUpload;

    @ApiModelProperty("是否合并分片")
    private boolean needMerge;

    @ApiModelProperty("已上传分片")
    private List<Integer> uploaded;
}
