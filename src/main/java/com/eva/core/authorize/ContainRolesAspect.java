package com.eva.core.authorize;

import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 角色检查切面
 */
@Aspect
@Component
public class ContainRolesAspect {

    @Resource
    private Authorizer authorizer;

    @Pointcut("@annotation(containRoles)")
    public void containRolesPointcut(ContainRoles containRoles) {
    }

    @Pointcut("@annotation(containAnyRoles)")
    public void containAnyRolesPointcut(ContainAnyRoles containAnyRoles) {
    }

    @Around(value = "containRolesPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint, ContainRoles annotation) throws Throwable {
        boolean authorized = authorizer.hasRoles(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

    @Around(value = "containAnyRolesPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint, ContainAnyRoles annotation) throws Throwable {
        boolean authorized = authorizer.hasAnyRoles(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

}
