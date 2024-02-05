package com.eva.core.authorize;

import java.lang.annotation.*;

/**
 * 授权表达式注解，用法如下
 * - 是超级管理员：@AuthorizeExpress("isSuperAdmin()")
 * - 必须包含指定的角色或多个角色：@AuthorizeExpress("hasRoles('admin', 'manager')")
 * - 必须包含指定的权限或多个权限：@AuthorizeExpress("hasPermissions('system:user:create', 'system:user:new')")
 * - 必须包含指定的角色和权限：@AuthorizeExpress("hasRoles('admin') && hasPermissions('system:user:create')")
 * - 包含指定的任意角色：@AuthorizeExpress("hasAnyRoles('admin', 'manager')")
 * - 包含指定的任意权限：@AuthorizeExpress("hasAnyPermissions('system:user:create', 'system:user:new')")
 * - 包含指定的任意角色或任意权限：@AuthorizeExpress("hasAnyRoles('admin') || hasAnyPermissions('system:user:create')")
 * - 必须包含指定的角色和权限：@AuthorizeExpress("hasRoles('admin') || hasPermissions('system:user:create')")
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorizeExpress {

    String value() default "";
}
