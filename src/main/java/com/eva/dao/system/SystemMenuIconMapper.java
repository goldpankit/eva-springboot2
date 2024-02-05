package com.eva.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eva.dao.system.dto.QuerySystemMenuIconDTO;
import com.eva.dao.system.model.SystemMenuIcon;
import com.eva.dao.system.vo.SystemMenuIconVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SystemMenuIconMapper extends BaseMapper<SystemMenuIcon> {

    /**
     * 查询系统菜单图标
     *
     * @param dto QuerySystemMenuIconDTO
     * @return List<SystemMenuIconVO>
     */
    List<SystemMenuIconVO> search(QuerySystemMenuIconDTO dto);

}
