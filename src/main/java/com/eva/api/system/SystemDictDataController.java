package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.SystemDictDataBiz;
import com.eva.biz.system.dto.CreateSystemDictDataDTO;
import com.eva.biz.system.dto.UpdateSystemDictDataDTO;
import com.eva.core.prevent.PreventRepeat;
import com.eva.core.trace.Trace;
import com.eva.core.authorize.ContainPermissions;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.biz.common.UpdateSortDTO;
import com.eva.dao.system.dto.QuerySystemDictDataDTO;
import com.eva.dao.system.vo.SystemDictDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "字典数据")
@RestController
@RequestMapping("/system/dictData")
public class SystemDictDataController extends BaseController {

    @Resource
    private SystemDictDataBiz systemDictDataBiz;

    @PreventRepeat
    @ApiOperation("新建")
    @PostMapping("/create")
    @ContainPermissions("system:dict:data:create")
    public ApiResponse<?> create(@RequestBody CreateSystemDictDataDTO dto) {
        return ApiResponse.success(systemDictDataBiz.create(dto));
    }

    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    @ContainPermissions("system:dict:data:delete")
    public ApiResponse<?> deleteById(@PathVariable Integer id) {
        systemDictDataBiz.deleteById(id);
        return ApiResponse.success(null);
    }

    @ApiOperation("批量删除")
    @GetMapping("/delete/batch")
    @ContainPermissions("system:dict:data:delete")
    public ApiResponse<?> deleteByIdInBatch(@RequestParam String ids) {
        systemDictDataBiz.deleteByIdInBatch(this.getIdList(ids));
        return ApiResponse.success(null);
    }

    @ApiOperation("修改")
    @PostMapping("/updateById")
    @ContainPermissions("system:dict:data:update")
    public ApiResponse<?> updateById(@RequestBody UpdateSystemDictDataDTO dto) {
        systemDictDataBiz.updateById(dto);
        return ApiResponse.success(null);
    }

    @Trace
    @ApiOperation("排序")
    @PostMapping("/sort")
    @ContainPermissions("system:dict:data:sort")
    public ApiResponse<?> sort(@RequestBody UpdateSortDTO dto) {
        systemDictDataBiz.updateSort(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    @ContainPermissions("system:dict:data:query")
    public ApiResponse<PageData<SystemDictDataVO>> findPage (@RequestBody PageWrap<QuerySystemDictDataDTO> pageWrap) {
        return ApiResponse.success(systemDictDataBiz.findPage(pageWrap));
    }
}
