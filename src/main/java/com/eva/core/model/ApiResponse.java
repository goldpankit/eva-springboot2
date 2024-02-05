package com.eva.core.model;

import com.alibaba.fastjson.JSON;
import com.eva.core.constants.ResponseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 接口返回对象
 */
@Slf4j
@ApiModel("响应对象")
@Data
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

    @ApiModelProperty(value = "响应码")
    private int code;

    @ApiModelProperty(value = "请求是否成功")
    private boolean success;

    @ApiModelProperty(value = "错误消息")
    private String message;

    @ApiModelProperty(value = "数据")
    private T data;

    @ApiModelProperty(value = "异常消息")
    private String exception;

    public ApiResponse () {}

    /**
     * 请求成功
     *
     * @param data 数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.success("请求成功", data);
    }

    /**
     * 请求成功
     *
     * @param message 消息
     * @param data 数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), Boolean.TRUE, message, data, null);
    }

    /**
     * 请求失败
     *
     * @param message 消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> failed(String message) {
        return ApiResponse.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    /**
     * 请求失败
     *
     * @param status 响应状态
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> failed(ResponseStatus status) {
        return ApiResponse.failed(status.getCode(), status.getMessage());
    }

    /**
     * 请求失败
     *
     * @param status 响应状态
     * @param ex 异常对象
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> failed(ResponseStatus status, Throwable ex) {
        return ApiResponse.failed(status.getCode(), status.getMessage(), ex);
    }

    /**
     * 请求失败
     *
     * @param code 响应码
     * @param message 消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> failed(Integer code, String message) {
        return ApiResponse.failed(code, message, null);
    }

    /**
     * 请求失败
     *
     * @param code 响应码
     * @param message 消息
     * @param ex 异常
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> failed(Integer code, String message, Throwable ex) {
        if (ex == null) {
            return new ApiResponse<>(code, Boolean.FALSE, message, null, null);
        }
        // 处理异常栈，防止过多内容导致响应内容过大
        StackTraceElement[] trace = ex.getStackTrace();
        StringBuilder exceptionStack = new StringBuilder(ex + "\n");
        for (StackTraceElement traceElement : trace) {
            exceptionStack.append("\tat ").append(traceElement).append("\n");
            if (exceptionStack.length() > 5000) {
                break;
            }
        }
        return new ApiResponse<>(code, Boolean.FALSE, message, null, exceptionStack.toString());
    }

    /**
     * 直接通过HttpServletResponse写出响应结果
     *
     * @param response 响应
     * @param apiResponse ApiResponse
     */
    public static void response (HttpServletResponse response, ApiResponse<?> apiResponse) {
        try {
            response.setHeader("content-type", "application/json;charset=UTF-8");
            response.getOutputStream().write(JSON.toJSONString(apiResponse).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("EVA: response throw an exception", e);
        }
    }
}
