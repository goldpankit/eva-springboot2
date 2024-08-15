package com.eva.core.prevent;

import java.lang.annotation.*;

/**
 * 防重复注解
 * 防重复逻辑：默认情况下，记录请求地址、登录令牌和请求IP作为请求区分的因子，只有三者相同才视为是同一请求
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventRepeat {

    /**
     * 防重复规则设定类
     */
    Class<?> handler() default PreventRepeatDefaultHandler.class;

    /**
     * 间隔时间(ms)，在此时间再次被调用视为重复调用
     */
    int interval() default 800;

    /**
     * 错误消息
     */
    String message() default "请求过于频繁";

}
