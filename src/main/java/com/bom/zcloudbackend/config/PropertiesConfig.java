package com.bom.zcloudbackend.config;

import com.bom.zcloudbackend.common.util.PropertiesUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Frank Liang
 */
@Configuration
public class PropertiesConfig {

    @Resource
    private Environment env;

    /**
     * @PostConstruct 修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次
     */
    @PostConstruct
    public void setProperties() {
        PropertiesUtil.setEnvironment(env);
    }

}
