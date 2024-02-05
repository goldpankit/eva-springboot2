package com.eva.service.system;

import com.eva.dao.system.SystemRoleMapper;
import com.eva.dao.system.model.SystemRole;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SystemRoleService extends BaseService<SystemRole, SystemRoleMapper> {

    public SystemRoleService(SystemRoleMapper systemRoleMapper, Environment environment) {
        super(systemRoleMapper, environment);
    }

    /**
     * 查询用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SystemRole> findByUserId(Integer userId) {
        return mapper.selectByUserId(userId);
    }

}
