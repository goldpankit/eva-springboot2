package com.eva.core.session;

import com.eva.core.config.shiro.ShiroSessionDAO;
import com.eva.core.model.AppConfig;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 会话拦截处理
 */
@Slf4j
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Resource
    private ShiroSessionDAO shiroSessionDAO;

    @Resource
    private AppConfig projectConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 交互式会话，自动延长会话过期时间
        if ("INTERACTIVE".equals(projectConfig.getSession().getMode())) {
            try {
                Serializable sessionId = Utils.Session.getSessionId();
                if (sessionId != null) {
                    shiroSessionDAO.relive(Utils.Session.getSessionId());
                }
            } catch (Exception ignored) {
            }
        }
        return Boolean.TRUE;
    }

}
