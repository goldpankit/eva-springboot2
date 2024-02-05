package com.eva.core.secure.field;

import java.lang.annotation.*;

/**
 * 启用安全字段注解，添加该注解后，标识对象中存在安全字段
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableSecureField {

}
