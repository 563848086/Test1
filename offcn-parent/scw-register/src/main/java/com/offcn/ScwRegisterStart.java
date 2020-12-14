package com.offcn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 10:25
 * @Description:
 */
@SpringBootApplication
@EnableEurekaServer
public class ScwRegisterStart {
    public static void main(String[] args) {
        SpringApplication.run(ScwRegisterStart.class);
    }
}
