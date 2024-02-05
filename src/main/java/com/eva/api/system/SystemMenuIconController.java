package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.core.authorize.ContainPermissions;
import com.eva.core.authorize.ContainAnyPermissions;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.dto.QuerySystemMenuIconDTO;
import com.eva.biz.system.dto.CreateSystemMenuIconDTO;
import com.eva.biz.system.dto.UpdateSystemMenuIconDTO;
import com.eva.dao.system.model.SystemMenuIcon;
import com.eva.dao.system.vo.SystemMenuIconVO;
import com.eva.biz.system.SystemMenuIconBiz;
import com.eva.service.system.SystemMenuIconService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "系统图标")
@Slf4j
@RestController
@RequestMapping("/system/menu/icon")
public class SystemMenuIconController extends BaseController {

    @Resource
    private SystemMenuIconBiz systemMenuIconBiz;

    @Resource
    private SystemMenuIconService systemMenuIconService;

    @ApiOperation("创建")
    @PostMapping("/create")
    @ContainPermissions("system:menu:icon:create")
    public ApiResponse<?> create(@RequestBody CreateSystemMenuIconDTO dto) {
        systemMenuIconBiz.create(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("根据ID删除")
    @GetMapping("/delete/{id}")
    @ContainPermissions("system:menu:icon:delete")
    public ApiResponse<?> deleteById(@PathVariable Integer id) {
        systemMenuIconBiz.deleteById(id);
        return ApiResponse.success(null);
    }

    @ApiOperation("根据ID批量删除")
    @GetMapping("/delete/batch")
    @ContainPermissions("system:menu:icon:delete")
    public ApiResponse<?> deleteByIdInBatch(@RequestParam String ids) {
        systemMenuIconBiz.deleteByIdInBatch(this.getIdList(ids));
        return ApiResponse.success(null);
    }

    @ApiOperation("根据ID修改")
    @PostMapping("/updateById")
    @ContainPermissions("system:menu:icon:update")
    public ApiResponse<?> updateById(@RequestBody UpdateSystemMenuIconDTO dto) {
        systemMenuIconBiz.updateById(dto);
        return ApiResponse.success(null);
    }
    
    @ApiOperation("分页查询")
    @PostMapping("/page")
    @ContainPermissions({"system:menu:icon:query"})
    public ApiResponse<PageData<SystemMenuIconVO>> findPage(@RequestBody PageWrap<QuerySystemMenuIconDTO> pageWrap) {
        return ApiResponse.success(systemMenuIconBiz.findPage(pageWrap));
    }

    @ApiOperation("查询所有")
    @GetMapping("/all")
    @ContainAnyPermissions({
            "system:menu:icon:query",
            "system:menu:create",
            "system:menu:update"
    })
    public ApiResponse<List<SystemMenuIcon>> findAll() {
        return ApiResponse.success(systemMenuIconService.findAll());
    }
}
