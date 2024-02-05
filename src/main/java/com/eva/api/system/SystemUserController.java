package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.SystemUserBiz;
import com.eva.biz.system.dto.UpdateSystemUserDTO;
import com.eva.core.authorize.AuthorizeExpress;
import com.eva.core.prevent.PreventRepeat;
import com.eva.core.secure.field.EnableSecureField;
import com.eva.core.trace.Trace;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.biz.system.dto.CreateSystemUserDTO;
import com.eva.biz.system.dto.CreateUserRoleDTO;
import com.eva.dao.system.dto.QuerySystemUserDTO;
import com.eva.biz.system.dto.ResetSystemUserPwdDTO;
import com.eva.dao.system.vo.SystemUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/system/user")
public class SystemUserController extends BaseController {

    @Resource
    private SystemUserBiz systemUserBiz;

    @PreventRepeat
    @ApiOperation("配置用户角色")
    @PostMapping("/createUserRole")
    @AuthorizeExpress("isSuperAdmin() || hasPermissions('system:user:role:config')")
    public ApiResponse<?> createUserRole (@RequestBody CreateUserRoleDTO dto) {
        systemUserBiz.createUserRole(dto);
        return ApiResponse.success(null);
    }

    @Trace(withRequestParameters = false)
    @PreventRepeat
    @ApiOperation("重置用户密码")
    @PostMapping("/resetPwd")
    @AuthorizeExpress("isSuperAdmin() || hasPermissions('system:user:password:reset')")
    public ApiResponse<?> resetPwd (@RequestBody ResetSystemUserPwdDTO dto) {
        dto.setOperaUserId(this.getLoginUser().getId());
        systemUserBiz.resetPwd(dto);
        return ApiResponse.success(null);
    }

    @Trace(withRequestParameters = false)
    @PreventRepeat
    @ApiOperation("新建")
    @PostMapping("/create")
    @AuthorizeExpress("isSuperAdmin() || hasPermissions('system:user:create')")
    @EnableSecureField
    public ApiResponse<?> create(@RequestBody CreateSystemUserDTO systemUser) {
        systemUserBiz.create(systemUser);
        return ApiResponse.success(null);
    }

    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    @AuthorizeExpress("isSuperAdmin() || hasPermissions('system:user:delete')")
    public ApiResponse<?> deleteById(@PathVariable Integer id) {
        systemUserBiz.deleteById(id);
        return ApiResponse.success(null);
    }

    @ApiOperation("批量删除")
    @GetMapping("/delete/batch")
    @AuthorizeExpress("isSuperAdmin() || hasPermissions('system:user:delete')")
    public ApiResponse<?> deleteByIdInBatch(@RequestParam String ids) {
        systemUserBiz.deleteByIdInBatch(this.getIdList(ids));
        return ApiResponse.success(null);
    }

    @Trace(withRequestParameters = false)
    @ApiOperation("修改")
    @PostMapping("/updateById")
    @AuthorizeExpress("isSuperAdmin() || hasPermissions('system:user:update')")
    @EnableSecureField
    public ApiResponse<?> updateById(@RequestBody UpdateSystemUserDTO systemUser) {
        systemUserBiz.updateById(systemUser);
        return ApiResponse.success(null);
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    @AuthorizeExpress("isSuperAdmin() || hasPermissions('system:user:query')")
    @EnableSecureField
    public ApiResponse<PageData<SystemUserVO>> findPage (@RequestBody PageWrap<QuerySystemUserDTO> pageWrap) {
        PageData<SystemUserVO> page = systemUserBiz.findPage(pageWrap);
        return ApiResponse.success(page);
    }
}
