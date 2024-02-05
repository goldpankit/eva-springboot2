package com.eva.biz.system;

import com.eva.biz.system.dto.UpdateSystemConfigDTO;
import com.eva.core.constants.Constants;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.model.LoginUserInfo;
import com.eva.core.model.SystemConfigCache;
import com.eva.core.utils.AssertUtil;
import com.eva.core.utils.Utils;
import com.eva.service.common.CacheProxy;
import com.eva.service.system.SystemConfigService;
import com.eva.service.system.SystemPermissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.dao.system.SystemConfigMapper;
import com.eva.dao.system.model.SystemConfig;
import com.eva.dao.system.dto.QuerySystemConfigDTO;
import com.eva.biz.system.dto.CreateSystemConfigDTO;
import com.eva.dao.system.vo.SystemConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置业务实现
 */
@Slf4j
@Service
public class SystemConfigBiz {

    @Resource
    private SystemConfigMapper systemConfigMapper;

    @Resource
    private SystemConfigService systemConfigService;

    @Resource
    private SystemPermissionService systemPermissionService;

    @Resource
    private CacheProxy<String, Map<String, SystemConfigCache>> cacheProxy;

    /**
     * 创建
     *
     * @param dto 字段信息
     * @return 主键
     */
    @Transactional
    public Integer create(CreateSystemConfigDTO dto) {
        AssertUtil.notEmpty(dto.getCode(), "配置编码不能为空");
        AssertUtil.notEmpty(dto.getName(), "配置名称不能为空");
        AssertUtil.notEmpty(dto.getValue(), "配置值不能为空");
        AssertUtil.notEmpty(dto.getValueType(), "值类型不能为空");
        // 验证配置编码是否已存在
        if (systemConfigService.exists(new SystemConfig().setCode(dto.getCode()))) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "配置编码已存在");
        }
        // 创建系统配置
        SystemConfig newRecord = new SystemConfig();
        BeanUtils.copyProperties(dto, newRecord);
        // - 权限处理
        newRecord.setPermissionId(systemPermissionService.create(dto.getPermission(), dto.getName()));
        return systemConfigService.create(newRecord);
    }

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    @Transactional
    public void deleteById(Integer id) {
        AssertUtil.notNull(id, "主键不能为空");
        SystemConfig config = systemConfigService.findById(id);
        if (config == null) {
            return;
        }
        // 越权验证
        if (!Utils.Session.getLoginUser().getIsSuperAdmin()) {
            this.checkPrivilege(config);
        }
        // 删除权限
        systemPermissionService.deleteByIdAllowNull(config.getPermissionId());
        // 执行删除
        systemConfigService.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 主键集合
     */
    @Transactional
    public void deleteByIdInBatch(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    /**
     * 根据主键更新
     *
     * @param dto 更新后的字段信息
     */
    @Transactional
    public void updateById(UpdateSystemConfigDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "主键不能为空");
        AssertUtil.notEmpty(dto.getCode(), "配置编码不能为空");
        AssertUtil.notEmpty(dto.getName(), "配置名称不能为空");
        AssertUtil.notEmpty(dto.getValue(), "配置值不能为空");
        AssertUtil.notEmpty(dto.getValueType(), "值类型不能为空");
        SystemConfig config = systemConfigService.findById(dto.getId());
        AssertUtil.notNull(config, ResponseStatus.DATA_EMPTY);
        // 越权验证
        if (!Utils.Session.getLoginUser().getIsSuperAdmin()) {
            this.checkPrivilege(config);
        }
        // 验证编码是否已存在
        SystemConfig existsConfig = SystemConfig.builder().id(dto.getId()).code(dto.getCode()).build();
        if (systemConfigService.exists(existsConfig)) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "配置编码已存在");
        }
        // 修改系统配置
        SystemConfig newRecord = new SystemConfig();
        BeanUtils.copyProperties(dto, newRecord);
        // - 权限处理
        newRecord.setPermissionId(systemPermissionService.sync(
                config.getPermissionId(),
                dto.getPermission(),
                dto.getName())
        );
        systemConfigService.updateById(newRecord);
    }

    /**
     * 分页查询
     *
     * @param pageWrap 分页参数
     * @return 分页数据
     */
    public PageData<SystemConfigVO> findPage(PageWrap<QuerySystemConfigDTO> pageWrap) {
        PageHelper.startPage(pageWrap.getPage(), pageWrap.getCapacity());
        // 获取当前登录用户信息
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        pageWrap.getModel().setPermissionIds(userInfo.getSystemConfigPermissionIds());
        pageWrap.getModel().setIsSuperAdmin(userInfo.getIsSuperAdmin());
        pageWrap.getModel().setLoginUserId(userInfo.getId());
        List<SystemConfigVO> result = systemConfigMapper.search(pageWrap.getModel());
        return PageData.from(new PageInfo<>(result));
    }

    /**
     * 加载到缓存
     */
    public void loadToCache () {
        log.debug("加载系统配置至缓存");
        List<SystemConfig> configs = systemConfigService.findAll();
        List<SystemConfigCache> configCaches = new ArrayList<>();
        for (SystemConfig config : configs) {
            SystemConfigCache configCache = new SystemConfigCache();
            BeanUtils.copyProperties(config, configCache);
            configCaches.add(configCache);
        }
        Map<String, SystemConfigCache> configMap = new HashMap<>();
        for (SystemConfigCache configCache : configCaches) {
            configMap.put(configCache.getCode(), configCache);
        }
        cacheProxy.put(Constants.CacheKey.SYSTEM_CONFIGS, configMap);
        log.debug("系统配置缓存完成");
    }

    /**
     * 越权验证
     *
     * @param config 系统配置
     */
    private void checkPrivilege (SystemConfig config) {
        if (config.getPermissionId() != null) {
            if (!Utils.Session.getLoginUser()
                    .getSystemConfigPermissionIds()
                    .contains(config.getPermissionId())
            ) {
                throw new BusinessException(ResponseStatus.PRIVILEGE_ERROR);
            }
        }
    }
}
