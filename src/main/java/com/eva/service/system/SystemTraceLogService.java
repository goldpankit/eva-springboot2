package com.eva.service.system;

import com.eva.dao.system.SystemTraceLogMapper;
import com.eva.dao.system.model.SystemTraceLog;
import com.eva.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SystemTraceLogService extends BaseService<SystemTraceLog, SystemTraceLogMapper> {

    @Autowired
    public SystemTraceLogService(SystemTraceLogMapper systemTraceLogMapper, Environment environment) {
        super(systemTraceLogMapper, environment);
    }
}
