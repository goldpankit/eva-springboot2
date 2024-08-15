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
    public void containRolesInMethodsPointcut(ContainRoles containRoles) {
    }

    @Pointcut("@within(containRoles)")
    public void containRolesInClassPointcut(ContainRoles containRoles) {
    }

    @Pointcut("@annotation(containAnyRoles)")
    public void containAnyRolesInMethodsPointcut(ContainAnyRoles containAnyRoles) {
    }

    @Pointcut("@within(containAnyRoles)")
    public void containAnyRolesInClassPointcut(ContainAnyRoles containAnyRoles) {
    }

    @Around(value = "containRolesInMethodsPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkMethodsAuthorization(ProceedingJoinPoint joinPoint, ContainRoles annotation) throws Throwable {
        boolean authorized = authorizer.hasRoles(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

    @Around(value = "containRolesInClassPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkClassAuthorization(ProceedingJoinPoint joinPoint, ContainRoles annotation) throws Throwable {
        boolean authorized = authorizer.hasRoles(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

    @Around(value = "containAnyRolesInMethodsPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkMethodsAuthorization(ProceedingJoinPoint joinPoint, ContainAnyRoles annotation) throws Throwable {
        boolean authorized = authorizer.hasAnyRoles(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

    @Around(value = "containAnyRolesInClassPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkClassAuthorization(ProceedingJoinPoint joinPoint, ContainAnyRoles annotation) throws Throwable {
        boolean authorized = authorizer.hasAnyRoles(annotation.value());
        if (!authorized) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }

}
