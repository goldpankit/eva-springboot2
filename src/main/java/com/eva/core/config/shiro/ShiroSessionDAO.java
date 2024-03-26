package com.eva.core.config.shiro;

import com.eva.core.utils.Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 自定义Shiro SessionDAO，将会话信息存入缓存中
 */
@Data
@Slf4j
@Component
public class ShiroSessionDAO implements SessionDAO {

    @Lazy
    @Resource
    private ShiroCache shiroCache;

    @Resource
    private ShiroTokenManager shiroTokenManager;

    @Override
    public Serializable create(Session session) {
        if (session == null) {
            throw new UnknownSessionException("创建会话失败：session为null");
        }
        Serializable sessionId = shiroTokenManager.build();
        ((SimpleSession)session).setId(sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException{
        if (sessionId == null) {
            log.warn("读取会话失败：sessionId为null");
            return null;
        }
        if (sessionId instanceof String) {
            // 对SessionId进行验证（可用于防止Session捕获、暴力捕捉等一系列安全问题，最终安全性取决于check如何实现）
            shiroTokenManager.check((String) sessionId);
        }
        Session session = getSession(sessionId);
        if (session == null) {
            throw new UnknownSessionException("读取会话失败：根据sessionId'" + sessionId + "'未找到Session对象");
        }
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        // 固定模式，不更新session
        if ("FIXED".equals(Utils.AppConfig.getSession().getMode())) {
            return;
        }
        // 存储或延长Session时长
        this.saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            shiroCache.remove(session.getId());
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<>();
        Set<Object> keys = shiroCache.keys();
        if (keys != null && keys.size() > 0) {
            for (Object key : keys) {
                sessions.add((SimpleSession) shiroCache.get(key));
            }
        }
        return sessions;
    }

    /**
     * 保存会话
     *
     * @param session 会话
     */
    public void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            throw new UnknownSessionException("保存会话失败：会话为null或会话id为null");
        }
        shiroCache.put(session.getId(), (SimpleSession) session);
    }

    /**
     * 获取会话
     *
     * @param sessionId 会话ID
     * @return 会话对象
     */
    public Session getSession (Serializable sessionId) {
        Serializable object = shiroCache.get(sessionId);
        Session session = null;
        if (object != null) {
            session = (Session)shiroCache.get(sessionId);
        }
        return session;
    }
}
