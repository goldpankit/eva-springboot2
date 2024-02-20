package com.eva.core.config.shiro;

import com.eva.core.model.LoginUserInfo;
import com.eva.core.utils.Utils;
import com.eva.dao.system.model.*;
import com.eva.service.system.*;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shiro密码比对处理
 */
@Component
public class ShiroCredentialsMatcher extends HashedCredentialsMatcher {

    @Lazy
    @Resource
    private SystemRoleService systemRoleService;

    @Lazy
    @Resource
    private SystemPermissionService systemPermissionService;

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
        List<SystemRole> roles = systemRoleService.findByUserId(loginUserInfo.getId());
        // 设置角色
        List<Integer> roleIds = new ArrayList<>();
        List<String> roleCodes = new ArrayList<>();
        for (SystemRole role : roles) {
            roleIds.add(role.getId());
            roleCodes.add(role.getCode());
        }
        loginUserInfo.setRoles(roleCodes);
        // 设置权限
        List<SystemPermission> permissions = systemPermissionService.findByUserId(loginUserInfo.getId());
        List<String> permissionCodes = new ArrayList<>();
        for (SystemPermission permission : permissions) {
            permissionCodes.add(permission.getCode());
        }
        loginUserInfo.setPermissions(permissionCodes);
        loginUserInfo.setIsSuperAdmin(roleCodes.contains(Utils.AppConfig.getSuperAdminRole()));
        // 设置菜单权限
        loginUserInfo.setMenuPermissionIds(Collections.emptySet());
        if (!roleIds.isEmpty()) {
            loginUserInfo.setMenuPermissionIds(systemPermissionService.findMenuPermissionIdsByRoleIds(roleIds));
        }
        // 设置功能权限
        loginUserInfo.setMenuFuncPermissionIds(Collections.emptySet());
        if (!roleIds.isEmpty()) {
            loginUserInfo.setMenuFuncPermissionIds(systemPermissionService.findMenuFuncPermissionIdsByRoleIds(roleIds));
        }
        // 设置系统配置权限
        loginUserInfo.setSystemConfigPermissionIds(Collections.emptySet());
        if (!roleIds.isEmpty()) {
            loginUserInfo.setSystemConfigPermissionIds(systemPermissionService.findSystemConfigPermissionIdsByRoleIds(roleIds));
        }
        return Boolean.TRUE;
    }
}
