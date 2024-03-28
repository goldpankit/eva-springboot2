package com.eva.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eva.dao.system.model.SystemPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface SystemPermissionMapper extends BaseMapper<SystemPermission> {

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return List<SystemPermission>
     */
    List<SystemPermission> selectByUserId(Integer userId);

    /**
     * 根据角色ID查询
     *
     * @param roleId 角色ID
     * @return List<SystemPermission>
     */
    List<SystemPermission> selectByRoleId(Integer roleId);

    /**
     * 根据角色ID集查询菜单权限ID集
     *
     * @param roleIds 角色ID集
     * @return 菜单权限ID集
     */
    List<Integer> selectMenuPermissionIdsByRoleIds(@Param("roleIds") Set<Integer> roleIds);

    /**
     * 根据角色ID集查询功能权限ID集
     *
     * @param roleIds 角色ID集
     * @return 功能权限ID集
     */
    List<Integer> selectMenuFuncPermissionIdsByRoleIds(@Param("roleIds") Set<Integer> roleIds);

    /**
     * 根据角色ID集查询系统配置权限ID集
     *
     * @param roleIds 角色ID集
     * @return 系统配置权限ID集
     */
    List<Integer> selectSystemConfigPermissionIdsByRoleIds(@Param("roleIds") Set<Integer> roleIds);

}
