package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("删除文件DTO")
public class DeleteFileDTO {

    private Long userFileId;

    private String filePath;

    private String fileName;

    private Integer isDir;

}
