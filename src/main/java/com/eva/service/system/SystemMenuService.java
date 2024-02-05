package com.eva.service.system;

import com.eva.core.model.LoginUserInfo;
import com.eva.core.utils.Utils;
import com.eva.dao.system.SystemMenuMapper;
import com.eva.dao.system.model.SystemMenu;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SystemMenuService extends BaseService<SystemMenu, SystemMenuMapper> {

    public SystemMenuService(SystemMenuMapper mapper, Environment environment) {
        super(mapper, environment);
    }

    /**
     * 为菜单重新排序
     *
     * @param parentId 父菜单主键
     */
    @Transactional
    public void updateSortByParentId(Integer parentId) {
        this.updateSort(this.findSortedChildrenByParentId(parentId));
    }

    /**
     * 修改排序值
     * @param menus 菜单列表
     */
    @Transactional
    public void updateSort(List<SystemMenu> menus) {
        List<SystemMenu> newMenus = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            newMenus.add(SystemMenu.builder()
                    .id(menus.get(i).getId())
                    .parentId(menus.get(i).getParentId())
                    .iconId(menus.get(i).getIconId())
                    .permissionId(menus.get(i).getPermissionId())
                    .sort(i + 1)
                    .build());
        }
        // 批量修改排序值
        this.updateByIdInBatch(newMenus);
    }

    /**
     * 获取子菜单列表
     *
     * @param parentId 父菜单主键
     * @return 子菜单列表
     */
    public List<SystemMenu> findSortedChildrenByParentId(Integer parentId) {
        // 父菜单主键不为null，获取子菜单
        if (parentId != null) {
            QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(SystemMenu::getParentId, parentId)
                    .eq(SystemMenu::getDeleted, Boolean.FALSE)
                    .orderByAsc(SystemMenu::getSort)
            ;
            return this.findList(queryWrapper);
        }
        // 父菜单主键为null，获取根菜单
        QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .isNull(SystemMenu::getParentId)
                .eq(SystemMenu::getDeleted, Boolean.FALSE)
                .orderByAsc(SystemMenu::getSort);
        return this.findList(queryWrapper);
    }

    /**
     * 获取子菜单最大排序值
     *
     * @param parentId 父菜单主键
     * @return 最大排序值
     */
    public int getMaxSort(Integer parentId) {
        QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemMenu::getDeleted, Boolean.FALSE);
        // 父菜单主键为null，查询根菜单数量
        if (parentId == null) {
            queryWrapper.lambda().isNull(SystemMenu::getParentId);
        }
        // 父菜单主键不为null，查询子菜单数量
        else {
            queryWrapper.lambda().eq(SystemMenu::getParentId, parentId);
        }
        long sort = this.count(queryWrapper);
        return (int) sort + 1;
    }

    /**
     * 结合用户菜单权限查询子孙菜单ID
     *
     * @param menuId 菜单ID
     * @return 子孙菜单ID集
     */
    public Set<Integer> findChildIdsWithPermission(Integer menuId) {
        LoginUserInfo loginUser = Utils.Session.getLoginUser();
        Set<Integer> pool = new LinkedHashSet<>();
        this.fillChildren(pool, Collections.singleton(menuId), loginUser);
        return pool;
    }

    /**
     * 填充子菜单ID
     *
     * @param pool 子菜单集合，用于记录子菜单ID
     * @param parentIds 父菜单ID
     */
    private void fillChildren(Set<Integer> pool, Set<Integer> parentIds, LoginUserInfo loginUserInfo) {
        QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SystemMenu::getParentId, parentIds)
                // 拥有权限的菜单 和 不需要权限的菜单
                .and(qw -> qw
                        .in(SystemMenu::getPermissionId, loginUserInfo.getMenuPermissionIds())
                        .or()
                        .isNull(SystemMenu::getPermissionId)
                )
                .eq(SystemMenu::getDeleted, Boolean.FALSE)
        ;
        Set<Integer> ids = this.findIds(queryWrapper);
        if (ids.size() > 0) {
            pool.addAll(ids);
            this.fillChildren(pool, ids, loginUserInfo);
        }
    }
}
