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

    private static final String KEY_PREFIX = "shiro:session:";

    @Resource
    private CacheProxy<Object, Serializable> cacheProxy;

    @Override
    public Serializable get(Object key) throws CacheException {
        if (key == null) {
            return null;
        }
        return cacheProxy.get(getKey(key));
    }

    @Override
    public Serializable put(Object key, Serializable value) throws CacheException {
        if (key == null) {
            return null;
        }
        cacheProxy.put(getKey(key), value, Utils.AppConfig.getSession().getExpire());
        return value;
    }

    public void relive(Object key) {
        if (key == null) {
            return;
        }
        cacheProxy.relive(getKey(key), Utils.AppConfig.getSession().getExpire());
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
        Set<Object> keys = cacheProxy.keys(KEY_PREFIX + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }
        return keys.stream()
                .map(k -> k.toString().replace(KEY_PREFIX, ""))
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

    private Object getKey (Object key) {
        return (key instanceof String ? (KEY_PREFIX + key) : key);
    }
}
