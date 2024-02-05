package com.eva.core.config.mybatis;

import com.baomidou.mybatisplus.annotation.TableField;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * MyBatis 拦截器，实现创建人、更信任、创建时间、更新时间的自动填充
 * 实现逻辑：当没有指定字段的值时，自动填充字段
 */
@Slf4j
@Component
@Intercepts({
    @Signature(type= Executor.class, method = "update", args={MappedStatement.class, Object.class})
})
public class MyBatisInterceptor implements Interceptor {

    private static final String[] CREATED_BY_LIST = new String[]{"createdBy"};

    private static final String[] CREATED_AT_LIST = new String[]{"createdAt"};

    private static final String[] UPDATED_BY_LIST = new String[]{"updatedBy"};

    private static final String[] UPDATED_AT_LIST = new String[]{"updatedAt"};

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object target = invocation.getArgs()[1];
        if (target == null) {
            return invocation.proceed();
        }
        if(target instanceof MapperMethod.ParamMap) {
            try {
                target = ((MapperMethod.ParamMap<?>) target).get("param1");
                if (target == null) {
                    return invocation.proceed();
                }
            } catch (Exception ignored) {
            }
        }
        // 创建语句
        if (SqlCommandType.INSERT == sqlCommandType) {
            this.handleOperaStatement(target, CREATED_BY_LIST, CREATED_AT_LIST);
        }
        // 更新语句
        else if (SqlCommandType.UPDATE == sqlCommandType) {
            this.handleOperaStatement(target, UPDATED_BY_LIST, UPDATED_AT_LIST);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    /**
     * 处理新增和编辑语句
     */
    private void handleOperaStatement(Object target, String[]... fieldNames) throws Exception{
        // 操作人
        for (String fieldName : fieldNames[0]) {
            Field operaUserField = ReflectionUtils.findField(target.getClass(), fieldName);
            if (operaUserField == null) {
                continue;
            }
            if (this.existsField(operaUserField)) {
                Object operaUser = this.getFieldValue(operaUserField, target);
                if (operaUser == null) {
                    this.setFieldValue(operaUserField, target, Utils.Session.getLoginUser().getId());
                }
            }
        }
        // 操作时间
        for (String fieldName : fieldNames[1]) {
            Field operaTimeField = ReflectionUtils.findField(target.getClass(), fieldName);
            if (operaTimeField == null) {
                continue;
            }
            if (this.existsField(operaTimeField)) {
                Object operaTime = this.getFieldValue(operaTimeField, target);
                if (operaTime == null) {
                    this.setFieldValue(operaTimeField, target, new Date());
                }
            }
        }
    }

    /**
     * 判断字段是否存在
     *
     * @param field 字段
     * @return boolean
     */
    private boolean existsField(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null && !tableField.exist()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 给属性赋值
     */
    private void setFieldValue(Field field, Object target, Object value) throws Exception {
        field.setAccessible(true);
        field.set(target, value);
        field.setAccessible(false);
    }

    /**
     * 获取属性值
     */
    private Object getFieldValue(Field field, Object target) throws Exception {
        field.setAccessible(true);
        Object value = field.get(target);
        field.setAccessible(false);
        return value;
    }
}
