package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.SystemRoleBiz;
import com.eva.biz.system.dto.CreateSystemRoleDTO;
import com.eva.biz.system.dto.UpdateSystemRoleDTO;
import com.eva.core.authorize.ContainAnyPermissions;
import com.eva.core.authorize.ContainPermissions;
import com.eva.core.prevent.PreventRepeat;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.core.utils.Utils;
import com.eva.dao.system.dto.QuerySystemRoleDTO;
import com.eva.dao.system.model.SystemRole;
import com.eva.dao.system.vo.SystemRoleVO;
import com.eva.service.system.SystemRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "系统角色")
@RestController
@RequestMapping("/system/role")
public class SystemRoleController extends BaseController {

    @Resource
    private SystemRoleBiz systemRoleBiz;

    @Resource
    private SystemRoleService systemRoleService;

    @PreventRepeat
    @ApiOperation("新建")
    @PostMapping("/create")
    @ContainPermissions("system:role:create")
    public ApiResponse<?> create(@RequestBody CreateSystemRoleDTO dto) {
        return ApiResponse.success(systemRoleBiz.create(dto));
    }

    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    @ContainPermissions("system:role:delete")
    public ApiResponse<?> deleteById(@PathVariable Integer id) {
        systemRoleBiz.deleteById(id);
        return ApiResponse.success(null);
    }

    @ApiOperation("批量删除")
    @GetMapping("/delete/batch")
    @ContainPermissions("system:role:delete")
    public ApiResponse<?> deleteByIdInBatch(@RequestParam String ids) {
        systemRoleBiz.deleteByIdInBatch(this.getIdList(ids));
        return ApiResponse.success(null);
    }

    @ApiOperation("修改")
    @PostMapping("/updateById")
    @ContainPermissions("system:role:update")
    public ApiResponse<?> updateById(@RequestBody UpdateSystemRoleDTO dto) {
        systemRoleBiz.updateById(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    @ContainPermissions("system:role:query")
    public ApiResponse<PageData<SystemRoleVO>> findPage (@RequestBody PageWrap<QuerySystemRoleDTO> pageWrap) {
        return ApiResponse.success(systemRoleBiz.findPage(pageWrap));
    }

    @ApiOperation("查询所有")
    @GetMapping("/all")
    @ContainAnyPermissions({"system:role:query", "system:user:role:config"})
    public ApiResponse<List<SystemRole>> findAll () {
        List<SystemRole> roles = systemRoleService.findAll();
        // 如果不是超级管理员，不要查询出超级管理员角色
        if (!getLoginUser().getIsSuperAdmin()) {
            roles.removeIf(role -> role.getCode().equals(Utils.AppConfig.getSuperAdminRole()));
        }
        return ApiResponse.success(roles);
    }
}
