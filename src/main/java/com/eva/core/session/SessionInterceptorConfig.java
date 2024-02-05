package com.eva.core.session;

import com.eva.core.model.AppConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Swagger拦截器配置
 */
@Configuration
public class SessionInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private SessionInterceptor sessionInterceptor;

    @Resource
    private AppConfig projectConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(sessionInterceptor);
        // 添加指定拦截的路径
        registration.addPathPatterns(
                projectConfig.getSession().getInterceptor().getPathPatterns());
        // 添加指定排除拦截的路径
        registration.excludePathPatterns(
                projectConfig.getSession().getInterceptor().getExcludePathPatterns());
    }
}
