package com.bom.zcloudbackend.config.jwt;

import lombok.Data;

/**
 * @author Frank Liang
 */
@Data
public class JwtHeader {

    private String alg;

    private String typ;

}
