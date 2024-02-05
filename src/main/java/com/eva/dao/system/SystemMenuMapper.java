package com.eva.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eva.dao.system.dto.QuerySystemMenuDTO;
import com.eva.dao.system.model.SystemMenu;
import com.eva.dao.system.vo.SystemMenuVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {

    /**
     * 查询菜单管理列表
     *
     * @param dto 查询参数
     * @return 菜单列表
     */
    List<SystemMenuVO> search(QuerySystemMenuDTO dto);

    /**
     * 根据用户ID查询
     *
     * @param dto 查询参数
     * @return 用户菜单列表
     */
    List<SystemMenuVO> selectUserMenus(QuerySystemMenuDTO dto);

}
