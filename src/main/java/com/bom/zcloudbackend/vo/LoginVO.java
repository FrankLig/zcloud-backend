package com.bom.zcloudbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("登录VO")
@Data
public class LoginVO {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("token")
    private String token;
}
