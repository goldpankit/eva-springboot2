package com.eva.biz.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.SystemLoginLogMapper;
import com.eva.dao.system.dto.QuerySystemLoginLogDTO;
import com.eva.dao.system.model.SystemLoginLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SystemLoginLogBiz {

    @Resource
    private SystemLoginLogMapper systemLoginLogMapper;

    /**
     * 分页查询
     *
     * @param pageWrap 分页参数
     * @return 分页数据
     */
    public PageData<SystemLoginLog> findPage(PageWrap<QuerySystemLoginLogDTO> pageWrap) {
        IPage<SystemLoginLog> page = new Page<>(pageWrap.getPage(), pageWrap.getCapacity());
        QueryWrapper<SystemLoginLog> queryWrapper = new QueryWrapper<>();
        QuerySystemLoginLogDTO dto = pageWrap.getModel();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(dto.getLoginUsername()), SystemLoginLog::getLoginUsername, dto.getLoginUsername())
                .eq(StringUtils.isNotBlank(dto.getIp()), SystemLoginLog::getIp, dto.getIp())
                .eq(StringUtils.isNotBlank(dto.getServerIp()), SystemLoginLog::getServerIp, dto.getServerIp())
                .eq(dto.getSuccess() != null, SystemLoginLog::getSuccess, dto.getSuccess())
        ;
        // 登录开始时间
        if (dto.getLoginTime() != null) {
            if (dto.getLoginTime().getStart() != null) {
                queryWrapper.lambda()
                        .ge(SystemLoginLog::getLoginTime, dto.getLoginTime().getStart());
            }
            if (dto.getLoginTime().getEnd() != null) {
                queryWrapper.lambda()
                        .le(SystemLoginLog::getLoginTime, dto.getLoginTime().getEnd());
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
        return PageData.from(systemLoginLogMapper.selectPage(page, queryWrapper));
    }

}
