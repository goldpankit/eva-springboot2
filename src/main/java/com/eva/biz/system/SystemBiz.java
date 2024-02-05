package com.eva.biz.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eva.core.constants.Constants;
import com.eva.core.model.ClientConfig;
import com.eva.dao.system.SystemDictDataMapper;
import com.eva.dao.system.model.SystemConfig;
import com.eva.dao.system.model.SystemDict;
import com.eva.dao.system.model.SystemDictData;
import com.eva.service.system.SystemDictService;
import com.eva.service.system.SystemConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SystemBiz {

    @Resource
    private SystemDictService systemDictService;

    @Resource
    private SystemDictDataMapper systemDictDataMapper;

    @Resource
    private SystemConfigService systemConfigService;

    /**
     * 获取客户端配置
     *
     * @return 客户端配置
     */
    public ClientConfig getClientConfig() {
        ClientConfig config = new ClientConfig();
        config.setConfigs(this.getSystemConfigList());
        config.setDictList(this.getDictList());
        return config;
    }

    /**
     * 获取系统配置列表
     *
     * @return 系统配置列表
     */
    private List<ClientConfig.Config> getSystemConfigList () {
        SystemConfig queryDto = new SystemConfig();
        queryDto.setScope(Constants.SystemConfig.SCOPE_SERVER_AND_CLIENT);
        List<SystemConfig> configs = systemConfigService.findList(queryDto);
        List<ClientConfig.Config> configList = new ArrayList<>();
        for (SystemConfig config : configs) {
            ClientConfig.Config newConfig = new ClientConfig.Config();
            BeanUtils.copyProperties(config, newConfig);
            configList.add(newConfig);
        }
        return configList;
    }

    /**
     * 获取字典列表
     *
     * @return 系统字典列表
     */
    private List<ClientConfig.Dict> getDictList () {
        List<ClientConfig.Dict> dictList = new ArrayList<>();
        // 查询字典
        SystemDict queryDto = new SystemDict();
        // - 仅查询管理后台字典
        queryDto.setScope(Constants.SystemDict.SCOPE_BACK_END);
        List<SystemDict> systemDictList = systemDictService.findList(queryDto);
        List<Integer> dictIds = new ArrayList<>();
        for (SystemDict dict : systemDictList) {
            ClientConfig.Dict newDict = new ClientConfig.Dict();
            BeanUtils.copyProperties(dict, newDict);
            newDict.setDataList(new ArrayList<>());
            dictList.add(newDict);
            dictIds.add(dict.getId());
        }
        // 查询字典数据
        QueryWrapper<SystemDictData> queryDataWrapper = new QueryWrapper<>();
        queryDataWrapper.lambda()
                .in(SystemDictData::getDictId, dictIds)
                .eq(SystemDictData::getDeleted, Boolean.FALSE)
                .orderByAsc(SystemDictData::getSort)
        ;
        List<SystemDictData> dataList = systemDictDataMapper.selectList(queryDataWrapper);
        // 将数据按照字典id装载到字典中
        for (SystemDictData data : dataList) {
            for (ClientConfig.Dict dict : dictList) {
                if (dict.getId().equals(data.getDictId())) {
                    ClientConfig.DictData newDictData = new ClientConfig.DictData();
                    BeanUtils.copyProperties(data, newDictData);
                    dict.getDataList().add(newDictData);
                }
            }
        }
        return dictList;
    }
}
