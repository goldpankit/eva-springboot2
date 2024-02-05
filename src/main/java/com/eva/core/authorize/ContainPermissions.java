package com.eva.core.authorize;

import java.lang.annotation.*;

/**
 * 包含全部权限注解
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContainPermissions {

    String[] value();
}
