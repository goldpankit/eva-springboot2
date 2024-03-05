package com.eva.biz.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eva.biz.system.dto.CreateSystemMenuDTO;
import com.eva.biz.system.dto.UpdateSystemMenuDTO;
import com.eva.biz.system.dto.UpdateSystemMenuStatusDTO;
import com.eva.core.constants.Constants;
import com.eva.core.exception.BusinessException;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.model.LoginUserInfo;
import com.eva.core.utils.AssertUtil;
import com.eva.core.utils.Utils;
import com.eva.biz.common.UpdateSortDTO;
import com.eva.dao.system.SystemMenuMapper;
import com.eva.dao.system.dto.QuerySystemMenuDTO;
import com.eva.dao.system.model.SystemMenu;
import com.eva.dao.system.model.SystemMenuFunc;
import com.eva.dao.system.vo.SystemMenuVO;
import com.eva.dao.system.vo.SystemMenuNodeVO;
import com.eva.service.system.SystemMenuFuncService;
import com.eva.service.system.SystemMenuService;
import com.eva.service.system.SystemPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemMenuBiz {

    @Resource
    private SystemMenuService systemMenuService;

    @Resource
    private SystemMenuMapper systemMenuMapper;

    @Resource
    private SystemPermissionService systemPermissionService;

    @Resource
    private SystemMenuFuncService systemMenuFuncService;

    @Resource
    private SystemMenuFuncBiz systemMenuFuncBiz;

    /**
     * 创建
     *
     * @param dto 字段信息
     * @return 主键
     */
    public Integer create(CreateSystemMenuDTO dto) {
        AssertUtil.notEmpty(dto.getName(), "菜单名称不能为空");
        // 非目录
        if (!Constants.SystemMenu.TYPE_DIR.equals(dto.getType())) {
            AssertUtil.notEmpty(dto.getUri(), "访问路径不能为空");
        }
        // 外部链接
        if (Constants.SystemMenu.TYPE_IFRAME.equals(dto.getType())
                || Constants.SystemMenu.TYPE_EXTERNAL.equals(dto.getType())) {
            AssertUtil.isURL(dto.getUri(), "访问路径格式不正确");
        }
        // 验证父菜单
        if (dto.getParentId() != null) {
            this.checkParent(dto.getParentId(), null);
        }
        // 执行创建
        // - 目录，清空访问地址和权限标识符
        if (Constants.SystemMenu.TYPE_DIR.equals(dto.getType())) {
            dto.setUri(null);
            dto.setPermission(null);
        }
        SystemMenu systemMenu = new SystemMenu();
        BeanUtils.copyProperties(dto, systemMenu);
        // - 排序处理
        systemMenu.setSort(systemMenuService.getMaxSort(dto.getParentId()));
        // - 权限处理
        systemMenu.setPermissionId(systemPermissionService.create(dto.getPermission(), dto.getName()));
        return systemMenuService.create(systemMenu);
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @Transactional
    public void deleteById(Integer id) {
        AssertUtil.notNull(id, "主键不能为空");
        SystemMenu menu = systemMenuService.findById(id);
        if (menu == null) {
            return;
        }
        // 越权验证
        this.checkPrivilege(menu);
        // 查询子菜单主键集合
        Set<Integer> ids = systemMenuService.findChildIdsWithPermission(id);
        ids.add(id);
        // 查询出所有待删除的菜单
        List<SystemMenu> menus = systemMenuService.findByIds(ids);
        if (menus.isEmpty()) {
            return;
        }
        List<SystemMenu> deleteList = new ArrayList<>();
        // 循环删除菜单
        for (SystemMenu m : menus) {
            // 删除权限
            systemPermissionService.deleteByIdAllowNull(m.getPermissionId());
            // 添加到待删除列表
            SystemMenu newMenu = new SystemMenu();
            newMenu.setId(m.getId());
            // - 保留父菜单ID
            newMenu.setParentId(m.getParentId());
            newMenu.setDeleted(Boolean.TRUE);
            deleteList.add(newMenu);
        }
        // 批量删除菜单
        systemMenuService.updateByIdInBatch(deleteList);
        // 修改兄弟节点排序
        List<SystemMenu> brothers = systemMenuService.findSortedChildrenByParentId(menu.getParentId());
        // 删除菜单下的功能
        QueryWrapper<SystemMenuFunc> queryFuncWrapper = new QueryWrapper<>();
        queryFuncWrapper.lambda()
                .in(SystemMenuFunc::getMenuId, ids)
                .eq(SystemMenuFunc::getDeleted, Boolean.FALSE)
        ;
        List<SystemMenuFunc> menuFunctions = systemMenuFuncService.findList(queryFuncWrapper);
        if (!CollectionUtils.isEmpty(menuFunctions)) {
            systemMenuFuncBiz.deleteByIdInBatch(menuFunctions
                    .stream()
                    .map(SystemMenuFunc::getId).collect(Collectors.toList())
            );
        }
        systemMenuService.updateSort(brothers);
    }

    /**
     * 批量删除
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
     * 根据主键更新
     *
     * @param dto 更新后的字段信息
     */
    @Transactional
    public void updateById(UpdateSystemMenuDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "菜单主键不能为空");
        AssertUtil.notEmpty(dto.getName(), "菜单名称不能为空");
        // 验证type
        // 非目录
        if (!Constants.SystemMenu.TYPE_DIR.equals(dto.getType())) {
            AssertUtil.notEmpty(dto.getUri(), "访问路径不能为空");
        }
        // 外部链接
        if (Constants.SystemMenu.TYPE_IFRAME.equals(dto.getType())
                || Constants.SystemMenu.TYPE_EXTERNAL.equals(dto.getType())) {
            AssertUtil.isURL(dto.getUri(), "访问路径格式不正确");
        }
        SystemMenu menu = systemMenuService.findById(dto.getId());
        AssertUtil.notEmpty(menu, ResponseStatus.DATA_EMPTY);
        // 越权验证
        this.checkPrivilege(menu);
        // 验证上级菜单
        if (dto.getParentId() != null) {
            this.checkParent(dto.getParentId(), dto.getId());
        }
        // 执行修改
        // - 目录，清空访问地址和权限标识符
        if (Constants.SystemMenu.TYPE_DIR.equals(dto.getType())) {
            dto.setUri(null);
            dto.setPermission(null);
        }
        SystemMenu newMenu = new SystemMenu();
        BeanUtils.copyProperties(dto, newMenu);
        // - 权限处理
        newMenu.setPermissionId(systemPermissionService.sync(
                menu.getPermissionId(),
                dto.getPermission(),
                dto.getName())
        );
        systemMenuService.updateById(newMenu);
        // 如果上级菜单从有到无，或发生了变更，则调整原来父菜单下的子菜单排序
        if (!Objects.equals(dto.getId(), menu.getId())) {
            systemMenuService.updateSortByParentId(menu.getParentId());
        }
    }

    /**
     * 修改菜单状态
     *
     * @param dto 修改状态参数
     */
    public void updateStatus (UpdateSystemMenuStatusDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "菜单主键不能为空");
        AssertUtil.notEmpty(dto.getDisabled(), "菜单状态不能为空");
        // 查询菜单
        SystemMenu menu = systemMenuService.findById(dto.getId());
        SystemMenu newMenu = new SystemMenu();
        BeanUtils.copyProperties(dto, newMenu);
        // 保留父级菜单、图标和权限
        newMenu.setParentId(menu.getParentId());
        newMenu.setIconId(menu.getIconId());
        newMenu.setPermissionId(menu.getPermissionId());
        systemMenuService.updateById(newMenu);
    }

    /**
     * 修改排序
     *
     * @param dto 排序参数
     */
    public void updateSort(UpdateSortDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "菜单主键不能为空");
        AssertUtil.notEmpty(dto.getTargetId(), "目标菜单主键不能为空");
        AssertUtil.notEquals(dto.getId(), dto.getTargetId(), "菜单主键和目标菜单主键不能相同");
        SystemMenu currentMenu = systemMenuService.findById(dto.getId());
        SystemMenu targetMenu = systemMenuService.findById(dto.getTargetId());
        AssertUtil.notEmpty(currentMenu, ResponseStatus.DATA_EMPTY);
        AssertUtil.notEmpty(targetMenu, ResponseStatus.DATA_EMPTY);
        // 越权验证
        this.checkPrivilege(currentMenu);
        // 获取待排序的菜单列表
        List<SystemMenu> menus = systemMenuService.findSortedChildrenByParentId(currentMenu.getParentId());
        // 获取当前菜单和目标菜单的索引
        int currentIndex = menus.indexOf(currentMenu);
        int targetIndex = menus.indexOf(targetMenu);
        if (currentIndex == -1 || targetIndex == -1) {
            log.error("菜单移动失败：移动的菜单和目标菜单不在同一个子集中");
            throw new BusinessException(ResponseStatus.DATA_EMPTY, "排序的菜单不存在，请刷新后重试");
        }
        // 调整顺序
        menus.set(currentIndex, targetMenu);
        menus.set(targetIndex, currentMenu);
        // 执行修改
        systemMenuService.updateSort(menus);
    }

    /**
     * 查询菜单管理列表
     *
     * @return 菜单节点列表
     */
    public List<SystemMenuNodeVO> search() {
        // 获取当前登录用户信息
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        // 构造查询参数
        QuerySystemMenuDTO queryDto = new QuerySystemMenuDTO();
        queryDto.setPermissionIds(userInfo.getMenuPermissionIds());
        queryDto.setIsSuperAdmin(userInfo.getIsSuperAdmin());
        // 执行查询
        List<SystemMenuVO> menus = systemMenuMapper.search(queryDto);
        // 转为树结构
        return this.transferToTree(menus);
    }

    /**
     * 查询用户左侧菜单列表
     *
     * @return 菜单节点列表
     */
    public List<SystemMenuNodeVO> findUserMenus() {
        // 获取当前登录用户信息
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        // 构造查询参数
        QuerySystemMenuDTO queryDto = new QuerySystemMenuDTO();
        queryDto.setPermissionIds(userInfo.getMenuPermissionIds());
        queryDto.setIsSuperAdmin(userInfo.getIsSuperAdmin());
        // 执行查询
        List<SystemMenuVO> menus = systemMenuMapper.selectUserMenus(queryDto);
        // 转为树结构
        List<SystemMenuNodeVO> menuNodes = this.transferToTree(menus);
        // 移除空目录
        this.removeEmptyDirs(menuNodes);
        return menuNodes;
    }

    /**
     * 转换为树
     *
     * @param menus 菜单列表
     * @return List<SystemMenuNodeVO>
     */
    public List<SystemMenuNodeVO> transferToTree (List<SystemMenuVO> menus) {
        List<SystemMenuNodeVO> rootMenus = new ArrayList<>();
        // 添加根菜单
        for (SystemMenuVO menu : menus) {
            if (menu.getParentId() == null) {
                SystemMenuNodeVO rootNode = new SystemMenuNodeVO();
                BeanUtils.copyProperties(menu, rootNode);
                rootNode.setIndex("menu_" + menu.getId());
                rootNode.setChildren(new ArrayList<>());
                rootMenus.add(rootNode);
            }
        }
        // 移除根菜单
        menus.removeIf(menu -> menu.getParentId() == null);
        // 循环为根菜单添加子菜单
        for (SystemMenuNodeVO child : rootMenus) {
            this.fillChildren(child, menus);
        }
        return rootMenus;
    }

    /**
     * 去掉树中空的目录
     *
     * @param menuNodes 菜单树
     */
    private void removeEmptyDirs(List<SystemMenuNodeVO> menuNodes) {
        if (menuNodes == null) {
            return;
        }
        List<SystemMenuNodeVO> removedItems = new ArrayList<>();
        for (SystemMenuNodeVO node : menuNodes) {
            if (!Constants.SystemMenu.TYPE_DIR.equals(node.getType())) {
                continue;
            }
            if (node.getChildren().size() > 0) {
                this.removeEmptyDirs(node.getChildren());
            }
            if (node.getChildren().size() == 0) {
                removedItems.add(node);
            }
        }
        if (removedItems.size() > 0) {
            menuNodes.removeAll(removedItems);
        }
    }

    /**
     * 填充子菜单
     *
     * @param parent 父菜单
     * @param menus 未添加到父菜单中的子菜单列表
     */
    private void fillChildren(SystemMenuNodeVO parent, List<SystemMenuVO> menus) {
        if (menus.size() == 0) {
            return;
        }
        // 记录已添加的子菜单
        List<SystemMenuVO> addedMenus = new ArrayList<>();
        // 循环子菜单，逐一添加到父菜单中
        for (SystemMenuVO menu : menus) {
            if (!parent.getId().equals(menu.getParentId())) {
                continue;
            }
            // 构建子菜单节点对象
            SystemMenuNodeVO child = new SystemMenuNodeVO();
            BeanUtils.copyProperties(menu, child);
            child.setIndex("menu_" + menu.getId());
            child.setChildren(new ArrayList<>());
            parent.getChildren().add(child);
            // 记录到已添加的菜单列表中
            addedMenus.add(menu);
        }
        // 移除已添加的菜单
        menus.removeAll(addedMenus);
        // 递归为子菜单添加子菜单
        for (SystemMenuNodeVO child : parent.getChildren()) {
            this.fillChildren(child, menus);
        }
    }

    /**
     * 验证上级菜单
     *
     * @param parentId 上级菜单主键
     * @param currentMenuId 当前菜单主键
     */
    private void checkParent (Integer parentId, Integer currentMenuId) {
        SystemMenu parent = systemMenuService.findById(parentId);
        // 验证上级菜单是否存在
        AssertUtil.notNull(parent, "上级菜单不存在");
        // 验证上级菜单是否合理
        if (currentMenuId != null && currentMenuId.equals(parentId)) {
            throw new BusinessException(ResponseStatus.NOT_ALLOWED, "上级菜单不能为当前菜单");
        }
        if (!StringUtils.isBlank(parent.getUri())) {
            throw new BusinessException(ResponseStatus.NOT_ALLOWED, "上级菜单必须为一个目录");
        }
        // 验证上级菜单是否越权
        this.checkPrivilege(parent);
    }

    /**
     * 越权验证
     *
     * @param currentMenu 当前菜单
     */
    private void checkPrivilege (SystemMenu currentMenu) {
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        // 超级管理员
        if (userInfo.getIsSuperAdmin()) {
            return;
        }
        // 非超级管理员，查看是否存在权限
        if (currentMenu.getPermissionId() != null) {
            if (!Utils.Session.getLoginUser()
                    .getMenuPermissionIds()
                    .contains(currentMenu.getPermissionId())
            ) {
                throw new BusinessException(ResponseStatus.PRIVILEGE_ERROR);
            }
        }
    }
}
