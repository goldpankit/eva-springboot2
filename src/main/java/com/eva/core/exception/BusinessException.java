package com.eva.core.exception;

import com.eva.core.constants.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Integer code, String message, Throwable e) {
        super(message, e);
        this.code = code;
    }

    public BusinessException(ResponseStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public BusinessException(ResponseStatus status, String message) {
        super(message);
        this.code = status.getCode();
    }

    public BusinessException(ResponseStatus status, Throwable e) {
        super(status.getMessage(), e);
        this.code = status.getCode();
    }

    public BusinessException(ResponseStatus status, String message, Throwable e) {
        super(message, e);
        this.code = status.getCode();
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code=" + code + "," +
                "message=" + getMessage() +
                '}';
    }
}
