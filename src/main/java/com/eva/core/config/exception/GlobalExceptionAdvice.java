package com.eva.core.config.exception;

import com.eva.core.exception.BusinessException;
import com.eva.core.model.ApiResponse;
import com.eva.core.constants.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@Component
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public Object handleBusinessException (BusinessException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.failed(e.getCode(), e.getMessage());
    }

    /**
     * 无权限异常处理
     */
    @ExceptionHandler(UnauthorizedException.class)
    public Object handleUnauthorizedException (UnauthorizedException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.failed("没有操作权限");
    }

    /**
     * 参数验证未通过异常处理
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object handleMissingServletRequestParameterException (MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.failed(ResponseStatus.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 其它异常处理
     */
    @ExceptionHandler(Exception.class)
    public Object handleException (Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponse.failed(ResponseStatus.SERVER_ERROR, e);
    }
}
