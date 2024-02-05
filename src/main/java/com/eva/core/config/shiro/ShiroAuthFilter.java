package com.eva.core.config.shiro;

import com.eva.core.model.ApiResponse;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Shiro认证过滤器，处理未认证情况的响应
 */
public class ShiroAuthFilter extends FormAuthenticationFilter {

    public ShiroAuthFilter() {
        super();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        ApiResponse.response((HttpServletResponse) response, ApiResponse.failed(HttpStatus.UNAUTHORIZED.value(), "未登录或登录信息已过期"));
        return Boolean.FALSE;
    }
}
