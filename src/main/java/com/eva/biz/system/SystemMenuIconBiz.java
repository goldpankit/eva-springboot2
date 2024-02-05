package com.eva.biz.system;

import com.eva.core.constants.ResponseStatus;
import com.eva.core.utils.AssertUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.SystemMenuIconMapper;
import com.eva.dao.system.model.SystemMenuIcon;
import com.eva.dao.system.dto.QuerySystemMenuIconDTO;
import com.eva.biz.system.dto.CreateSystemMenuIconDTO;
import com.eva.biz.system.dto.UpdateSystemMenuIconDTO;
import com.eva.dao.system.vo.SystemMenuIconVO;
import com.eva.service.system.SystemMenuIconService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统图标业务实现
 */
@Service
public class SystemMenuIconBiz {

    @Resource
    private SystemMenuIconMapper systemMenuIconMapper;

    @Resource
    private SystemMenuIconService systemMenuIconService;

    /**
     * 创建
     *
     * @param dto 字段信息
     * @return 主键
     */
    @Transactional
    public Integer create(CreateSystemMenuIconDTO dto) {
        AssertUtil.notEmpty(dto.getName(), "图标名称不能为空");
        AssertUtil.notEmpty(dto.getAccessType(), "访问类型不能为空");
        // 创建系统图标
        SystemMenuIcon newRecord = new SystemMenuIcon();
        BeanUtils.copyProperties(dto, newRecord);
        return systemMenuIconService.create(newRecord);
    }

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    @Transactional
    public void deleteById(Integer id) {
        systemMenuIconService.deleteById(id);
    }

    /**
     * 批量根据主键删除
     *
     * @param ids 主键集合
     */
    @Transactional
    public void deleteByIdInBatch(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (Integer id : ids) {
            systemMenuIconService.deleteById(id);
        }
    }

    /**
     * 根据主键更新
     *
     * @param dto 更新后的字段信息
     */
    @Transactional
    public void updateById(UpdateSystemMenuIconDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "主键不能为空");
        AssertUtil.notEmpty(dto.getName(), "图标名称不能为空");
        AssertUtil.notEmpty(dto.getAccessType(), "访问类型不能为空");
        AssertUtil.notEmpty(systemMenuIconService.findById(dto.getId()), ResponseStatus.DATA_EMPTY);
        // 修改系统图标
        SystemMenuIcon newRecord = new SystemMenuIcon();
        BeanUtils.copyProperties(dto, newRecord);
        systemMenuIconService.updateById(newRecord);
    }

    /**
     * 分页查询
     *
     * @param pageWrap 分页参数
     * @return 分页数据
     */
    public PageData<SystemMenuIconVO> findPage(PageWrap<QuerySystemMenuIconDTO> pageWrap) {
        PageHelper.startPage(pageWrap.getPage(), pageWrap.getCapacity());
        List<SystemMenuIconVO> result = systemMenuIconMapper.search(pageWrap.getModel());
        return PageData.from(new PageInfo<>(result));
    }
}
