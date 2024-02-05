package com.eva.biz.system;

import com.eva.biz.system.dto.CreateSystemDictDTO;
import com.eva.biz.system.dto.UpdateSystemDictDTO;
import com.eva.core.constants.Constants;
import com.eva.core.exception.BusinessException;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.model.DictCache;
import com.eva.core.model.DictDataCache;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.core.utils.AssertUtil;
import com.eva.dao.system.SystemDictMapper;
import com.eva.dao.system.dto.QuerySystemDictDTO;
import com.eva.dao.system.model.SystemDict;
import com.eva.dao.system.model.SystemDictData;
import com.eva.dao.system.vo.SystemDictVO;
import com.eva.dao.system.vo.SystemDictWithDataVO;
import com.eva.service.common.CacheProxy;
import com.eva.service.system.SystemDictDataService;
import com.eva.service.system.SystemDictService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 字典管理业务实现
 */
@Slf4j
@Service
public class SystemDictBiz {

    @Resource
    private SystemDictService systemDictService;

    @Resource
    private SystemDictMapper systemDictMapper;

    @Resource
    private SystemDictDataService systemDictDataService;

    @Resource
    private CacheProxy<String, Map<String, DictCache>> cacheProxy;

    /**
     * 创建
     *
     * @param dto 字段信息
     * @return 主键
     */
    public Integer create(CreateSystemDictDTO dto) {
        AssertUtil.notEmpty(dto.getCode(), "字典编码不能为空");
        AssertUtil.notEmpty(dto.getName(), "字典名称不能为空");
        AssertUtil.notEmpty(dto.getScope(), "字典作用域不能为空");
        // 验证编码是否存在
        if (systemDictService.exists(new SystemDict().setCode(dto.getCode()))) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "字典编码已存在");
        }
        // 执行创建
        SystemDict newDict = new SystemDict();
        BeanUtils.copyProperties(dto, newDict);
        return systemDictService.create(newDict);
    }

    /**
     * 根据ID删除
     *
     * @param id ID
     */
    @Transactional
    public void deleteById (Integer id) {
        // 删除字典
        systemDictService.deleteById(id);
        // 查询字典数据
        SystemDictData queryDataDto = new SystemDictData();
        queryDataDto.setDictId(id);
        Set<Integer> dataIds = systemDictDataService.findIds(queryDataDto);
        systemDictDataService.deleteByIdInBatch(dataIds);
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
     * @param dto 更新后的字段信息
     */
    public void updateById(UpdateSystemDictDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "主键不能为空");
        AssertUtil.notEmpty(dto.getCode(), "字典编码不能为空");
        AssertUtil.notEmpty(dto.getName(), "字典名称不能为空");
        AssertUtil.notEmpty(dto.getScope(), "字典作用域不能为空");
        AssertUtil.notEmpty(systemDictService.findById(dto.getId()), ResponseStatus.DATA_EMPTY);
        // 验证编码是否存在
        SystemDict dict = SystemDict.builder().id(dto.getId()).code(dto.getCode()).build();
        if (systemDictService.exists(dict)) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "字典编码已存在");
        }
        // 执行修改
        SystemDict newDict = new SystemDict();
        BeanUtils.copyProperties(dto, newDict);
        systemDictService.updateById(newDict);
    }

    /**
     * 分页查询
     *
     * @param pageWrap 分页参数
     * @return 分页数据
     */
    public PageData<SystemDictVO> findPage(PageWrap<QuerySystemDictDTO> pageWrap) {
        PageHelper.startPage(pageWrap.getPage(), pageWrap.getCapacity());
        List<SystemDictVO> dictList = systemDictMapper.search(pageWrap.getModel(), pageWrap.getOrderByClause());
        return PageData.from(new PageInfo<>(dictList));
    }

    /**
     * 加载到缓存
     */
    public void loadToCache () {
        log.debug("加载字典数据至缓存");
        List<SystemDictWithDataVO> dictWithDataList = systemDictService.findListWithData();
        List<DictCache> dictList = new ArrayList<>();
        for (SystemDictWithDataVO dictWithDataVO : dictWithDataList) {
            DictCache dictCache = new DictCache();
            BeanUtils.copyProperties(dictWithDataVO, dictCache, "dataList");
            List<DictDataCache> dataList = new ArrayList<>();
            for (SystemDictData data : dictWithDataVO.getDataList()) {
                DictDataCache dictDataCache = new DictDataCache();
                BeanUtils.copyProperties(data, dictDataCache);
                dataList.add(dictDataCache);
            }
            dictCache.setDataList(dataList);
            dictList.add(dictCache);
        }
        Map<String, DictCache> dictMap = new HashMap<>();
        for (DictCache dictCache : dictList) {
            if (dictMap.containsKey(dictCache.getCode())) {
                throw new BusinessException(ResponseStatus.DATA_ERROR, "字典存在重复记录");
            }
            dictMap.put(dictCache.getCode(), dictCache);
        }
        cacheProxy.put(Constants.CacheKey.DICTIONARIES, dictMap);
        log.debug("字典数据缓存完成");
    }
}
