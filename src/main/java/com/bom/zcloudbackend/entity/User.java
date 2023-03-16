package com.bom.zcloudbackend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("用户")
@Data
@TableName("user")
public class User {

    @ApiModelProperty("用户ID")
    @TableId(type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("加密盐值")
    private String salt;

    @ApiModelProperty("注册时间")
    private String registerTime;

    @ApiModelProperty("加密密钥")
    private String encryptKey;
}
