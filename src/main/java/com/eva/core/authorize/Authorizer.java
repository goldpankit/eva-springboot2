package com.eva.core.authorize;

import com.eva.core.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 权限验证器
 */
@Component
public final class Authorizer {

    // 内置方法列表
    private final List<Method> innerMethods = new ArrayList<>(4);

    public Authorizer() {
        try {
            innerMethods.add(Authorizer.class.getMethod("hasRoles", String[].class));
            innerMethods.add(Authorizer.class.getMethod("hasPermissions", String[].class));
            innerMethods.add(Authorizer.class.getMethod("hasAnyRoles", String[].class));
            innerMethods.add(Authorizer.class.getMethod("hasAnyPermissions", String[].class));
            innerMethods.add(Authorizer.class.getMethod("isSuperAdmin"));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否为超级管理员
     *
     * @return boolean
     */
    public boolean isSuperAdmin () {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.hasRole(Utils.AppConfig.getSuperAdminRole());
    }

    /**
     * 判断是否包含所有角色
     *
     * @param roles 角色列表
     * @return boolean
     */
    public boolean hasRoles (String[] roles) {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.hasAllRoles(Arrays.asList(roles));
    }

    /**
     * 判断是否包含任意角色
     *
     * @param roles 角色列表
     * @return boolean
     */
    public boolean hasAnyRoles (String[] roles) {
        Subject currentUser = SecurityUtils.getSubject();
        boolean[] hasRoles = currentUser.hasRoles(Arrays.asList(roles));
        for (boolean hasRole : hasRoles) {
            if (hasRole) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 判断是否包含所有权限
     *
     * @param permissions 权限列表
     * @return boolean
     */
    public boolean hasPermissions (String[] permissions) {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isPermittedAll(permissions);
    }

    /**
     * 判断是否包含任意权限
     *
     * @param permissions 权限列表
     * @return boolean
     */
    public boolean hasAnyPermissions (String[] permissions) {
        Subject currentUser = SecurityUtils.getSubject();
        boolean[] hasPermissions = currentUser.isPermitted(permissions);
        for (boolean hasPermission : hasPermissions) {
            if (hasPermission) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    // 验证表达式是否成立
    public boolean checkExpress (String express) {
        // 表达式为空
        if (StringUtils.isBlank(express)) {
            return Boolean.FALSE;
        }
        express = express.trim();
        // 或者表达式
        String[] orExpresses = express.split("\\|\\|");
        // 单一表达式
        if (orExpresses.length == 1) {
            boolean authorized = this.checkSingleExpress(orExpresses[0].trim());
            if (authorized) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        // 处理或者表达式
        for (String orExp : orExpresses) {
            orExp = orExp.trim();
            // 并且表达式
            String[] andExpresses = orExp.split("&&");
            // 表达式只有一个，说明是单个表达式
            if (andExpresses.length == 1) {
                if (this.checkSingleExpress(orExp)) {
                    return Boolean.TRUE;
                }
            }
            // 处理并且表达式
            boolean authorized = true;
            for (String andExp : andExpresses) {
                andExp = andExp.trim();
                if (!this.checkSingleExpress(andExp)) {
                    authorized = false;
                    break;
                }
            }
            if (authorized) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 验证表达式
     *
     * @param express 表达式
     * @return boolean
     */
    private boolean checkSingleExpress (String express) {
        try {
            for (Method method: innerMethods) {
                if (express.startsWith(method.getName())) {
                    if ("isSuperAdmin".equals(method.getName())) {
                        return (boolean) method.invoke(this);
                    }
                    return (boolean) method.invoke(this, (Object) this.getArgs(express));
                }
            }
            throw new RuntimeException(express + "权限表达式不正确");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取表达式参数
     *
     * @param express 表达式
     * @return 参数数组
     */
    private String[] getArgs (String express) {
        String argString = express.substring(express.indexOf("(") + 1, express.indexOf(")"));
        String[] args = argString.split(",");
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
            args[i] = args[i].replaceAll("'", "");
        }
        return args;
    }
}
