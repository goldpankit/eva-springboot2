package com.eva.core.utils;

import java.lang.reflect.Field;

/**
 * Mybatis Plus 工具类
 */
public class MyBatisPlus {

    /**
     * 将空转为null，用于mybatis plus查询数据时将值为""的字段转为null，防止将空字符串作为条件。
     */
    public <T> void blankToNull(T object) {
        if (object == null) {
            return;
        }
        try {
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(object);
                if ("".equals(value)) {
                    field.set(object, null);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
