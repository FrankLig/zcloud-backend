package com.bom.zcloudbackend.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("重命名文件DTO")
public class RenameFileDTO {

    private Long userFileId;

    private String fileName;
}
