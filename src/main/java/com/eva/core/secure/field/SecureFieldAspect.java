package com.eva.core.secure.field;

import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 安全字段切面
 * 复杂度：计查询数据为N，字段数为M，复杂度为O(NM)
 */
@Slf4j
@Aspect
@Component
public class SecureFieldAspect {

    @Pointcut("@annotation(enableSecureField)")
    public void enableSecureFieldPointcut(EnableSecureField enableSecureField) {
    }

    @Around(value = "enableSecureFieldPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object handleSecureField(ProceedingJoinPoint joinPoint, EnableSecureField annotation) throws Throwable {
        // 参数处理，将安全字段进行加密
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            this.handleSecureField(arg, Boolean.TRUE);
        }
        // 执行方法
        Object result = joinPoint.proceed();
        // 结果为null，直接返回
        if (result == null) {
            return null;
        }
        // 结果处理，将安全字段进行解密
        this.handleSecureField(result, Boolean.FALSE);
        return result;
    }

    /**
     * 处理安全字段
     *
     * @param data 需处理安全字段的数据
     * @param isEncrypt 是否为加密处理
     */
    private void handleSecureField(Object data, boolean isEncrypt) throws Throwable {
        if (data == null) {
            return;
        }
        // 列出一些常见且不可能启用了安全字段的类，这些类直接不做处理，以加快处理的速度
        Class<?> clazz = data.getClass();
        if (Byte.class.equals(clazz)
                || Boolean.class.equals(clazz)
                || Character.class.equals(clazz)
                || Short.class.equals(clazz)
                || Float.class.equals(clazz)
                || Integer.class.equals(clazz)
                || Double.class.equals(clazz)
                || Long.class.equals(clazz)
                || String.class.equals(clazz)
                || Object.class.equals(clazz)
                || Date.class.equals(clazz)
                || Map.class.isAssignableFrom(clazz)
                // 排除基本类型
                || !clazz.getName().contains(".")
        ) {
            return;
        }
        // PageWrap对象，直接处理model，加快处理速度
        if (data instanceof PageWrap) {
            PageWrap<?> pageWrap = (PageWrap<?>) data;
            this.handleSecureField(pageWrap.getModel(), isEncrypt);
            return;
        }
        // PageData对象，直接处理records，加快处理速度
        if (data instanceof PageData) {
            PageData<?> pageData = (PageData<?>) data;
            if (CollectionUtils.isEmpty(pageData.getRecords())) {
                return;
            }
            this.handleSecureField(pageData.getRecords(), isEncrypt);
            return;
        }
        // ApiResponse对象，直接处理data，加快处理速度
        if (data instanceof ApiResponse) {
            ApiResponse<?> response = (ApiResponse<?>) data;
            if (response.getData() == null) {
                return;
            }
            this.handleSecureField(response.getData(), isEncrypt);
            return;
        }
        // Collection类型
        if (data instanceof Collection) {
            Collection<?> collection = (Collection<?>) data;
            if (CollectionUtils.isEmpty(collection)) {
                return;
            }
            for (Object value : collection) {
                this.handleSecureField(value, isEncrypt);
            }
        }
        // 未开启安全字段，不做处理
        if (AnnotationUtils.findAnnotation(clazz, EnableSecureField.class) == null) {
            return;
        }
        // 开启了安全字段的类，处理安全字段
        Field[] fields = FieldUtils.getAllFields(clazz);
        for (Field field : fields) {
            // 字段存在安全字段注解
            if (this.isSecureField(field)) {
                log.debug("处理安全字段: {}-{}", clazz.getName(), field.getName());
                String value = (String) this.getFieldValue(field, data);
                // 字段值为空，不做加解密处理
                if (StringUtils.isBlank(value)) {
                    continue;
                }
                if (isEncrypt) {
                    field.set(data, Utils.Secure.encryptField((String) this.getFieldValue(field, data)));
                } else {
                    field.set(data, Utils.Secure.decryptField((String) this.getFieldValue(field, data)));
                }
            }
            /*
             获取值后重新处理授权
             因为验证的类中不确定性太多，可能存在泛型情况，无法简单的通过field.getType()准确的获取类型，
             从而无法确定是否需要进一步处理权限。此处视为每个字段都需要进行权限处理。
             */
            this.handleSecureField(this.getFieldValue(field, data), isEncrypt);
        }
    }

    /**
     * 判断是否为授权字段
     *
     * @param field 字段对象
     * @return boolean
     */
    private boolean isSecureField(Field field) {
        SecureField secureField = field.getAnnotation(SecureField.class);
        boolean hasAnnotation = secureField != null;
        if (hasAnnotation) {
            if (!String.class.equals(field.getType())) {
                throw new RuntimeException("在安全字段只能使用字符串类型定义：" + field.getName());
            }
        }
        return hasAnnotation;
    }

    /**
     * 获取字段值
     * 只有在字段类型为Collection或标注了EnableFieldAuthorize注解的类时，才会调用此方法
     *
     * @param field 字段
     * @param result 结果（可能是List或单独的对象）
     * @return 字段值
     */
    private Object getFieldValue(Field field, Object result) throws IllegalAccessException{
        field.setAccessible(true);
        return field.get(result);
    }

}
