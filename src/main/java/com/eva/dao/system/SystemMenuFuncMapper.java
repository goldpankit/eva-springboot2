package com.eva.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eva.dao.system.dto.QuerySystemMenuFuncDTO;
import com.eva.dao.system.model.SystemMenuFunc;
import com.eva.dao.system.vo.SystemMenuFuncVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SystemMenuFuncMapper extends BaseMapper<SystemMenuFunc> {

    /**
     * 查询菜单功能
     *
     * @param dto QuerySystemMenuFuncDTO
     * @return List<SystemMenuFuncVO>
     */
    List<SystemMenuFuncVO> search(QuerySystemMenuFuncDTO dto);

}
