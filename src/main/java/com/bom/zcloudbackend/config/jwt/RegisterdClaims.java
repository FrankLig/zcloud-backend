package com.bom.zcloudbackend.config.jwt;

import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
public class RegisterdClaims {

    private String iss;

    private String exp;

    private String sub;

    private String aud;

}
