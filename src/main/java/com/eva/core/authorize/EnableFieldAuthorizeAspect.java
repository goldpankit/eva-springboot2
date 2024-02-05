package com.eva.core.authorize;

import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.*;
import java.util.*;

/**
 * 字段授权切面
 * 复杂度：计查询数据为N，字段数为M，复杂度为O(NM)
 */
@Slf4j
@Aspect
@Component
public class EnableFieldAuthorizeAspect {

    @Resource
    private Authorizer authorizer;

    @Pointcut("@annotation(enableFieldAuthorize)")
    public void enableFieldAuthorizePointcut(EnableFieldAuthorize enableFieldAuthorize) {
    }

    @Around(value = "enableFieldAuthorizePointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint, EnableFieldAuthorize annotation) throws Throwable {
        // 参数权限处理
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            this.handleAuthorize(arg);
        }
        // 执行方法
        Object result = joinPoint.proceed();
        // 结果为null，直接返回
        if (result == null) {
            return null;
        }
        // 结果权限处理
        this.handleAuthorize(result);
        return result;
    }

    /**
     * 处理授权
     *
     * @param result 需处理授权的数据
     */
    private void handleAuthorize(Object result) throws Throwable {
        if (result == null) {
            return;
        }
        // 列出一些常见且不可能启用了字段权限验证的类，这些类直接不做处理，以加快验证的速度
        Class<?> clazz = result.getClass();
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
                || !clazz.getName().contains(".")
        ) {
            return;
        }
        // PageWrap对象，直接处理model，加快验证速度
        if (result instanceof PageWrap) {
            PageWrap<?> pageWrap = (PageWrap<?>) result;
            this.handleAuthorize(pageWrap.getModel());
            return;
        }
        // PageData对象，直接处理records，加快验证速度
        if (result instanceof PageData) {
            PageData<?> pageData = (PageData<?>) result;
            if (CollectionUtils.isEmpty(pageData.getRecords())) {
                return;
            }
            this.handleAuthorize(pageData.getRecords());
            return;
        }
        // ApiResponse对象，直接处理data，加快验证速度
        if (result instanceof ApiResponse) {
            ApiResponse<?> response = (ApiResponse<?>) result;
            if (response.getData() == null) {
                return;
            }
            this.handleAuthorize(response.getData());
            return;
        }
        // Collection类型
        if (result instanceof Collection) {
            Collection<?> collection = (Collection<?>) result;
            if (CollectionUtils.isEmpty(collection)) {
                return;
            }
            for (Object value : collection) {
                this.handleAuthorize(value);
            }
        }
        // 未开启字段权限验证，不做处理
        if (clazz.getAnnotation(EnableFieldAuthorize.class) == null) {
            return;
        }
        // 开启了字段权限验证的类，处理字段权限
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 字段存在授权注解
            if (this.isAuthorizeField(field)) {
                log.debug("处理字段权限: {}-{}", clazz.getName(), field.getName());
                AuthorizeDispatch dispatch = new AuthorizeDispatch(authorizer, field);
                boolean unauthorized = dispatch.handle(result);
                // 如果字段没有权限，则值已被置空，直接处理下一个字段
                if (unauthorized) {
                    continue;
                }
            }
            /*
             获取值后重新处理授权
             因为验证的类中不确定性太多，可能存在泛型情况，无法简单的通过field.getType()准确的获取类型，
             从而无法确定是否需要进一步处理权限。此处视为每个字段都需要进行权限处理。
             */
            this.handleAuthorize(this.getFieldValue(field, result));
        }
    }

    /**
     * 判断是否为授权字段
     *
     * @param field 字段对象
     * @return boolean
     */
    private boolean isAuthorizeField (Field field) {
        ContainRoles containRoles = field.getAnnotation(ContainRoles.class);
        ContainAnyRoles containAnyRoles = field.getAnnotation(ContainAnyRoles.class);
        ContainPermissions containPermissions = field.getAnnotation(ContainPermissions.class);
        ContainAnyPermissions containAnyPermissions = field.getAnnotation(ContainAnyPermissions.class);
        AuthorizeExpress authorizeExpress = field.getAnnotation(AuthorizeExpress.class);
        boolean hasAuthorize = containRoles != null
                || containAnyRoles != null
                || containPermissions != null
                || containAnyPermissions != null
                || authorizeExpress != null;
        if (hasAuthorize) {
            if (Map.class.isAssignableFrom(field.getType())) {
                throw new RuntimeException("在字段权限控制中，不支持Map类型的字段权限控制：" + field.getName());
            }
            if (Collection.class.isAssignableFrom(field.getType())
                    && !field.getType().equals(Collection.class)
                    && !field.getType().equals(List.class)
                    && !field.getType().equals(Set.class)
            ) {
                throw new RuntimeException("在字段权限控制中，集合字段请使用List、Set或Collection定义：" + field.getName());
            }
        }
        return hasAuthorize;
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

    @Data
    private static class AuthorizeDispatch {

        private Authorizer authorizer;

        private Field field;

        public AuthorizeDispatch (Authorizer authorizer, Field field) {
            this.authorizer = authorizer;
            this.field = field;
        }

        public boolean handle (Object result) throws Exception {
            return this.handleAuthorizeExpress(field.getAnnotation(AuthorizeExpress.class), field, result)
                    || this.handleContainPermissions(field.getAnnotation(ContainPermissions.class), field, result)
                    || this.handleContainAnyPermissions(field.getAnnotation(ContainAnyPermissions.class), field, result)
                    || this.handleContainRoles(field.getAnnotation(ContainRoles.class), field, result)
                    || this.handleContainAnyRoles(field.getAnnotation(ContainAnyRoles.class), field, result);
        }

        private boolean handleContainRoles (ContainRoles annotation, Field field, Object result) throws Exception {
            if (annotation == null) {
                return false;
            }
            boolean authorized = authorizer.hasRoles(annotation.value());
            if (!authorized) {
                this.cleanField(field, result);
                return true;
            }
            return false;
        }

        private boolean handleContainAnyRoles (ContainAnyRoles annotation, Field field, Object result) throws Exception {
            if (annotation == null) {
                return false;
            }
            boolean authorized = authorizer.hasAnyRoles(annotation.value());
            if (!authorized) {
                this.cleanField(field, result);
                return true;
            }
            return false;
        }

        private boolean handleContainPermissions (ContainPermissions annotation, Field field, Object result) throws Exception {
            if (annotation == null) {
                return false;
            }
            boolean authorized = authorizer.hasPermissions(annotation.value());
            if (!authorized) {
                this.cleanField(field, result);
                return true;
            }
            return false;
        }

        private boolean handleContainAnyPermissions (ContainAnyPermissions annotation, Field field, Object result) throws Exception {
            if (annotation == null) {
                return false;
            }
            boolean authorized = authorizer.hasAnyPermissions(annotation.value());
            if (!authorized) {
                this.cleanField(field, result);
                return true;
            }
            return false;
        }

        private boolean handleAuthorizeExpress (AuthorizeExpress annotation, Field field, Object result) throws Exception {
            if (annotation == null) {
                return false;
            }
            boolean authorized = authorizer.checkExpress(annotation.value());
            if (!authorized) {
                this.cleanField(field, result);
                return true;
            }
            return false;
        }

        /**
         * 清理字段数据
         *
         * @param field 字段
         * @param result 结果
         */
        private void cleanField (Field field, Object result) throws Exception {
            field.setAccessible(true);
            // 此处要求定义字段啊必须采用List、Set、Collection，不可使用具体实现类，否则会抛出异常
            field.set(result, this.getEmptyValue(field));
        }

        /**
         * 获取字段空值
         *
         * @param field 字段
         * @return 空值
         */
        private Object getEmptyValue(Field field) {
            if (String.class.equals(field.getType())) {
                return "";
            }
            if (CharSequence.class.equals(field.getType())) {
                return "";
            }
            if (Map.class.isAssignableFrom(field.getType())) {
                return Collections.emptyMap();
            }
            if (Set.class.isAssignableFrom(field.getType())) {
                return Collections.emptySet();
            }
            if (Collection.class.isAssignableFrom(field.getType())) {
                return Collections.emptyList();
            }
            return null;
        }
    }

}
