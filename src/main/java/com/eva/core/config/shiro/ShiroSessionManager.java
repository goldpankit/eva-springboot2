package com.eva.core.config.shiro;

import com.eva.core.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 自定义会话管理器
 */
@Slf4j
public class ShiroSessionManager extends DefaultSessionManager implements WebSessionManager {

    @Override
    protected void onStart(Session session, SessionContext context) {
        super.onStart(session, context);
        if (!WebUtils.isHttp(context)) {
            log.debug("SessionContext参数不兼容Http，或者没有Http请求/响应对。");
            return;
        }
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        HttpServletResponse response = WebUtils.getHttpResponse(context);
        Serializable sessionId = session.getId();
        this.storeSessionId(sessionId, request, response);
        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }

    @Override
    public Serializable getSessionId(SessionKey key) {
        Serializable sessionId = super.getSessionId(key);
        if (sessionId == null && WebUtils.isWeb(key)) {
            ServletRequest servletRequest = WebUtils.getRequest(key);
            if (!(servletRequest instanceof HttpServletRequest)) {
                log.debug("无法从请求头中获取sessionId，该请求不是一个HttpServletRequest对象！");
                return null;
            }
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            // 从cookie中获取认证
            javax.servlet.http.Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (javax.servlet.http.Cookie cookie : cookies) {
                    if (Constants.HEADER_TOKEN.equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            // 从header中获取认证
            return request.getHeader(Constants.HEADER_TOKEN);
        }
        return sessionId;
    }

    @Override
    public boolean isServletContainerSessions() {
        return false;
    }

    private void storeSessionId(Serializable currentId, HttpServletRequest request, HttpServletResponse response) {
        if (currentId == null) {
            throw new IllegalArgumentException("会话ID为null，无法持久化会话ID！");
        }
        Cookie cookie = new SimpleCookie(Constants.HEADER_TOKEN);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        String idString = currentId.toString();
        cookie.setValue(idString);
        cookie.saveTo(request, response);
        log.debug("存储会话ID到cookie中，会话ID：{}", idString);
    }
}
