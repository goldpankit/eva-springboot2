package com.eva.core.config.swagger;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Swagger拦截器
 */
@Slf4j
@Component
public class SwaggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!Utils.AppConfig.getApiDoc().getEnabled() || Utils.AppConfig.isProductionEnv()) {
            String uri = request.getContextPath();
            if (StringUtils.isNotBlank(Utils.AppConfig.getApiDoc().getRedirectUri()))
                uri = request.getContextPath() + Utils.AppConfig.getApiDoc().getRedirectUri();
            if (StringUtils.isBlank(uri))
                uri = "/";
            try {
                response.sendRedirect(uri);
            } catch (IOException e) {
                log.error(String.format("接口文档未启用，重定向到 '%s' 出现了错误 : %s", uri, e.getMessage()), e);
            }
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
