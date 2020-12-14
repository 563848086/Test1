package com.offcn.project.config;

import com.offcn.utils.OSSTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: lhq
 * @Date: 2020/9/7 10:32
 * @Description:
 */
@Configuration
public class AppProjectConfig {

    //声明OSS模板类
    @ConfigurationProperties(prefix = "oss")
    @Bean  //<bean class= id=>
    public OSSTemplate ossTemplate(){
        return new OSSTemplate();
    }
}
