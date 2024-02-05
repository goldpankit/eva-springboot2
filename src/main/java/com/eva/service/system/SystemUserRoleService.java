package com.eva.service.system;

import com.eva.dao.system.SystemUserRoleMapper;
import com.eva.dao.system.model.SystemUserRole;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SystemUserRoleService extends BaseService<SystemUserRole, SystemUserRoleMapper> {

    public SystemUserRoleService(SystemUserRoleMapper mapper, Environment environment) {
        super(mapper, environment);
    }

}
