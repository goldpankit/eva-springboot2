package com.eva.biz.system;

import com.eva.biz.system.dto.UpdateSystemMenuFuncDTO;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.model.LoginUserInfo;
import com.eva.core.utils.AssertUtil;
import com.eva.core.utils.Utils;
import com.eva.dao.system.model.SystemMenu;
import com.eva.service.system.SystemPermissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.SystemMenuFuncMapper;
import com.eva.dao.system.model.SystemMenuFunc;
import com.eva.dao.system.dto.QuerySystemMenuFuncDTO;
import com.eva.biz.system.dto.CreateSystemMenuFuncDTO;
import com.eva.dao.system.vo.SystemMenuFuncVO;
import com.eva.service.system.SystemMenuFuncService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemMenuFuncBiz {

    @Resource
    private SystemMenuFuncMapper systemMenuFuncMapper;

    @Resource
    private SystemMenuFuncService systemMenuFuncService;

    @Resource
    private SystemPermissionService systemPermissionService;

    /**
     * 创建
     *
     * @param dto 字段信息
     * @return 主键
     */
    @Transactional
    public Integer create(CreateSystemMenuFuncDTO dto) {
        AssertUtil.notEmpty(dto.getMenuId(), "菜单ID不能为空");
        AssertUtil.notEmpty(dto.getName(), "功能名称不能为空");
        AssertUtil.notEmpty(dto.getPermission(), "权限标识符不能为空");
        // 执行创建
        SystemMenuFunc newRecord = new SystemMenuFunc();
        BeanUtils.copyProperties(dto, newRecord);
        // - 权限处理
        newRecord.setPermissionId(systemPermissionService.create(dto.getPermission(), dto.getName()));
        return systemMenuFuncService.create(newRecord);
    }

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    @Transactional
    public void deleteById(Integer id) {
        // 查询
        SystemMenuFunc menuFunc = systemMenuFuncService.findById(id);
        if (menuFunc == null) {
            return;
        }
        // 越权验证
        this.checkPrivilege(menuFunc);
        // 删除权限
        systemPermissionService.deleteByIdAllowNull(menuFunc.getPermissionId());
        // 执行删除
        SystemMenuFunc newFunc = new SystemMenuFunc();
        newFunc.setId(id);
        newFunc.setPermissionId(null);
        newFunc.setDeleted(Boolean.TRUE);
        systemMenuFuncService.updateById(newFunc);
    }

    /**
     * 批量根据主键删除
     *
     * @param ids 主键集合
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
     * 根据主键修改
     *
     * @param dto 修改后的字段信息
     */
    @Transactional
    public void updateById(UpdateSystemMenuFuncDTO dto) {
        AssertUtil.notNull(dto.getId(), "主键不能为空");
        AssertUtil.notNull(dto.getMenuId(), "菜单ID不能为空");
        AssertUtil.notEmpty(dto.getName(), "功能名称不能为空");
        AssertUtil.notEmpty(dto.getPermission(), "权限标识符不能为空");
        // 查询菜单
        SystemMenuFunc menuFunc = systemMenuFuncService.findById(dto.getId());
        if (menuFunc == null || menuFunc.getDeleted()) {
            throw new BusinessException(ResponseStatus.DATA_EMPTY);
        }
        // 越权验证
        this.checkPrivilege(menuFunc);
        // 执行修改
        SystemMenuFunc newRecord = new SystemMenuFunc();
        BeanUtils.copyProperties(dto, newRecord);
        // - 权限处理
        newRecord.setPermissionId(systemPermissionService.sync(
                menuFunc.getPermissionId(),
                dto.getPermission(),
                dto.getName())
        );
        systemMenuFuncService.updateById(newRecord);
    }

    /**
     * 分页查询
     *
     * @param pageWrap 分页参数
     * @return 分页数据
     */
    public PageData<SystemMenuFuncVO> search(PageWrap<QuerySystemMenuFuncDTO> pageWrap) {
        PageHelper.startPage(pageWrap.getPage(), pageWrap.getCapacity());
        // 获取当前登录用户信息
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        // 补充权限参数
        pageWrap.getModel().setUserId(userInfo.getId());
        pageWrap.getModel().setPermissionIds(userInfo.getMenuFuncPermissionIds());
        pageWrap.getModel().setIsSuperAdmin(userInfo.getIsSuperAdmin());
        List<SystemMenuFuncVO> result = systemMenuFuncMapper.search(pageWrap.getModel());
        return PageData.from(new PageInfo<>(result));
    }

    /**
     * 越权验证
     *
     * @param currentMenuFunc 当前菜单功能
     */
    private void checkPrivilege (SystemMenuFunc currentMenuFunc) {
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        // 超级管理员
        if (userInfo.getIsSuperAdmin()) {
            return;
        }
        // 非超级管理员
        if (currentMenuFunc.getPermissionId() != null) {
            // 如果是自己创建的菜单功能，即使没有权限也可删除
            if (currentMenuFunc.getCreatedBy().equals(userInfo.getId())) {
                return;
            }
            // 如果不存在该菜单功能权限，视为越权
            if (!Utils.Session.getLoginUser()
                    .getMenuFuncPermissionIds()
                    .contains(currentMenuFunc.getPermissionId())
            ) {
                throw new BusinessException(ResponseStatus.PRIVILEGE_ERROR);
            }
        }
    }
}
