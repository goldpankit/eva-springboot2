package com.eva.biz.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.dto.QuerySystemTraceLogDTO;
import com.eva.dao.system.model.SystemTraceLog;
import com.eva.service.system.SystemTraceLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SystemTraceLogBiz {

    @Resource
    private SystemTraceLogService systemTraceLogService;

    /**
     * 分页查询
     *
     * @param pageWrap 分页参数
     * @return 分页数据
     *
     */
    public PageData<SystemTraceLog> findPage(PageWrap<QuerySystemTraceLogDTO> pageWrap) {
        QueryWrapper<SystemTraceLog> queryWrapper = new QueryWrapper<>();
        QuerySystemTraceLogDTO dto = pageWrap.getModel();
        queryWrapper.lambda()
            .like(StringUtils.isNotBlank(dto.getUserRealName()), SystemTraceLog::getUserRealName, dto.getUserRealName())
            .like(StringUtils.isNotBlank(dto.getOperaModule()), SystemTraceLog::getOperaModule, dto.getOperaModule())
            .like(StringUtils.isNotBlank(dto.getRequestUri()), SystemTraceLog::getRequestUri, dto.getRequestUri())
            .eq(dto.getStatus() != null, SystemTraceLog::getStatus, dto.getStatus())
            .eq(dto.getExceptionLevel() != null, SystemTraceLog::getExceptionLevel, dto.getExceptionLevel())
        ;
        // 操作开始时间
        if (dto.getOperaTime() != null) {
            if (dto.getOperaTime().getStart() != null) {
                queryWrapper
                    .lambda()
                    .ge(SystemTraceLog::getOperaTime, dto.getOperaTime().getStart());
            }
            if (dto.getOperaTime().getEnd() != null) {
                queryWrapper
                    .lambda()
                    .le(SystemTraceLog::getOperaTime, dto.getOperaTime().getEnd());
            }
        }
        // 字段排序
        for(PageWrap.SortData sortData: pageWrap.getSorts()) {
            if (sortData.getDirection().equalsIgnoreCase(PageWrap.DESC)) {
                queryWrapper.orderByDesc(sortData.getProperty());
            } else {
                queryWrapper.orderByAsc(sortData.getProperty());
            }
        }
        return systemTraceLogService.findPage(pageWrap.getPage(), pageWrap.getCapacity(), queryWrapper);
    }
}
