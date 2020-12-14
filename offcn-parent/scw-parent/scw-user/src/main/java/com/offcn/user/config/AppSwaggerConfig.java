package com.offcn.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 11:42
 * @Description:
 */
@Configuration
@EnableSwagger2  //开启Swagger接口文档
public class AppSwaggerConfig {


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("七易众筹项目接口文档")
                .contact("lhq")
                .version("1.0")
                .description("给前端开发人员使用的说明文档")
                .termsOfServiceUrl("http://www.ujiuye.com")
                .build();
    }


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)   //说明文档类型
                .apiInfo(apiInfo())         //引入文档说明信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.offcn.user.controller"))     //引入文档扫描路径
                .paths(PathSelectors.any())
                .build();
    }


}
