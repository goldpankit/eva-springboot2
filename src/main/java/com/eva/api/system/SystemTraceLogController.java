package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.SystemTraceLogBiz;
import com.eva.core.authorize.EnableFieldAuthorize;
import com.eva.core.excel.ExcelExporter;
import com.eva.core.authorize.ContainPermissions;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.dto.QuerySystemTraceLogDTO;
import com.eva.dao.system.model.SystemTraceLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "跟踪日志")
@RestController
@RequestMapping("/system/traceLog")
public class SystemTraceLogController extends BaseController {

    @Resource
    private SystemTraceLogBiz systemTraceLogBiz;

    @ApiOperation("分页查询")
    @PostMapping("/page")
    @ContainPermissions("system:traceLog:query")
    @EnableFieldAuthorize
    public ApiResponse<PageData<SystemTraceLog>> findPage (@RequestBody PageWrap<QuerySystemTraceLogDTO> pageWrap) {
        return ApiResponse.success(systemTraceLogBiz.findPage(pageWrap));
    }

    @ApiOperation("导出Excel")
    @PostMapping("/exportExcel")
    @ContainPermissions("system:traceLog:query")
    public void exportExcel (@RequestBody PageWrap<QuerySystemTraceLogDTO> pageWrap, HttpServletResponse response) {
        pageWrap.setPage(1);
        // 查询日志
        List<SystemTraceLog> logs = systemTraceLogBiz.findPage(pageWrap).getRecords();
        // 移除第一条日志（当前导出动作产生的日志不纳入导出数据中）
        if (logs.size() > 0 && "/system/traceLog/exportExcel".equals(logs.get(0).getRequestUri())) {
            logs.remove(0);
        }
        // 执行导出
        ExcelExporter.build(SystemTraceLog.class)
                .exportData(logs, "操作日志", response);
    }
}
