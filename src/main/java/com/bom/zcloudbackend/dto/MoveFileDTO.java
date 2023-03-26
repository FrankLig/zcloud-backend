package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("移动文件DTO")
public class MoveFileDTO {

    private String filePath;

    private String fileName;

    private String oldFilePath;

    private String extendName;
}
