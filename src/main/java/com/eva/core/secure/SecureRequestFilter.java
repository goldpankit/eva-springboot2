package com.eva.core.secure;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.servlet.ServletDuplicateInputStream;
import com.eva.core.servlet.ServletDuplicateRequestWrapper;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 请求参数过滤器，实现参数的解密和重新注入
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/*", filterName = "requestParamFilter")
public class SecureRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 不存在加密接口，直接放行
        if (Utils.AppConfig.getSecurity().getTransmission().getPathPatterns().length == 0) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String uri = httpServletRequest.getRequestURI().replace(httpServletRequest.getContextPath(), "");
        // 解密请求参数
        if (Utils.Secure.isSecureRequest(httpServletRequest)) {
            // 防止请求流被拦截器进行一次读取后关闭导致流无法连续读取的问题
            ServletDuplicateRequestWrapper requestWrapper = new ServletDuplicateRequestWrapper(httpServletRequest);
            ServletDuplicateInputStream duplicateInputStream = ((ServletDuplicateInputStream)requestWrapper.getInputStream());
            // 解密URL参数
            String requestParameter = servletRequest.getParameter("_p");
            if (StringUtils.isNotBlank(requestParameter)) {
                try {
                    requestParameter = Utils.Secure.decryptTransmission(requestParameter);
                    JSONObject paramsJson = JSON.parseObject(requestParameter);
                    for (String key : paramsJson.keySet()) {
                        requestWrapper.addParameter(key, paramsJson.get(key));
                    }
                } catch (SecurityException e) {
                    throw new IllegalArgumentException("解密接口" + uri + "失败！", e);
                }
            }
            // 解密body参数
            String body = duplicateInputStream.getBody();
            if (StringUtils.isNotBlank(body)) {
                JSONObject jsonBody;
                try {
                    jsonBody = JSON.parseObject(body);
                } catch (Exception e) {
                    throw new BusinessException(ResponseStatus.BAD_REQUEST, "解密接口" + uri + "失败，接口参数需为J正确的SON字符串！");
                }
                String requestBody = jsonBody.getString("_p");
                // 没有加密参数
                if (jsonBody.keySet().size() > 0 && StringUtils.isBlank(requestBody)) {
                    throw new BusinessException(ResponseStatus.BAD_REQUEST, "解密接口" + uri + "失败，接口需传递_p参数！");
                }
                try {
                    duplicateInputStream.setBody(Utils.Secure.decryptTransmission(requestBody));
                } catch (SecurityException e) {
                    throw new BusinessException(ResponseStatus.BAD_REQUEST, "解密接口" + uri + "失败！", e);
                }
            }
            filterChain.doFilter(requestWrapper, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
