package com.eva.core.config.shiro;

import com.eva.core.model.LoginUserInfo;
import com.eva.core.utils.Utils;
import com.eva.service.system.*;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Shiro密码比对处理
 */
@Component
public class ShiroCredentialsMatcher extends HashedCredentialsMatcher {

    @Lazy
    @Resource
    private LoginUserService loginUserService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        LoginUserInfo loginUserInfo = (LoginUserInfo)info.getPrincipals().getPrimaryPrincipal();
        // 加密密码
        String pwd = Utils.Secure.encryptPassword(new String(usernamePasswordToken.getPassword()), loginUserInfo.getSalt());
        // 比较密码
        boolean result = this.equals(pwd, loginUserInfo.getPassword());
        if (!result) {
            return Boolean.FALSE;
        }
        // 补充用户信息
        loginUserService.padding(loginUserInfo);
        return Boolean.TRUE;
    }
}
