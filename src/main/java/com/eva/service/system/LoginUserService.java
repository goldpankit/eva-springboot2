package com.eva.service.system;

import com.eva.core.model.LoginUserInfo;
import com.eva.core.utils.Utils;
import com.eva.dao.system.model.SystemPermission;
import com.eva.dao.system.model.SystemRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 登录用户服务
 */
@Service
public class LoginUserService {

    @Resource
    private SystemRoleService systemRoleService;

    @Resource
    private SystemPermissionService systemPermissionService;

    /**
     * 补充登录用户信息
     *
     * @param loginUserInfo 登录信息
     */
    public void padding(LoginUserInfo loginUserInfo) {
        List<SystemRole> roles = systemRoleService.findByUserId(loginUserInfo.getId());
        // 设置角色
        Set<Integer> roleIds = new HashSet<>();
        Set<String> roleCodes = new HashSet<>();
        for (SystemRole role : roles) {
            roleIds.add(role.getId());
            roleCodes.add(role.getCode());
        }
        loginUserInfo.setRoles(roleCodes);
        // 设置权限
        List<SystemPermission> permissions = systemPermissionService.findByUserId(loginUserInfo.getId());
        Set<String> permissionCodes = new HashSet<>();
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
    }

    /* @kit position line */
}
