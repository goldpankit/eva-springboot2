package com.eva.core.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.CacheException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义Shiro CacheManager
 */
@Slf4j
@Component
public class ShiroCacheManager extends AbstractCacheManager {

    private static ApplicationContext applicationContext;

    @Override
    protected ShiroCache createCache(String s) throws CacheException {
        return applicationContext.getBean(ShiroCache.class);
    }

    @Resource
    public void setApplicationContext (ApplicationContext applicationContext) {
        if (ShiroCacheManager.applicationContext == null) {
            ShiroCacheManager.applicationContext = applicationContext;
        }
    }
}
