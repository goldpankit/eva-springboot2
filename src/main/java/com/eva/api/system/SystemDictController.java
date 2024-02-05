package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.SystemDictBiz;
import com.eva.biz.system.dto.CreateSystemDictDTO;
import com.eva.biz.system.dto.UpdateSystemDictDTO;
import com.eva.core.prevent.PreventRepeat;
import com.eva.core.authorize.ContainPermissions;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.dto.QuerySystemDictDTO;
import com.eva.dao.system.vo.SystemDictVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "系统字典")
@RestController
@RequestMapping("/system/dict")
public class SystemDictController extends BaseController {

    @Resource
    private SystemDictBiz systemDictBiz;

    @PreventRepeat
    @ApiOperation("新建")
    @PostMapping("/create")
    @ContainPermissions("system:dict:create")
    public ApiResponse<Integer> create(@RequestBody CreateSystemDictDTO dto) {
        return ApiResponse.success(systemDictBiz.create(dto));
    }

    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    @ContainPermissions("system:dict:delete")
    public ApiResponse<?> deleteById(@PathVariable Integer id) {
        systemDictBiz.deleteById(id);
        return ApiResponse.success(null);
    }

    @ApiOperation("批量删除")
    @GetMapping("/delete/batch")
    @ContainPermissions("system:dict:delete")
    public ApiResponse<?> deleteByIdInBatch(@RequestParam String ids) {
        systemDictBiz.deleteByIdInBatch(this.getIdList(ids));
        return ApiResponse.success(null);
    }

    @ApiOperation("修改")
    @PostMapping("/updateById")
    @ContainPermissions("system:dict:update")
    public ApiResponse<?> updateById(@RequestBody UpdateSystemDictDTO dto) {
        systemDictBiz.updateById(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    @ContainPermissions("system:dict:query")
    public ApiResponse<PageData<SystemDictVO>> findPage (@RequestBody PageWrap<QuerySystemDictDTO> pageWrap) {
        return ApiResponse.success(systemDictBiz.findPage(pageWrap));
    }

    @ApiOperation("刷新缓存")
    @GetMapping("/cache/refresh")
    @ContainPermissions("system:dict:query")
    public ApiResponse<?> refreshCache() {
        systemDictBiz.loadToCache();
        return ApiResponse.success(null);
    }
}
