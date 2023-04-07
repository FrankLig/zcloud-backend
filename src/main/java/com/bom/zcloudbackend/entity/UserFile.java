package com.bom.zcloudbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Frank Liang
 */
@ApiModel(value = "用户文件", description = "用户与文件的关联")
@Data
@TableName("userfile")
public class UserFile {

    @ApiModelProperty("用户文件ID")
    @TableId(type = IdType.AUTO)
    private Long userFileId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("扩展名")
    private String extendName;

    @ApiModelProperty(value = "是否目录",notes = "0-不是目录，1-是目录")
    private Integer isDir;

    @ApiModelProperty("上传时间")
    private String uploadTime;

    @ApiModelProperty(value = "删除标志",notes = "0-未删除，1-已删除")
    private Integer deleteTag;

    @ApiModelProperty("删除时间")
    private String deleteTime;

    @ApiModelProperty("删除批次号")
    private String deleteBatchNum;


}
