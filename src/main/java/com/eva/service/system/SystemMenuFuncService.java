package com.eva.service.system;

import com.eva.dao.system.SystemMenuFuncMapper;
import com.eva.dao.system.model.SystemMenuFunc;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SystemMenuFuncService extends BaseService<SystemMenuFunc, SystemMenuFuncMapper> {

    public SystemMenuFuncService(SystemMenuFuncMapper mapper, Environment environment) {
        super(mapper, environment);
    }
}
