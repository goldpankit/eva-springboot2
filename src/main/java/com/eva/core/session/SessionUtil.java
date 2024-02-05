package com.eva.core.session;

import com.eva.core.model.LoginUserInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;

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

    /**
     * 获得shiro session
     *
     * @return 会话对象
     */
    public Serializable getSessionId () {
        org.apache.shiro.session.Session session = SecurityUtils.getSubject().getSession();
        if (session == null) {
            return null;
        }
        return session.getId();
    }
}
