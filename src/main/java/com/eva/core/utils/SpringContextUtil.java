package com.eva.core.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Spring上下文工具
 */
@Component
public class SpringContextUtil {

    @Resource
    public ApplicationContext applicationContext;

    /**
     * 获取Bean实例
     *
     * @param name 类注册名称
     * @return Object
     */
    public Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 获取Bean实例
     *
     * @param clazz Class
     * @return T
     */
    public <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    /**
     * 获取Bean实例
     *
     * @param name 类注册名称
     * @param clazz Class
     * @return T
     */
    public <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 获取环境对象
     *
     * @return Environment
     */
    public Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }
}
