package com.eva.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eva.dao.system.dto.QuerySystemDictDataDTO;
import com.eva.dao.system.model.SystemDictData;
import com.eva.dao.system.vo.SystemDictDataVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SystemDictDataMapper extends BaseMapper<SystemDictData> {

    /**
     * 查询字典数据管理列表
     *
     * @param dto 详见QuerySystemDictDataDTO
     * @return List<SystemDictDataListVO>
     */
    List<SystemDictDataVO> search(QuerySystemDictDataDTO dto);

}
