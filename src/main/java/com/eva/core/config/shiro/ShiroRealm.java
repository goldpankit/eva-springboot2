package com.eva.core.config.shiro;

import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.model.LoginUserInfo;
import com.eva.dao.system.model.SystemUser;
import com.eva.service.system.SystemUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义Realm，处理认证和权限
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Lazy
    @Resource
    private SystemUserService systemUserService;

    /**
     * 权限处理
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LoginUserInfo loginUserInfo = (LoginUserInfo)principalCollection.getPrimaryPrincipal();
        // 设置用户角色和权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(loginUserInfo.getRoles());
        authorizationInfo.addStringPermissions(loginUserInfo.getPermissions());
        return authorizationInfo;
    }

    /**
     * 认证处理
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取用户名
        String username = authenticationToken.getPrincipal().toString();
        // 根据用户名查询用户对象
        SystemUser queryDto = new SystemUser();
        queryDto.setUsername(username);
        SystemUser user = systemUserService.findOne(queryDto);
        if (user == null) {
            throw new BusinessException(ResponseStatus.ACCOUNT_INCORRECT);
        }
        // 验证用户
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        BeanUtils.copyProperties(user, loginUserInfo);
        return new SimpleAuthenticationInfo(loginUserInfo, user.getPassword(), this.getName());
    }

}
