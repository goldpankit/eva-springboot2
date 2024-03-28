package com.eva.service.system;

import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.utils.AssertUtil;
import com.eva.dao.system.SystemPermissionMapper;
import com.eva.dao.system.model.SystemPermission;
import com.eva.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SystemPermissionService extends BaseService<SystemPermission, SystemPermissionMapper> {

    public SystemPermissionService(SystemPermissionMapper mapper, Environment environment) {
        super(mapper, environment);
    }

    /**
     * 创建
     *
     * @param code 权限编码
     * @param name 权限名称
     * @return 主键
     */
    @Transactional
    public Integer create(String code, String name) {
        // 空编码视为不创建
        if (StringUtils.isBlank(code)) {
            return null;
        }
        // 基础验证
        AssertUtil.notEmpty(name, "权限名称不能为空");
        // 验证权限标识符是否重复
        if (this.exists(new SystemPermission().setCode(code))) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "权限标识符已存在");
        }
        // 创建权限
        SystemPermission newPermission = SystemPermission.builder()
                .code(code)
                .name(name)
                .build();
        return this.create(newPermission);
    }

    /**
     * 根据主键删除，允许主键为null，为null时不做删除
     *
     * @param id 主键
     */
    @Transactional
    public void deleteByIdAllowNull(Integer id) {
        if (id == null) {
            return;
        }
        this.deleteById(id);
    }

    /**
     * 同步处理权限
     * 当挂载模块修改了权限码时，需调用该方法进行同步
     *
     * @param id   主键
     * @param code 权限编码
     * @param name 权限名称
     * @return 主键
     */
    @Transactional
    public Integer sync(Integer id, String code, String name) {
        // 创建权限
        if (id == null) {
            // 不存在权限标志符，不做处理
            if (StringUtils.isBlank(code)) {
                return null;
            }
            // 执行创建
            AssertUtil.notEmpty(name, "权限名称不能为空");
            return this.create(code, name);
        }
        // 删除权限
        if (StringUtils.isBlank(code)) {
            this.deleteByIdAllowNull(id);
            return null;
        }
        // 修改权限
        AssertUtil.notEmpty(name, "权限名称不能为空");
        // - 验证权限标识符是否已存在
        SystemPermission permission = SystemPermission.builder().id(id).code(code).build();
        if (this.exists(permission)) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "权限标识符已存在");
        }
        // - 执行修改
        SystemPermission newPermission = SystemPermission.builder()
                .id(id)
                .code(code)
                .name(name)
                .build();
        this.updateById(newPermission);
        return id;
    }

    /**
     * 查询用户的权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<SystemPermission> findByUserId(Integer userId) {
        return mapper.selectByUserId(userId);
    }

    /**
     * 查询角色的权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public List<SystemPermission> findByRoleId(Integer roleId) {
        return mapper.selectByRoleId(roleId);
    }

    /**
     * 根据角色ID集查询菜单权限ID集
     *
     * @param roleIds 角色ID集
     * @return 菜单权限ID列表
     */
    public Set<Integer> findMenuPermissionIdsByRoleIds(Set<Integer> roleIds) {
        return new HashSet<>(mapper.selectMenuPermissionIdsByRoleIds(roleIds));
    }

    /**
     * 根据角色ID集查询菜单功能权限ID集
     *
     * @param roleIds 角色ID集
     * @return 菜单功能权限ID列表
     */
    public Set<Integer> findMenuFuncPermissionIdsByRoleIds(Set<Integer> roleIds) {
        return new HashSet<>(mapper.selectMenuFuncPermissionIdsByRoleIds(roleIds));
    }

    /**
     * 根据角色ID集查询系统配置权限ID集合
     *
     * @param roleIds 角色ID集
     * @return 系统配置权限ID集合
     */
    public Set<Integer> findSystemConfigPermissionIdsByRoleIds(Set<Integer> roleIds) {
        return new HashSet<>(mapper.selectSystemConfigPermissionIdsByRoleIds(roleIds));
    }
}
