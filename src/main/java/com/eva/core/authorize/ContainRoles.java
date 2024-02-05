package com.eva.core.authorize;

import java.lang.annotation.*;

/**
 * 包含全部角色注解
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContainRoles {

    String[] value();
}
