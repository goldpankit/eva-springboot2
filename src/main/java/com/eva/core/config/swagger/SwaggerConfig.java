package com.eva.core.config.swagger;

import com.eva.core.model.AppConfig;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;

/**
 * Swagger配置
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Resource
    private AppConfig projectConfig;

    @Bean
    public ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title(projectConfig.getApiDoc().getTitle())
                .description(projectConfig.getApiDoc().getDescription())
                .version(projectConfig.getVersion())
                .build();
    }

    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.getApiInfo())
                .host(projectConfig.getApiDoc().getHost())
                .select()
                // 设置需要被扫描的类，这里设置为添加了@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }
}
