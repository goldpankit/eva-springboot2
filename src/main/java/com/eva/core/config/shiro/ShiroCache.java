package com.eva.core.config.shiro;

import com.eva.core.utils.Utils;
import com.eva.service.common.CacheProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Shiro缓存
 */
@Scope(value = "prototype")
@Slf4j
@Component
public class ShiroCache implements Cache<Object, Serializable> {

    @Resource
    private CacheProxy<Object, Serializable> cacheProxy;

    @Override
    public Serializable get(Object key) throws CacheException {
        if (key == null) {
            return null;
        }
        return cacheProxy.get(getKey(key));
    }

    /**
     * put方法在创建新的会话和更新会话时均会被调用
     *
     * @param key token
     * @param value 会话对象
     * @return 会话对象
     * @throws CacheException 缓存异常
     */
    @Override
    public Serializable put(Object key, Serializable value) throws CacheException {
        if (key == null) {
            return null;
        }
        // 如果为新会话，则直接添加会话
        if (cacheProxy.get(getKey(key)) == null) {
            cacheProxy.put(getKey(key), value, Utils.AppConfig.getSession().getExpire());
        }
        // 如果为交互式会话，则延长会话的过期时间
        if ("INTERACTIVE".equals(Utils.AppConfig.getSession().getMode())) {
            this.relive(key);
        }
        return value;
    }

    @Override
    public void clear() throws CacheException {
        Set<Object> keys = this.keys();
        cacheProxy.remove(keys);
    }

    @Override
    public int size() {
        return this.keys().size();
    }

    @Override
    public Set<Object> keys() {
        Set<Object> keys = cacheProxy.keys(Utils.AppConfig.getSession().getTokenCachePrefix() + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }
        return keys.stream()
                .map(k -> k.toString().replace(Utils.AppConfig.getSession().getTokenCachePrefix(), ""))
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public Collection<Serializable> values() {
        Collection<Serializable> values = new ArrayList<>();
        Set<Object> keys = this.keys();
        if (CollectionUtils.isEmpty(keys)) {
            return values;
        }
        for (Object k : keys) {
            values.add(cacheProxy.get(k));
        }
        return values;
    }

    @Override
    public Serializable remove(Object key) throws CacheException {
        if (key == null) {
            return null;
        }
        Serializable value = this.get(getKey(key));
        cacheProxy.remove(getKey(key));
        return value;
    }

    /**
     * 重新延长缓存键的存储时长
     *
     * @param key token
     */
    public void relive(Object key) {
        if (key == null) {
            return;
        }
        cacheProxy.relive(getKey(key), Utils.AppConfig.getSession().getExpire());
    }

    /**
     * 获取令牌缓存Key
     *
     * @param key token
     * @return 缓存Key
     */
    private Object getKey (Object key) {
        return (key instanceof String ? (Utils.AppConfig.getSession().getTokenCachePrefix() + key) : key);
    }
}
