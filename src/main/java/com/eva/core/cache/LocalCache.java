package com.eva.core.cache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单机本地缓存，项目初始阶段使用
 */
@Slf4j
@Component
public final class LocalCache<K,V> {

    // 数据存储对象
    private final ConcurrentHashMap<K, Value<V>> cache = new ConcurrentHashMap<>();

    /**
     * 添加缓存，超时时间为30分钟
     *
     * @param key 缓存键
     * @param value 缓存值
     */
    public void put(K key, V value) {
        Value<V> v = cache.get(key);
        if (v != null) {
            v.value = value;
            return;
        }
        put(key, value, -1L);
    }

    /**
     * 添加缓存
     *
     * @param key 缓存键
     * @param value 缓存值
     * @param timeout 超时时间
     */
    public void put(K key, V value, long timeout) {
        if(key == null) {
            throw new NullPointerException("缓存键不能为null");
        }
        // 清理旧数据
        for(Map.Entry<K, Value<V>> entry: cache.entrySet()){
            Value<?> v = entry.getValue();
            // 值为空时清除掉
            if(v.getValue() == null) {
                cache.remove(entry.getKey());
            }
            // 超时时清除掉
            if(this.isExpired(v)) {
                cache.remove(entry.getKey());
            }
        }
        // 添加缓存
        Value<V> v = new Value<>();
        v.setBirthtime(System.currentTimeMillis());
        v.setTimeout(timeout);
        v.setValue(value);
        cache.put(key, v);

        // 获取对象大小，超过一定大小打印警告
        int cacheSize = SerializationUtils.serialize(cache).length;
        // 警告大小
        final int WARN_SIZE = 1073741824;
        if(cacheSize > WARN_SIZE) {
            // GB大小
            final int GB_SIZE = 1073741824;
            log.warn("本地缓存已超过{}G，当前缓存容量为：{}G", (double) Math.round((double) WARN_SIZE / GB_SIZE * 100) / 100, (double) Math.round((double) cacheSize / GB_SIZE * 100) / 100);
        }
    }

    /**
     * 刷新缓存
     *
     * @param key 缓存键
     */
    public void relive(K key) {
        if (key == null) return;
        Value<?> v = cache.get(key);
        if (v == null) {
            return;
        }
        v.setBirthtime(new Date().getTime());
    }

    /**
     * 根据key获取缓存对象
     *
     * @param key 缓存键
     */
    public V get(K key) {
        if(key == null) return null;
        Value<V> value = cache.get(key);
        if(value == null) {
            return null;
        }
        if(value.getValue() == null) {
            return null;
        }
        // 已过期
        if (this.isExpired(value)) {
            remove(key);
            return null;
        }
        return value.getValue();
    }

    /**
     * 根据key获取缓存对象
     *
     * @param key 缓存键
     */
    public void remove(K key) {
        log.debug("删除{}", key);
        if(key == null) return;
        Value<V> value = cache.get(key);
        if (value == null) {
            return;
        }
        cache.remove(key);
    }

    /**
     * 清空缓存
     */
    public void clear () {
        cache.clear();
    }

    /**
     * 获取缓存数
     */
    public int size () {
        return cache.size();
    }

    /**
     * 获取缓存所有的key
     *
     * @return Keys
     */
    public Set<K> keys () {
        return cache.keySet();
    }

    /**
     * 获取缓存key
     *
     * @param keyPattern key正则
     * @return Keys
     */
    public Set<K> keys (String keyPattern) {
        if(StringUtils.isBlank(keyPattern)) {
            return Collections.emptySet();
        }
        String pattern = StringUtils.replace(keyPattern, "*", ".*");
        Set<K> keys = this.keys();
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }
        Set<K> filterKeys = new HashSet<>();
        for (K key : keys) {
            if (key instanceof String && ((String) key).matches(pattern)) {
                filterKeys.add(key);
            }
        }
        return filterKeys;
    }

    /**
     * 获取缓存值
     */
    public Collection<V> values () {
        Collection<Value<V>> values = cache.values();
        List<V> vs = new ArrayList<>(keys().size());
        for (Value<V> v : values) {
            if (this.isExpired(v)) {
                continue;
            }
            if (v.value != null) {
                vs.add(v.value);
            }
        }
        return vs;
    }

    /**
     * 判断缓存值是否超时
     *
     * @param value 缓存值
     * @return 是否过期
     */
    private boolean isExpired(Value<?> value) {
        return value.getTimeout() != -1L
                && System.currentTimeMillis() - value.getBirthtime() > value.getTimeout();
    }

    @Data
    private static class Value<V> implements Serializable {

        // 缓存具体值
        private V value;

        // 存储时间
        private long birthtime;

        // 超时时间
        private long timeout;
    }

}
