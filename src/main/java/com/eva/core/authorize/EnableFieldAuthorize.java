package com.eva.core.authorize;

import java.lang.annotation.*;

/**
 * 启动字段授权
 * 添加该注解后，将根据权限注解进行授权处理，没有权限则重置对应字段值为空值。
 * 如果字段中包含List，且泛型值开启了字段授权，则会对List中的每一个元素进行授权处理。
 * 如果字段中包含了开启了字段授权的对象，则会对该对象进行验证。
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableFieldAuthorize {
}
