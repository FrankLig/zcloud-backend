package com.bom.zcloudbackend.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
@ApiModel("加密文件上传实体")
public class EncUploadFileDTO {

    private String fileName;

    private String fileType;

    private long fileSize;

    private String timeStampName;

    private int success;

    private String message;

    private String url;

    private String filePath;
}
