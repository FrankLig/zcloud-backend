package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("重命名文件DTO")
public class RenameFileDTO {

    private Long userFileId;

    private String fileName;
}
