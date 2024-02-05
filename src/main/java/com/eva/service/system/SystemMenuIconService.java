package com.eva.service.system;

import com.eva.dao.system.SystemMenuIconMapper;
import com.eva.dao.system.model.SystemMenuIcon;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SystemMenuIconService extends BaseService<SystemMenuIcon, SystemMenuIconMapper> {

    public SystemMenuIconService(SystemMenuIconMapper mapper, Environment environment) {
        super(mapper, environment);
    }
}
