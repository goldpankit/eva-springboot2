package com.eva.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eva.dao.system.dto.QuerySystemRoleDTO;
import com.eva.dao.system.model.SystemRole;
import com.eva.dao.system.vo.SystemRoleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SystemRoleMapper extends BaseMapper<SystemRole> {

    /**
     * 查询角色管理列表
     *
     * @param dto 详见QuerySystemRoleDTO
     * @param orderByClause 排序SQL
     * @return List<SystemRoleListVO>
     */
    List<SystemRoleVO> search(@Param("dto") QuerySystemRoleDTO dto, @Param("orderByClause") String orderByClause);

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return List<SystemRole>
     */
    List<SystemRole> selectByUserId(Integer userId);

}
