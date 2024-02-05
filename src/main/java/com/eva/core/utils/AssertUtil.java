package com.eva.core.utils;

import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 断言类
 * 作用：提供项目当中的断言方法
 * 目的：统一管理断言方法，避免在各个地方进行不规范或引入各种第三方库的情况
 */
public final class AssertUtil {

    /**
     * 验证非null
     *
     * @param object 验证参数
     * @param message 错误消息
     */
    public static void notNull (Object object, String message) {
        AssertUtil.notNull(object, ResponseStatus.BAD_REQUEST, message);
    }

    /**
     * 验证非null
     *
     * @param object 验证参数
     * @param status 响应状态
     */
    public static void notNull (Object object, ResponseStatus status) {
        AssertUtil.notNull(object, status, status.getMessage());
    }

    /**
     * 验证非null
     *
     * @param object 验证参数
     * @param status 响应状态
     * @param message 错误消息
     */
    public static void notNull (Object object, ResponseStatus status, String message) {
        if (object == null) {
            throw new BusinessException(status, message);
        }
    }

    /**
     * 验证非空
     *
     * @param object 验证参数
     * @param message 错误消息
     */
    public static void notEmpty (Object object, String message) {
        AssertUtil.notEmpty(object, ResponseStatus.BAD_REQUEST, message);
    }

    /**
     * 验证非空
     *
     * @param object 验证参数
     * @param status 响应状态
     */
    public static void notEmpty (Object object, ResponseStatus status) {
        AssertUtil.notEmpty(object, status, status.getMessage());
    }

    /**
     * 验证非空
     *
     * @param object 验证参数
     * @param status 响应状态
     * @param message 错误消息
     */
    public static void notEmpty (Object object, ResponseStatus status, String message) {
        if (object == null) {
            throw new BusinessException(status, message);
        }
        // 字符串
        if (object instanceof String) {
            if (StringUtils.isBlank(((String) object))) {
                throw new BusinessException(status, message);
            }
        }
        // 集合
        if (object instanceof Collection) {
            if (((Collection<?>) object).isEmpty()) {
                throw new BusinessException(status, message);
            }
        }
        // 数组
        if (object instanceof Object[]) {
            if (((Object[]) object).length == 0) {
                throw new BusinessException(status, message);
            }
        }
        // Map
        if (object instanceof Map) {
            if (((Map<?,?>) object).isEmpty()) {
                throw new BusinessException(status, message);
            }
        }
    }

    /**
     * 验证两个对象是否相等
     *
     * @param object1 对象1
     * @param object2 对象2
     * @param message 错误消息
     */
    public static void notEquals (Object object1, Object object2, String message) {
        AssertUtil.notEquals(object1, object2, ResponseStatus.BAD_REQUEST, message);
    }

    /**
     * 验证两个对象是否相等
     *
     * @param object1 对象1
     * @param object2 对象2
     * @param status 响应状态
     */
    public static void notEquals (Object object1, Object object2, ResponseStatus status) {
        AssertUtil.notEquals(object1, object2, status, status.getMessage());
    }

    /**
     * 验证两个对象是否相等
     *
     * @param object1 对象1
     * @param object2 对象2
     * @param status 响应状态
     * @param message 错误消息
     */
    public static void notEquals (Object object1, Object object2, ResponseStatus status, String message) {
        if (Objects.equals(object1, object2)) {
            throw new BusinessException(status, message);
        }
    }

    /**
     * 判断是否为一个正确的网址
     *
     * @param url 网址
     */
    public static void isURL(String url, String message) {
        if (!url.matches("^(http|https)://.+$")) {
            throw new BusinessException(ResponseStatus.BAD_REQUEST, message);
        }
    }

    /**
     * 判断数值是否在范围内
     * @param value 目标数值
     * @param min 最小值
     * @param max 最大值
     * @param message 错误消息
     */
    public static void inRange (int value, int min, int max, String message) {
        if (value < min || value > max) {
            throw new BusinessException(ResponseStatus.BAD_REQUEST, message);
        }
    }

    /**
     * 验证手机号码是否正确
     * @param phoneNumber 手机号码
     */
    public static void isPhoneNumber (String phoneNumber) {
        // 使用正则表达式来验证
        if (!phoneNumber.matches("^1[3|4|5|6|7|8|9][0-9]\\d{8}$")) {
            throw new BusinessException(ResponseStatus.BAD_REQUEST, "手机号码格式不正确");
        }
    }

    /**
     * 验证邮箱是否正确
     * @param email 邮箱
     */
    public static void isEmail (String email) {
        if (!email.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")) {
            throw new BusinessException(ResponseStatus.BAD_REQUEST, "邮箱格式不正确");
        }
    }

    /**
     * 验证身份证号码是否正确
     * @param idCardNumber 身份证号码
     */
    public static void isIdCardNumber (String idCardNumber) {
        if (!idCardNumber.matches("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")) {
            throw new BusinessException(ResponseStatus.BAD_REQUEST, "身份证号码格式不正确");
        }
    }

}
