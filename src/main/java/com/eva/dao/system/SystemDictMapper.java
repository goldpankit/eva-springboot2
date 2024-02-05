package com.eva.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eva.dao.system.dto.QuerySystemDictDTO;
import com.eva.dao.system.model.SystemDict;
import com.eva.dao.system.vo.SystemDictVO;
import com.eva.dao.system.vo.SystemDictWithDataVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SystemDictMapper extends BaseMapper<SystemDict> {

    /**
     * 查询字典管理列表
     *
     * @param dto 详见QuerySystemDictDTO
     * @param orderByClause 排序语句
     * @return List<SystemDictListVO>
     */
    List<SystemDictVO> search(@Param("dto") QuerySystemDictDTO dto, @Param("orderByClause") String orderByClause);

    /**
     * 查询所有字典和数据
     *
     * @return List<SystemDictWithDataVO>
     */
    List<SystemDictWithDataVO> selectListWithData();
}
