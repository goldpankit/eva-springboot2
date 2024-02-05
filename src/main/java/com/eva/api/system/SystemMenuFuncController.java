package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.dto.UpdateSystemMenuFuncDTO;
import com.eva.core.authorize.ContainPermissions;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.dto.QuerySystemMenuFuncDTO;
import com.eva.biz.system.dto.CreateSystemMenuFuncDTO;
import com.eva.dao.system.vo.SystemMenuFuncVO;
import com.eva.biz.system.SystemMenuFuncBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "菜单功能")
@Slf4j
@RestController
@RequestMapping("/system/menu/func")
public class SystemMenuFuncController extends BaseController {

    @Resource
    private SystemMenuFuncBiz systemMenuFuncBiz;

    @ApiOperation("创建")
    @PostMapping("/create")
    @ContainPermissions("system:menu:func:create")
    public ApiResponse<?> create(@RequestBody CreateSystemMenuFuncDTO dto) {
        systemMenuFuncBiz.create(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("根据ID删除")
    @GetMapping("/delete/{id}")
    @ContainPermissions("system:menu:func:delete")
    public ApiResponse<?> deleteById(@PathVariable Integer id) {
        systemMenuFuncBiz.deleteById(id);
        return ApiResponse.success(null);
    }

    @ApiOperation("根据ID批量删除")
    @GetMapping("/delete/batch")
    @ContainPermissions("system:menu:func:delete")
    public ApiResponse<?> deleteByIdInBatch(@RequestParam String ids) {
        systemMenuFuncBiz.deleteByIdInBatch(this.getIdList(ids));
        return ApiResponse.success(null);
    }

    @ApiOperation("根据ID修改")
    @PostMapping("/updateById")
    @ContainPermissions("system:menu:func:update")
    public ApiResponse<?> updateById(@RequestBody UpdateSystemMenuFuncDTO dto) {
        systemMenuFuncBiz.updateById(dto);
        return ApiResponse.success(null);
    }
    
    @ApiOperation("分页查询")
    @PostMapping("/page")
    @ContainPermissions("system:menu:func:query")
    public ApiResponse<PageData<SystemMenuFuncVO>> findPage(@RequestBody PageWrap<QuerySystemMenuFuncDTO> pageWrap) {
        return ApiResponse.success(systemMenuFuncBiz.search(pageWrap));
    }
}
