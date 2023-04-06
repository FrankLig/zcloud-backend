package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("下载文件DTO")
public class DownloadFileDTO {

    private Long userFileId;

}
