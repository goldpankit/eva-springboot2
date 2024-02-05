package com.eva.service.system;

import com.eva.dao.system.SystemUserMapper;
import com.eva.dao.system.model.SystemUser;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SystemUserService extends BaseService<SystemUser, SystemUserMapper> {

    public SystemUserService(SystemUserMapper systemUserMapper, Environment environment) {
        super(systemUserMapper, environment);
    }
}
