package com.offcn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 10:54
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(value = "com.offcn.user.mapper")
public class ScwUserStart {

    public static void main(String[] args) {
        SpringApplication.run(ScwUserStart.class);
    }
}
