package com.eva.service.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eva.dao.system.SystemDictDataMapper;
import com.eva.dao.system.model.SystemDictData;
import com.eva.service.BaseService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemDictDataService extends BaseService<SystemDictData, SystemDictDataMapper> {

    public SystemDictDataService(SystemDictDataMapper mapper, Environment environment) {
        super(mapper, environment);
    }

    /**
     * 修改排序
     *
     * @param dictId 字典ID
     */
    @Transactional
    public void updateSortByDictId (Integer dictId) {
        this.updateSort(this.findSortedListByDictId(dictId));
    }

    /**
     * 修改排序
     *
     * @param dictDataList 数据列表
     */
    @Transactional
    public void updateSort (List<SystemDictData> dictDataList) {
        List<SystemDictData> newDictDataList = new ArrayList<>();
        // 重新设置排序
        for (int i = 0; i < dictDataList.size(); i++) {
            newDictDataList.add(SystemDictData.builder()
                    .id(dictDataList.get(i).getId())
                    .sort(i + 1)
                    .build()
            );
        }
        // 批量修改排序值
        this.updateByIdInBatch(newDictDataList);
    }

    /**
     * 根据字典ID查询字典数据（排序）
     *
     * @param dictId 字典ID
     * @return 字典数据列表
     */
    public List<SystemDictData> findSortedListByDictId (Integer dictId) {
        QueryWrapper<SystemDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SystemDictData::getDictId, dictId)
                .eq(SystemDictData::getDeleted, Boolean.FALSE)
                .orderByAsc(SystemDictData::getSort)
        ;
        return this.findList(queryWrapper);
    }

    /**
     * 获取最大排序
     *
     * @param dictId 字典ID
     * @return 最大排序
     */
    public Integer getMaxSort (Integer dictId) {
        SystemDictData queryDto = new SystemDictData();
        queryDto.setDictId(dictId);
        long sort = (int) this.count(queryDto);
        return (int) sort + 1;
    }
}
