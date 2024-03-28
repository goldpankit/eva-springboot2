package com.eva.service.common;

import com.eva.core.cache.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 缓存代理类，便于缓存变更
 */
@Slf4j
@Component
public class CacheProxy<K,V> {

    @Resource
    private LocalCache<K,V> localCache;

    /**
     * 获取缓存值
     *
     * @param key 缓存键
     * @return V
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }
        return localCache.get(key);
    }

    /**
     * 写入缓存值
     *
     * @param key 缓存键
     * @param value 缓存值
     * @return V
     */
    public V put(K key, V value) {
        localCache.put(key, value);
        return value;
    }

    /**
     * 指定过期时长
     *
     * @param key 缓存键
     * @param value 缓存值
     * @param expire 过期时间(s)
     * @return V
     */
    public V put(K key, V value, int expire) {
        localCache.put(key, value, expire * 1000L);
        return value;
    }

    /**
     * 指定过期时长
     *
     * @param key 缓存键
     * @param value 缓存值
     * @param expire 过期时间(ms)
     * @return V
     */
    public V put(K key, V value, long expire) {
        log.debug("写入缓存, key = [{}]，超时时间 = {}ms", key, expire);
        if (key == null) {
            throw new NullPointerException("缓存Key不能为null");
        }
        localCache.put(key, value, expire);
        return value;
    }

    /**
     * 刷新缓存
     *
     * @param key 缓存键
     * @param expire 过期时间(s)
     */
    public void relive(K key, int expire) {
        this.relive(key, expire * 1000L);
    }

    /**
     * 刷新缓存
     *
     * @param key 缓存键
     * @param expire 过期时间(ms)
     */
    public void relive(K key, long expire) {
        log.debug("刷新缓存, key = [{}]", key);
        localCache.relive(key);
    }

    /**
     * 清理缓存
     */
    public void clear() {
        log.debug("清理缓存");
        localCache.clear();
    }

    /**
     * 获取缓存数
     *
     * @return int
     */
    public int size() {
        return localCache.size();
    }

    /**
     * 获取缓存key
     *
     * @return Set<K>
     */
    public Set<K> keys() {
        return localCache.keys();
    }

    /**
     * 获取缓存key
     *
     * @param keyPattern 缓存键正则
     * @return Set<K>
     */
    public Set<K> keys(String keyPattern) {
        return localCache.keys(keyPattern);
    }

    /**
     * 获取所有缓存值
     *
     * @return Collection<V>
     */
    public Collection<V> values() {
        Collection<V> values = localCache.values();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        return values;
    }

    /**
     * 获取所有缓存值
     *
     * @param key 缓存键
     */
    public void remove(K key) {
        log.debug("删除缓存, key = [{}]", key);
        if (key == null) {
            return;
        }
        localCache.remove(key);
    }

}
