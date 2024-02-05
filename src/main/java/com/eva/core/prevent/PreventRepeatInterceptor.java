package com.eva.core.prevent;

import com.eva.core.model.ApiResponse;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 防重复调用处理
 */
@Slf4j
@Component
public class PreventRepeatInterceptor implements HandlerInterceptor {

    private static ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return Boolean.TRUE;
        }
        try {
            // 获取具体执行的方法
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            PreventRepeat prAnnotation = method.getAnnotation(PreventRepeat.class);
            // 接口未添加防重复注解
            if (prAnnotation == null) {
                return Boolean.TRUE;
            }
            // 获取处理器
            PreventRepeatHandlerAdapter adapter = (PreventRepeatHandlerAdapter) applicationContext.getBean(prAnnotation.handler());
            // 验证重复请求
            if(prAnnotation.interval() > 0 && adapter.isRepeat(request, prAnnotation.interval())) {
                log.warn("拦截到重复的请求，url：{}，请求IP：{}", request.getRequestURI(), Utils.User_Client.getIP(request));
                ApiResponse<?> apiResponse = ApiResponse.failed(ResponseStatus.REPEAT_REQUEST);
                if (!"".equals(prAnnotation.message())) {
                    apiResponse.setMessage(prAnnotation.message());
                }
                ApiResponse.response(response, apiResponse);
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            log.warn("防重复处理出现了异常，你可以使用DEBUG模式来查看详细的错误信息！");
            log.debug(e.getMessage(), e);
        }
        return Boolean.TRUE;
    }

    @Resource
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (PreventRepeatInterceptor.applicationContext == null) {
            PreventRepeatInterceptor.applicationContext = applicationContext;
        }
    }
}
