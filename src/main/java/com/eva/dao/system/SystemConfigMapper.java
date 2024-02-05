package com.eva.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eva.dao.system.dto.QuerySystemConfigDTO;
import com.eva.dao.system.model.SystemConfig;
import com.eva.dao.system.vo.SystemConfigVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

    /**
     * 查询系统配置
     *
     * @param dto QuerySystemConfigDTO
     * @return List<SystemConfigVO>
     */
    List<SystemConfigVO> search(QuerySystemConfigDTO dto);

}
