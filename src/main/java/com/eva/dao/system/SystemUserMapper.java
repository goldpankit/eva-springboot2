package com.eva.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eva.dao.system.dto.QuerySystemUserDTO;
import com.eva.dao.system.model.SystemUser;
import com.eva.dao.system.vo.SystemUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    /**
     * 查询用户管理列表
     *
     * @param dto 详见QuerySystemUserDTO
     * @param orderByClause 排序SQL
     * @return List<SystemUserListVO>
     */
    List<SystemUserVO> search(@Param("dto") QuerySystemUserDTO dto, @Param("orderByClause") String orderByClause);

}
