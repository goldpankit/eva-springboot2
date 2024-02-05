package com.eva.core.secure;

import com.eva.api.BaseController;
import com.eva.core.model.ApiResponse;
import com.eva.core.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RestControllerAdvice(basePackageClasses = BaseController.class)
public class SecurityResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return Boolean.TRUE;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 不存在加密接口，直接返回结果对象
        if (Utils.AppConfig.getSecurity().getTransmission().getPathPatterns().length == 0) {
            return o;
        }
        // 非ApiResponse对象，不做加密处理
        if (!(o instanceof ApiResponse)) {
            return o;
        }
        // 加密ApiResponse
        HttpServletRequest httpServletRequest = ((ServletServerHttpRequest)serverHttpRequest).getServletRequest();
        if (Utils.Secure.isSecureRequest(httpServletRequest)) {
            try {
                String responseBody = new String(Utils.SpringContext.getBean(Jackson2ObjectMapperBuilder.class)
                        .build()
                        .writeValueAsBytes(o), StandardCharsets.UTF_8);
                return Utils.Secure.encryptTransmission(responseBody);
            } catch (SecurityException e) {
                log.error("加密响应失败！", e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return o;
    }
}
