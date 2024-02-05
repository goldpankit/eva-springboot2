package com.eva.core.secure.twofa;

import com.eva.core.constants.Constants;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 2FA切面，实现请求的2FA认证
 * 实现逻辑：
 *   1. 从请求头中获取密码，如果不存在密码头，则抛出TWO_FA_REQUIRED
 *   2. 从请求头中获取哦密码，与当前登录用户的密码进行对比，如果密码不匹配，则抛出TWO_FA_FAILED
 * 注意：
 * 这里的密码验证并不安全，因为密码会在请求头中暴露，社工人员可通过查看请求头来直接获取密码，
 * 如果系统对安全性要求高，2FA建议调整为邮件验证码或短信验证码验证等。取决于对系统安全性的要求情况。
 */
@Slf4j
@Aspect
@Component
public class TwoFAAspect {

    @Pointcut("@annotation(enableTwoFA)")
    public void enableTwoFaPointcut(EnableTwoFA enableTwoFA) {
    }

    @Around(value = "enableTwoFaPointcut(annotation)", argNames = "joinPoint,annotation")
    public Object handleTwoFA(ProceedingJoinPoint joinPoint, EnableTwoFA annotation) throws Throwable {
        // 获取Request参数对象
        HttpServletRequest request = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
                break;
            }
        }
        if (request == null) {
            throw new BusinessException(ResponseStatus.TWO_FA_FAILED);
        }
        // 获取密码
        String password = request.getHeader(Constants.HEADER_2FA_PASSWORD);
        if (StringUtils.isBlank(password)) {
            throw new BusinessException(ResponseStatus.TWO_FA_REQUIRED);
        }
        password = Utils.Secure.decryptTransmission(password);
        password = Utils.Secure.encryptPassword(
                password,
                Utils.Session.getLoginUser().getSalt()
        );
        // 比对密码
        if (!password.equals(Utils.Session.getLoginUser().getPassword())) {
            throw new BusinessException(ResponseStatus.TWO_FA_FAILED);
        }
        return joinPoint.proceed();
    }

}
