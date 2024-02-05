package com.eva.service.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eva.dao.system.SystemRolePermissionMapper;
import com.eva.dao.system.model.SystemRolePermission;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Service
public class SystemRolePermissionService extends BaseService<SystemRolePermission, SystemRolePermissionMapper> {

    public SystemRolePermissionService(SystemRolePermissionMapper mapper, Environment environment) {
        super(mapper, environment);
    }

    @Transactional
    public void deleteByPermissionIds (Integer roleId, Set<Integer> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        QueryWrapper<SystemRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SystemRolePermission::getRoleId, roleId)
                .in(SystemRolePermission::getPermissionId, permissionIds);
        this.delete(queryWrapper);
    }
}
