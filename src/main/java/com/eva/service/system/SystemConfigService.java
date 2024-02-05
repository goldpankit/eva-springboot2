package com.eva.service.system;

import com.eva.dao.system.SystemConfigMapper;
import com.eva.dao.system.model.SystemConfig;
import com.eva.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigService extends BaseService<SystemConfig, SystemConfigMapper> {

    @Autowired
    public SystemConfigService(SystemConfigMapper systemConfigMapper, Environment environment) {
        super(systemConfigMapper, environment);
    }
}
