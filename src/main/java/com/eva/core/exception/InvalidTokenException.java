package com.eva.core.exception;

/**
 * 无效令牌异常
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("无效的令牌");
    }
}
