package com.eva.core.secure.twofa;

import java.lang.annotation.*;

/**
 * 启用2FA认证注解，Controller方法添加注解后，将对密码进行二次认证
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableTwoFA {
}
