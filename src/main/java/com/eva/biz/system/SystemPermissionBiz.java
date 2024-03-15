package com.eva.biz.system;

import com.eva.biz.system.vo.PermissionVO;
import com.eva.biz.system.vo.PermissionNodeVO;
import com.eva.core.constants.Constants;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.model.LoginUserInfo;
import com.eva.core.utils.AssertUtil;
import com.eva.core.utils.Utils;
import com.eva.biz.system.dto.ConfigPermissionsDTO;
import com.eva.dao.system.SystemConfigMapper;
import com.eva.dao.system.SystemMenuFuncMapper;
import com.eva.dao.system.dto.QuerySystemConfigDTO;
import com.eva.dao.system.dto.QuerySystemMenuFuncDTO;
import com.eva.dao.system.model.SystemRole;
import com.eva.dao.system.model.SystemRolePermission;
import com.eva.dao.system.vo.SystemConfigVO;
import com.eva.dao.system.vo.SystemMenuFuncVO;
import com.eva.dao.system.vo.SystemMenuNodeVO;
import com.eva.service.system.SystemPermissionService;
import com.eva.service.system.SystemRolePermissionService;
import com.eva.service.system.SystemRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SystemPermissionBiz {

    @Resource
    private SystemRoleService systemRoleService;

    @Resource
    private SystemMenuBiz systemMenuBiz;

    @Resource
    private SystemMenuFuncMapper systemMenuFuncMapper;

    @Resource
    private SystemConfigMapper systemConfigMapper;

    @Resource
    private SystemRolePermissionService systemRolePermissionService;

    @Resource
    private SystemPermissionService systemPermissionService;

    /**
     * 配置权限
     *
     * @param dto 配置信息
     */
    @Transactional
    public void configPermissions(ConfigPermissionsDTO dto) {
        AssertUtil.notEmpty(dto.getRoleId(), "角色主键不能为空");
        AssertUtil.notNull(dto.getMenuPermissionIds(), "菜单权限集不能为空");
        AssertUtil.notNull(dto.getFuncPermissionIds(), "功能权限集不能为空");
        // 验证角色是否存在
        SystemRole role = systemRoleService.findById(dto.getRoleId());
        AssertUtil.notNull(role, ResponseStatus.DATA_EMPTY);
        // 获取当前登录用户
        LoginUserInfo loginUser = Utils.Session.getLoginUser();
        // 验证是否为越权操作
        if (!loginUser.getIsSuperAdmin()) {
            this.checkPrivilege(dto, loginUser);
        }
        // 执行权限变更
        List<Integer> newPermissionIds = new ArrayList<>();
        if (dto.getMenuPermissionIds() != null) {
            // 删除菜单权限
            Set<Integer> menuPermissionIds = systemPermissionService
                    .findMenuPermissionIdsByRoleIds(Collections.singletonList(dto.getRoleId()));
            systemRolePermissionService.deleteByPermissionIds(dto.getRoleId(), menuPermissionIds);
            // 补充菜单权限
            newPermissionIds.addAll(dto.getMenuPermissionIds());
        }
        if (dto.getFuncPermissionIds() != null) {
            // 删除菜单功能权限
            Set<Integer> menuFuncPermissionIds = systemPermissionService
                    .findMenuFuncPermissionIdsByRoleIds(Collections.singletonList(dto.getRoleId()));
            systemRolePermissionService.deleteByPermissionIds(dto.getRoleId(), menuFuncPermissionIds);
            // 补充菜单功能权限
            newPermissionIds.addAll(dto.getFuncPermissionIds());
        }
        if (dto.getSystemConfigPermissionIds() != null) {
            // 删除系统配置项权限
            Set<Integer> systemConfigPermissionIds = systemPermissionService
                    .findSystemConfigPermissionIdsByRoleIds(Collections.singletonList(dto.getRoleId()));
            systemRolePermissionService.deleteByPermissionIds(dto.getRoleId(), systemConfigPermissionIds);
            // 补充系统配置项权限
            newPermissionIds.addAll(dto.getSystemConfigPermissionIds());
        }
        // 将补充的权限全部写入
        if (!CollectionUtils.isEmpty(newPermissionIds)) {
            for (Integer permissionId : newPermissionIds) {
                SystemRolePermission newRolePermission = new SystemRolePermission();
                newRolePermission.setRoleId(dto.getRoleId());
                newRolePermission.setPermissionId(permissionId);
                systemRolePermissionService.create(newRolePermission);
            }
        }
    }

    /**
     * 获取菜单权限树
     *
     * @return 权限节点列表
     */
    public PermissionVO getPermissions() {
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        PermissionVO permissionVO = new PermissionVO();
        // 查询菜单权限
        List<SystemMenuNodeVO> menuNodes = systemMenuBiz.search();
        // 查询菜单功能权限
        QuerySystemMenuFuncDTO queryFuncDto = new QuerySystemMenuFuncDTO();
        queryFuncDto.setPermissionIds(userInfo.getMenuFuncPermissionIds());
        queryFuncDto.setIsSuperAdmin(userInfo.getIsSuperAdmin());
        List<SystemMenuFuncVO> menuFunctions = systemMenuFuncMapper.search(queryFuncDto);
        permissionVO.setMenuNodes(this.toPermissionNodes(menuNodes, menuFunctions));
        // 查询系统配置权限
        QuerySystemConfigDTO queryConfigDto = new QuerySystemConfigDTO();
        queryConfigDto.setIsSuperAdmin(userInfo.getIsSuperAdmin());
        queryConfigDto.setPermissionIds(userInfo.getSystemConfigPermissionIds());
        queryConfigDto.setLoginUserId(userInfo.getId());
        List<SystemConfigVO> systemConfigs = systemConfigMapper.search(queryConfigDto);
        List<PermissionNodeVO> systemConfigNodes = new ArrayList<>();
        for (SystemConfigVO systemConfig : systemConfigs) {
            PermissionNodeVO systemConfigNode = PermissionNodeVO.builder()
                    .id(systemConfig.getPermissionId())
                    .permission(systemConfig.getPermission())
                    .label(systemConfig.getName())
                    .type("systemConfig")
                    .children(Collections.emptyList())
                    .build();
            systemConfigNodes.add(systemConfigNode);
        }
        permissionVO.setConfigNodes(systemConfigNodes);
        return permissionVO;
    }

    /**
     * 验证配置是否越权
     *
     * @param dto 配置信息
     * @param loginUser 当前登录的用户
     */
    private void checkPrivilege (ConfigPermissionsDTO dto, LoginUserInfo loginUser) {
        if (dto.getMenuPermissionIds() != null && !loginUser.getMenuPermissionIds().containsAll(dto.getMenuPermissionIds())) {
            throw new BusinessException(ResponseStatus.PRIVILEGE_ERROR);
        }
        if (dto.getFuncPermissionIds() != null && !loginUser.getMenuFuncPermissionIds().containsAll(dto.getFuncPermissionIds())) {
            throw new BusinessException(ResponseStatus.PRIVILEGE_ERROR);
        }
        if (dto.getSystemConfigPermissionIds() != null && !loginUser.getSystemConfigPermissionIds().containsAll(dto.getSystemConfigPermissionIds())) {
            throw new BusinessException(ResponseStatus.PRIVILEGE_ERROR);
        }
    }

    /**
     * 将菜单转为权限节点
     *
     * @param menuNodes 菜单节点列表
     * @return 权限节点列表
     */
    private List<PermissionNodeVO> toPermissionNodes (List<SystemMenuNodeVO> menuNodes, List<SystemMenuFuncVO> menuFuncs) {
        List<PermissionNodeVO> nodes = new ArrayList<>();
        for (SystemMenuNodeVO menuNode : menuNodes) {
            PermissionNodeVO permissionNode = PermissionNodeVO.builder()
                    .id(menuNode.getPermissionId())
                    .permission(menuNode.getPermission())
                    .type(Constants.SystemMenu.TYPE_DIR.equals(menuNode.getType())
                            ? "dir" : "menu")
                    .label(menuNode.getName())
                    .children(new ArrayList<>())
                    .build();
            nodes.add(permissionNode);
            // 当前菜单下存在子菜单，则将子菜单添加到子节点中
            if (!CollectionUtils.isEmpty(menuNode.getChildren())) {
                permissionNode.getChildren().addAll(this.toPermissionNodes(menuNode.getChildren(), menuFuncs));
                continue;
            }
            // 否则添加菜单功能
            List<Integer> handleIds = new ArrayList<>();
            for (SystemMenuFuncVO menuFunc : menuFuncs) {
                if (menuFunc.getMenuId().equals(menuNode.getId())) {
                    handleIds.add(menuFunc.getId());
                    PermissionNodeVO funcPermissionNode = PermissionNodeVO.builder()
                            .id(menuFunc.getPermissionId())
                            .permission(menuFunc.getPermission())
                            .type("func")
                            .label(menuFunc.getName())
                            .build();
                    permissionNode.getChildren().add(funcPermissionNode);
                }
            }
            // 删除已处理的菜单功能
            menuFuncs.removeIf(menuFunc -> handleIds.contains(menuFunc.getId()));
        }
        return nodes;
    }
}
