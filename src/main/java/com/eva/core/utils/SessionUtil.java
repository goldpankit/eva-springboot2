package com.eva.core.utils;

import com.eva.core.model.LoginUserInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * 会话工具类
 */
@Component("UtilSession")
public final class SessionUtil {

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户信息
     */
    public LoginUserInfo getLoginUser () {
        return (LoginUserInfo) SecurityUtils.getSubject().getPrincipal();
    }

}
