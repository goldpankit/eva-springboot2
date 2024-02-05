package com.eva.core.secure.field;

import java.lang.annotation.*;

/**
 * 安全字段注解
 * 添加该注解后表示字段需加密或解密
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecureField {
}
