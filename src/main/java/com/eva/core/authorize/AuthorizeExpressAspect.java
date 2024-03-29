package com.eva.core.authorize;

import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 权限表达式校验切面
 */
@Aspect
@Component
public class AuthorizeExpressAspect {

    @Resource
    private Authorizer authorizer;

    @Pointcut("@annotation(authorizeExpress)")
    public void authorizeExpressPointcut(AuthorizeExpress authorizeExpress) {
    }

    @Around(value = "authorizeExpressPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint, AuthorizeExpress annotation) throws Throwable {
        String express = annotation.value();
        if (!authorizer.checkExpress(express)) {
            throw new UnauthorizedException();
        }
        return joinPoint.proceed();
    }
}
