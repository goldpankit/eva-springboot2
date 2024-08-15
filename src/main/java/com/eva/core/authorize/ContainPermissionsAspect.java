package com.eva.core.authorize;

import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 权限检查切面
 */
@Aspect
@Component
public class ContainPermissionsAspect {

    @Resource
    private Authorizer authorizer;

    @Pointcut("@annotation(containPermissions)")
    public void containPermissionsInMethodsPointcut(ContainPermissions containPermissions) {
    }

    @Pointcut("@within(containPermissions)")
    public void containPermissionsInClassPointcut(ContainPermissions containPermissions) {
    }

    @Pointcut("@annotation(containAnyPermissions)")
    public void containAnyPermissionsInMethodsPointcut(ContainAnyPermissions containAnyPermissions) {
    }

    @Pointcut("@within(containAnyPermissions)")
    public void containAnyPermissionsInClassPointcut(ContainAnyPermissions containAnyPermissions) {
    }

    @Around(value = "containPermissionsInMethodsPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkMethodsAuthorization(ProceedingJoinPoint joinPoint, ContainPermissions annotation) throws Throwable {
        boolean authorized = authorizer.hasPermissions(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

    @Around(value = "containPermissionsInClassPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkClassAuthorization(ProceedingJoinPoint joinPoint, ContainPermissions annotation) throws Throwable {
        boolean authorized = authorizer.hasPermissions(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

    @Around(value = "containAnyPermissionsInMethodsPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkMethodsAuthorization(ProceedingJoinPoint joinPoint, ContainAnyPermissions annotation) throws Throwable {
        boolean authorized = authorizer.hasAnyPermissions(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

    @Around(value = "containAnyPermissionsInClassPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkClassAuthorization(ProceedingJoinPoint joinPoint, ContainAnyPermissions annotation) throws Throwable {
        boolean authorized = authorizer.hasPermissions(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

}
