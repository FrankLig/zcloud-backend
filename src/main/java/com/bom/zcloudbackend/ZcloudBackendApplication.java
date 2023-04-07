package com.bom.zcloudbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Frank Liang
 */
@MapperScan("com.bom.zcloudbackend.mapper")
@SpringBootApplication
public class ZcloudBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZcloudBackendApplication.class, args);
    }

}
