package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.SystemLoginLogBiz;
import com.eva.core.excel.ExcelExporter;
import com.eva.core.authorize.ContainPermissions;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.dto.QuerySystemLoginLogDTO;
import com.eva.dao.system.model.SystemLoginLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/system/loginLog")
@Api(tags = "登录日志")
public class SystemLoginLogController extends BaseController {

    @Resource
    private SystemLoginLogBiz systemLoginLogBiz;

    @PostMapping("/page")
    @ApiOperation("分页查询")
    @ContainPermissions("system:loginLog:query")
    public ApiResponse<PageData<SystemLoginLog>> findPage (@RequestBody PageWrap<QuerySystemLoginLogDTO> pageWrap) {
        return ApiResponse.success(systemLoginLogBiz.findPage(pageWrap));
    }

    @PostMapping("/exportExcel")
    @ApiOperation("导出Excel")
    @ContainPermissions("system:loginLog:query")
    public void export (@RequestBody PageWrap<QuerySystemLoginLogDTO> pageWrap, HttpServletResponse response) {
        ExcelExporter.build(SystemLoginLog.class)
                .exportData(
                    systemLoginLogBiz.findPage(pageWrap).getRecords(),
                    "登录日志",
                    response
                );
    }
}
