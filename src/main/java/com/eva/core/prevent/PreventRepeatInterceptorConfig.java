package com.eva.core.prevent;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Swagger拦截器配置
 */
@Configuration
public class PreventRepeatInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private PreventRepeatInterceptor preventRepeatInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(preventRepeatInterceptor).addPathPatterns("/**");
    }
}
