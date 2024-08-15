package com.eva.biz.system;

import com.eva.biz.system.dto.CreateSystemRoleDTO;
import com.eva.biz.system.dto.UpdateSystemRoleDTO;
import com.eva.core.exception.BusinessException;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.core.utils.AssertUtil;
import com.eva.core.utils.Utils;
import com.eva.dao.system.SystemRoleMapper;
import com.eva.dao.system.dto.QuerySystemRoleDTO;
import com.eva.dao.system.model.SystemRole;
import com.eva.dao.system.vo.SystemRoleVO;
import com.eva.service.system.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemRoleBiz {

    @Resource
    private SystemRoleService systemRoleService;

    @Resource
    private SystemPermissionService systemPermissionService;

    @Resource
    private SystemRoleMapper systemRoleMapper;

    /**
     * 创建角色
     *
     * @param dto 创建参数
     * @return 角色ID
     */
    public Integer create(CreateSystemRoleDTO dto) {
        AssertUtil.notEmpty(dto.getCode(), "角色编码不能为空");
        AssertUtil.notEmpty(dto.getName(), "角色名称不能为空");
        // 验证编码
        if (systemRoleService.exists(new SystemRole().setCode(dto.getCode()))) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "角色编码已存在");
        }
        // 执行创建
        SystemRole systemRole = new SystemRole();
        BeanUtils.copyProperties(dto, systemRole);
        return systemRoleService.create(systemRole);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    public void deleteById(Integer id) {
        AssertUtil.notEmpty(id, "主键不能为空");
        SystemRole role = systemRoleService.findById(id);
        if (role == null) {
            return;
        }
        // 不允许删除超级管理员权限
        if (role.getCode().equals(Utils.AppConfig.getSuperAdminRole())) {
            throw new BusinessException(ResponseStatus.NOT_ALLOWED, "不允许删除超级管理员权限");
        }
        systemRoleService.deleteById(id);
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色ID集
     */
    @Transactional
    public void deleteByIdInBatch(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    /**
     * 更新角色
     *
     * @param dto 修改参数
     */
    public void updateById(UpdateSystemRoleDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "主键不能为空");
        AssertUtil.notEmpty(dto.getCode(), "角色编码不能为空");
        AssertUtil.notEmpty(dto.getName(), "角色名称不能为空");
        SystemRole role = systemRoleService.findById(dto.getId());
        AssertUtil.notEmpty(role, ResponseStatus.DATA_EMPTY);
        // 不可修改超级管理员编码
        if (role.getCode().equals(Utils.AppConfig.getSuperAdminRole()) && !role.getCode().equals(dto.getCode())) {
            throw new BusinessException(ResponseStatus.NOT_ALLOWED, "不允许修改超级管理员编码");
        }
        // 验证角色编码
        role = SystemRole.builder().id(dto.getId()).code(dto.getCode()).build();
        if (systemRoleService.exists(role)) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "角色编号已存在");
        }
        // 执行创建
        SystemRole newRole = new SystemRole();
        BeanUtils.copyProperties(dto, newRole);
        systemRoleService.updateById(newRole);
    }

    /**
     * 分页查询
     *
     * @param pageWrap 分页参数
     * @return 分页数据
     */
    public PageData<SystemRoleVO> findPage(PageWrap<QuerySystemRoleDTO> pageWrap) {
        PageHelper.startPage(pageWrap.getPage(), pageWrap.getCapacity());
        List<SystemRoleVO> roleList = systemRoleMapper.search(pageWrap.getModel(), pageWrap.getOrderByClause());
        for (SystemRoleVO role : roleList) {
            role.setPermissions(systemPermissionService.findByRoleId(role.getId()));
        }
        return PageData.from(new PageInfo<>(roleList));
    }
}
