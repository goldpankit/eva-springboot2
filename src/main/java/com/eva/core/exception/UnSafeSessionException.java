package com.eva.core.exception;

/**
 * 不安全的会话异常
 */
public class UnSafeSessionException extends RuntimeException {

    public UnSafeSessionException () {
        super("不安全的会话");
    }
}
