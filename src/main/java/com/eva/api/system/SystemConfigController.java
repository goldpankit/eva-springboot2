package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.dto.UpdateSystemConfigDTO;
import com.eva.core.authorize.ContainPermissions;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.core.prevent.PreventRepeat;
import com.eva.dao.system.dto.QuerySystemConfigDTO;
import com.eva.biz.system.dto.CreateSystemConfigDTO;
import com.eva.dao.system.vo.SystemConfigVO;
import com.eva.biz.system.SystemConfigBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "系统配置")
@Slf4j
@RestController
@RequestMapping("/system/config")
public class SystemConfigController extends BaseController {

    @Resource
    private SystemConfigBiz systemConfigBiz;

    @PreventRepeat
    @ApiOperation("创建")
    @PostMapping("/create")
    @ContainPermissions("system:config:create")
    public ApiResponse<?> create(@RequestBody CreateSystemConfigDTO dto) {
        systemConfigBiz.create(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("根据ID删除")
    @GetMapping("/delete/{id}")
    @ContainPermissions("system:config:delete")
    public ApiResponse<?> deleteById(@PathVariable Integer id) {
        systemConfigBiz.deleteById(id);
        return ApiResponse.success(null);
    }

    @ApiOperation("批量删除")
    @GetMapping("/delete/batch")
    @ContainPermissions("system:config:delete")
    public ApiResponse<?> deleteByIdInBatch(@RequestParam String ids) {
        systemConfigBiz.deleteByIdInBatch(this.getIdList(ids));
        return ApiResponse.success(null);
    }

    @ApiOperation("根据ID修改")
    @PostMapping("/updateById")
    @ContainPermissions("system:config:update")
    public ApiResponse<?> updateById(@RequestBody UpdateSystemConfigDTO dto) {
        systemConfigBiz.updateById(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    @ContainPermissions("system:config:query")
    public ApiResponse<PageData<SystemConfigVO>> findPage(@RequestBody PageWrap<QuerySystemConfigDTO> pageWrap) {
        return ApiResponse.success(systemConfigBiz.findPage(pageWrap));
    }

    @ApiOperation("刷新缓存")
    @GetMapping("/cache/refresh")
    @ContainPermissions("system:config:query")
    public ApiResponse<?> refreshCache() {
        systemConfigBiz.loadToCache();
        return ApiResponse.success(null);
    }
}
