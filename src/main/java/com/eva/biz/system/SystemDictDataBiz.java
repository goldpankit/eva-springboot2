package com.eva.biz.system;

import com.eva.biz.system.dto.CreateSystemDictDataDTO;
import com.eva.biz.system.dto.UpdateSystemDictDataDTO;
import com.eva.core.exception.BusinessException;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.core.utils.AssertUtil;
import com.eva.biz.common.UpdateSortDTO;
import com.eva.dao.system.SystemDictDataMapper;
import com.eva.dao.system.dto.QuerySystemDictDataDTO;
import com.eva.dao.system.model.SystemDictData;
import com.eva.dao.system.vo.SystemDictDataVO;
import com.eva.service.system.SystemDictDataService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典数据业务实现
 */
@Slf4j
@Service
public class SystemDictDataBiz {

    @Resource
    private SystemDictDataService systemDictDataService;

    @Resource
    private SystemDictDataMapper systemDictDataMapper;

    /**
     * 创建
     *
     * @param dto 字段信息
     * @return 主键
     */
    public Integer create(CreateSystemDictDataDTO dto) {
        AssertUtil.notEmpty(dto.getDictId(), "字典ID不能为空");
        AssertUtil.notEmpty(dto.getValue(), "数据值不能为空");
        AssertUtil.notEmpty(dto.getLabel(), "数据标签不能为空");
        // 验证数据值是否已存在
        SystemDictData dictData = SystemDictData.builder().dictId(dto.getDictId()).value(dto.getValue()).build();
        if (systemDictDataService.exists(dictData)) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "数据值已存在");
        }
        // 执行创建
        SystemDictData newDictData = new SystemDictData();
        BeanUtils.copyProperties(dto, newDictData);
        newDictData.setSort(systemDictDataService.getMaxSort(dto.getDictId()));
        return systemDictDataService.create(newDictData);
    }

    /**
     * 根据ID删除
     *
     * @param id ID
     */
    @Transactional
    public void deleteById (Integer id) {
        AssertUtil.notNull(id, "字典数据ID不能为空");
        SystemDictData dictData = systemDictDataService.findById(id);
        if (dictData == null) {
            return;
        }
        AssertUtil.notEmpty(dictData.getDictId(), ResponseStatus.DATA_ERROR);
        // 执行删除
        systemDictDataService.deleteById(id);
        // 调整顺序
        systemDictDataService.updateSortByDictId(dictData.getDictId());
    }

    /**
     * 根据ID集批量删除
     *
     * @param ids ID集
     */
    @Transactional
    public void deleteByIdInBatch (List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    /**
     * 根据主键修改
     *
     * @param dto 修改后的字段信息
     */
    public void updateById(UpdateSystemDictDataDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "字典数据ID不能为空");
        AssertUtil.notEmpty(dto.getValue(), "数据值不能为空");
        AssertUtil.notEmpty(dto.getLabel(), "数据标签不能为空");
        // 验证记录是否存在
        SystemDictData dictData = systemDictDataService.findById(dto.getId());
        AssertUtil.notEmpty(dictData, ResponseStatus.DATA_EMPTY);
        // 验证值是否重复
        dictData = SystemDictData.builder()
                .id(dto.getId())
                .dictId(dictData.getDictId())
                .value(dto.getValue())
                .build();
        if (systemDictDataService.exists(dictData)) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "数据值已存在");
        }
        // 执行修改
        SystemDictData newDictData = new SystemDictData();
        BeanUtils.copyProperties(dto, newDictData);
        systemDictDataService.updateById(newDictData);
    }

    /**
     * 修改排序
     *
     * @param dto 排序参数
     */
    public void updateSort(UpdateSortDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "字典数据ID不能为空");
        AssertUtil.notEmpty(dto.getTargetId(), "目标字典数据ID不能为空");
        AssertUtil.notEquals(dto.getId(), dto.getTargetId(), "字典数据ID和目标字典数据ID不能相同");
        SystemDictData currentDictData = systemDictDataService.findById(dto.getId());
        SystemDictData targetDictData = systemDictDataService.findById(dto.getTargetId());
        AssertUtil.notEmpty(currentDictData, ResponseStatus.DATA_EMPTY);
        AssertUtil.notEmpty(targetDictData, ResponseStatus.DATA_EMPTY);
        // 获取待排序的字典数据列表
        List<SystemDictData> dictDataList = systemDictDataService.findSortedListByDictId(currentDictData.getDictId());
        // 获取当前数据排序值
        int currentIndex = dictDataList.indexOf(currentDictData);
        int targetIndex = dictDataList.indexOf(targetDictData);
        if (currentIndex == -1 || targetIndex == -1) {
            log.error("数据移动失败：移动的数据和目标数据不在同一个字典中");
            throw new BusinessException(ResponseStatus.DATA_EMPTY, "排序的数据不存在，请刷新后重试");
        }
        // 调整顺序
        dictDataList.set(currentIndex, targetDictData);
        dictDataList.set(targetIndex, currentDictData);
        // 批量修改排序值
        systemDictDataService.updateSort(dictDataList);
    }

    /**
     * 分页查询
     *
     * @param pageWrap 分页参数
     * @return 分页数据
     */
    public PageData<SystemDictDataVO> findPage(PageWrap<QuerySystemDictDataDTO> pageWrap) {
        PageHelper.startPage(pageWrap.getPage(), pageWrap.getCapacity());
        return PageData.from(new PageInfo<>(systemDictDataMapper.search(pageWrap.getModel())));
    }
}
