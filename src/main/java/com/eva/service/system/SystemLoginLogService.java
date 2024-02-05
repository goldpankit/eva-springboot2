package com.eva.service.system;

import com.eva.dao.system.SystemLoginLogMapper;
import com.eva.dao.system.model.SystemLoginLog;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SystemLoginLogService extends BaseService<SystemLoginLog, SystemLoginLogMapper> {

    public SystemLoginLogService(SystemLoginLogMapper mapper, Environment environment) {
        super(mapper, environment);
    }
}
