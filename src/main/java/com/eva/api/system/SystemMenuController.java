package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.SystemMenuBiz;
import com.eva.biz.system.dto.CreateSystemMenuDTO;
import com.eva.biz.system.dto.UpdateSystemMenuDTO;
import com.eva.biz.system.dto.UpdateSystemMenuStatusDTO;
import com.eva.core.prevent.PreventRepeat;
import com.eva.core.authorize.ContainPermissions;
import com.eva.core.model.ApiResponse;
import com.eva.biz.common.UpdateSortDTO;
import com.eva.dao.system.vo.SystemMenuNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "系统菜单")
@RestController
@RequestMapping("/system/menu")
public class SystemMenuController extends BaseController {

    @Resource
    private SystemMenuBiz systemMenuBiz;

    @PreventRepeat
    @ApiOperation("新建")
    @PostMapping("/create")
    @ContainPermissions("system:menu:create")
    public ApiResponse<?> create(@RequestBody CreateSystemMenuDTO systemMenu) {
        return ApiResponse.success(systemMenuBiz.create(systemMenu));
    }

    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    @ContainPermissions("system:menu:delete")
    public ApiResponse<?> deleteById(@PathVariable Integer id) {
        systemMenuBiz.deleteById(id);
        return ApiResponse.success(null);
    }

    @ApiOperation("批量删除")
    @GetMapping("/delete/batch")
    @ContainPermissions("system:menu:delete")
    public ApiResponse<?> deleteByIdInBatch(@RequestParam String ids) {
        systemMenuBiz.deleteByIdInBatch(this.getIdList(ids));
        return ApiResponse.success(null);
    }

    @ApiOperation("修改")
    @PostMapping("/updateById")
    @ContainPermissions("system:menu:update")
    public ApiResponse<?> updateById(@RequestBody UpdateSystemMenuDTO dto) {
        systemMenuBiz.updateById(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("修改状态")
    @PostMapping("/updateStatus")
    @ContainPermissions("system:menu:update")
    public ApiResponse<?> updateStatus(@RequestBody UpdateSystemMenuStatusDTO dto) {
        systemMenuBiz.updateStatus(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("修改排序")
    @PostMapping("/updateSort")
    @ContainPermissions("system:menu:sort")
    public ApiResponse<?> updateSort (@RequestBody UpdateSortDTO dto) {
        systemMenuBiz.updateSort(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("查询所有")
    @PostMapping("/all")
    @ContainPermissions("system:menu:query")
    public ApiResponse<List<SystemMenuNodeVO>> findAll () {
        return ApiResponse.success(systemMenuBiz.search());
    }

}
