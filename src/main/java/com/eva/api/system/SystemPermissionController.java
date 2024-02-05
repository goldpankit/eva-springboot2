package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.SystemPermissionBiz;
import com.eva.biz.system.vo.PermissionVO;
import com.eva.core.authorize.AuthorizeExpress;
import com.eva.core.prevent.PreventRepeat;
import com.eva.core.model.ApiResponse;
import com.eva.biz.system.dto.ConfigPermissionsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "系统权限")
@RestController
@RequestMapping("/system/permission")
public class SystemPermissionController extends BaseController {

    @Resource
    private SystemPermissionBiz systemMenuPermissionBiz;

    @ApiOperation("获取权限数据")
    @GetMapping("/data")
    @AuthorizeExpress("isSuperAdmin() || hasPermissions('system:role:config')")
    public ApiResponse<PermissionVO> getPermissions () {
        return ApiResponse.success(systemMenuPermissionBiz.getPermissions());
    }

    @PreventRepeat
    @ApiOperation("配置权限")
    @PostMapping("/config")
    @AuthorizeExpress("isSuperAdmin() || hasPermissions('system:role:config')")
    public ApiResponse<?> configMenuPermissions (@RequestBody ConfigPermissionsDTO dto) {
        systemMenuPermissionBiz.configPermissions(dto);
        return ApiResponse.success(null);
    }
}
