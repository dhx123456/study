package com.study.springboot.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置类，为所有的controller生成访问的API文档
 * 本地访问地址：http://localhost:{server.port}/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String CONTROLLER_PACKAGE = "com.study.springboot.controller";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("spring-data-jpa").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE)).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("spring-data-jpa").contact(new Contact("", "", "1326795703@qq.com")).version("1.0").build();
    }
}
