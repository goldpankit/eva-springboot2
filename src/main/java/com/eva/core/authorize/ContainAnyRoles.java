package com.eva.core.authorize;

import java.lang.annotation.*;

/**
 * 包含任意角色注解
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContainAnyRoles {

    String[] value();
}
