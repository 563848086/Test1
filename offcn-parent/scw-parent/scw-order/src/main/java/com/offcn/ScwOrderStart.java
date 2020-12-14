package com.offcn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * @Auther: lhq
 * @Date: 2020/9/8 09:21
 * @Description:
 */

@SpringCloudApplication   //@SpringBootApplication+@EnableDiscoveryClient+@EnableFeignClients
@EnableCircuitBreaker
@MapperScan("com.offcn.order.mapper")
public class ScwOrderStart {
    public static void main(String[] args) {
        SpringApplication.run(ScwOrderStart.class);
    }
}
