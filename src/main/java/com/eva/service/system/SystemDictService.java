package com.eva.service.system;

import com.eva.dao.system.SystemDictMapper;
import com.eva.dao.system.model.SystemDict;
import com.eva.dao.system.vo.SystemDictWithDataVO;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemDictService extends BaseService<SystemDict, SystemDictMapper> {

    public SystemDictService(SystemDictMapper mapper, Environment environment) {
        super(mapper, environment);
    }

    /**
     * 查询所有字典和数据
     *
     * @return List<SystemDictWithDataVO>
     */
    public List<SystemDictWithDataVO> findListWithData () {
        return mapper.selectListWithData();
    }
}
