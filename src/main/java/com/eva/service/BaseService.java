package com.eva.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eva.core.model.PageData;
import com.eva.dao.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service实现类基类
 *
 * @param <Model> 实体类型
 */
@Slf4j
public class BaseService<Model extends BaseModel, Mapper extends BaseMapper<Model>> {

    protected final Environment environment;

    protected final Mapper mapper;

    private final Class<Model> modelClass;

    protected BaseService(Mapper mapper, Environment environment) {
        this.mapper = mapper;
        this.environment = environment;
        this.modelClass = (Class<Model>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 创建
     *
     * @param model 实体对象
     * @return 主键
     */
    public Integer create(Model model) {
        log.debug("创建 {}，请求参数: {}", this.getModuleName(), JSON.toJSONString(model));
        mapper.insert(model);
        return model.getId();
    }

    /**
     * 批量创建
     *
     * @param models 实体对象集合
     * @return 主键集合
     */
    @Transactional
    public List<Integer> createInBatch(Collection<Model> models) {
        log.debug("批量创建 {} 开始", this.getModuleName());
        List<Integer> ids = new ArrayList<>();
        for (Model model : models) {
            ids.add(this.create(model));
        }
        log.debug("批量创建 {} 完成，共创建 {} 条记录", this.getModuleName(), ids.size());
        return ids;
    }

    /**
     * 根据主键逻辑删除
     *
     * @param id 主键
     * @return 影响行数
     */
    public int deleteById(Integer id) {
        return this.deleteById(id, Boolean.FALSE);
    }

    /**
     * 根据主键物理删除
     *
     * @param id 主键
     * @param isPhysical 是否物理删除
     * @return 影响行数
     */
    public int deleteById(Integer id, boolean isPhysical) {
        log.debug("{}删除 {}，请求参数: {}", isPhysical ? "物理" : "逻辑", this.getModuleName(), id);
        if (isPhysical) {
            return mapper.deleteById(id);
        }
        Model model = this.buildModel();
        model.setId(id);
        model.setDeleted(Boolean.TRUE);
        return mapper.updateById(model);
    }

    /**
     * 批量根据主键逻辑删除
     *
     * @param ids 主键集合
     * @return 影响行数
     */
    @Transactional
    public int deleteByIdInBatch(Collection<Integer> ids) {
        return this.deleteByIdInBatch(ids, Boolean.FALSE);
    }

    /**
     * 批量根据主键物理删除
     *
     * @param ids 主键集合
     * @param isPhysical 是否物理删除
     * @return 影响行数
     */
    @Transactional
    public int deleteByIdInBatch(Collection<Integer> ids, boolean isPhysical) {
        log.debug("批量{}删除 {}，请求参数: {}", isPhysical ? "物理" : "逻辑", this.getModuleName(), JSON.toJSONString(ids));
        ids = new ArrayList<>(ids);
        ids.removeIf(id -> id == null);
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        if (isPhysical) {
            return mapper.deleteBatchIds(ids);
        }
        int total = 0;
        for (Integer id : ids) {
            total += this.deleteById(id);
        }
        return total;
    }

    /**
     * 指定条件删除
     *
     * @param model 实体对象
     * @return 影响行数
     */
    @Transactional
    public int delete (Model model) {
        return this.delete(model, Boolean.FALSE);
    }

    /**
     * 指定条件删除
     *
     * @param model 实体对象
     * @param isPhysical 是否物理删除
     * @return 影响行数
     */
    @Transactional
    public int delete (Model model, boolean isPhysical) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>(model);
        if (isPhysical) {
            return mapper.delete(queryWrapper);
        }
        List<Integer> deleteIds = new ArrayList<>();
        this.findList(queryWrapper).forEach(item -> {
            deleteIds.add(item.getId());
        });
        return this.deleteByIdInBatch(deleteIds);
    }

    /**
     * 根据条件删除
     *
     * @param deleteWrapper 删除条件
     * @return 影响行数
     */
    @Transactional
    public int delete (QueryWrapper<Model> deleteWrapper) {
        return this.delete(deleteWrapper, Boolean.FALSE);
    }

    /**
     * 根据条件删除
     *
     * @param deleteWrapper 删除条件
     * @param isPhysical 是否物理删除
     * @return 影响行数
     */
    @Transactional
    public int delete (QueryWrapper<Model> deleteWrapper, boolean isPhysical) {
        deleteWrapper.setEntityClass(modelClass);
        if (isPhysical) {
            return mapper.delete(deleteWrapper);
        }
        Model model = this.buildModel();
        model.setDeleted(Boolean.TRUE);
        return mapper.update(model, deleteWrapper);
    }

    /**
     * 根据主键更新
     *
     * @param model 实体对象
     * @return 影响行数
     */
    public int updateById(Model model) {
        log.debug("修改 {}，请求参数: {}", this.getModuleName(), JSON.toJSONString(model));
        return mapper.updateById(model);
    }

    /**
     * 批量根据主键更新
     *
     * @param models 实体对象集合
     * @return 影响行数
     */
    @Transactional
    public int updateByIdInBatch(Collection<Model> models) {
        log.debug("批量修改 {} 开始", this.getModuleName());
        if (CollectionUtils.isEmpty(models)) {
            return 0;
        }
        int total = 0;
        for (Model model : models) {
            total += this.updateById(model);
        }
        log.debug("批量修改 {} 完成，共修改 {} 条记录", this.getModuleName(), total);
        return total;
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return Model
     */
    public Model findById(Integer id) {
        return this.findById(id, Boolean.FALSE);
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @param withDeleted 是否包含已删除的数据
     * @return 实体对象
     */
    public Model findById(Integer id, boolean withDeleted) {
        Model model = mapper.selectById(id);
        if (withDeleted) {
            return model;
        }
        if (model != null && model.getDeleted()) {
            return null;
        }
        return model;
    }

    /**
     * 根据主键集合查询
     *
     * @param ids 主键集合
     * @return 实体对象集合
     */
    public List<Model> findByIds(Set<Integer> ids) {
        return this.findByIds(ids, Boolean.FALSE);
    }

    /**
     * 根据主键集合查询
     *
     * @param ids 主键集合
     * @param withDeleted 是否包含已删除的数据
     * @return 实体对象集合
     */
    public List<Model> findByIds(Set<Integer> ids, boolean withDeleted) {
        LambdaQueryWrapper<Model> queryWrapper = new LambdaQueryWrapper<>(modelClass);
        queryWrapper.in(Model::getId, ids);
        if (!withDeleted) {
            queryWrapper.eq(Model::getDeleted, Boolean.FALSE);
        }
        return mapper.selectList(queryWrapper);
    }

    /**
     * 查询主键集合
     *
     * @param model 实体对象
     * @return 主键集合
     */
    public Set<Integer> findIds (Model model) {
        return this.findIds(model, Boolean.FALSE);
    }

    /**
     * 查询主键集合
     *
     * @param model 实体对象
     * @param withDeleted 是否包含已删除的数据
     * @return 主键集合
     */
    public Set<Integer> findIds (Model model, Boolean withDeleted) {
        if (!withDeleted) {
            model.setDeleted(Boolean.FALSE);
        }
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>(model);
        return this.findIds(queryWrapper);
    }

    /**
     * 查询主键集合
     *
     * @param queryWrapper 查询条件
     * @return 主键集合
     */
    public Set<Integer> findIds (QueryWrapper<Model> queryWrapper) {
        queryWrapper.setEntityClass(modelClass);
        queryWrapper.lambda().select(Model::getId);
        return new LinkedHashSet<>(mapper.selectList(queryWrapper)
                .stream()
                .map(item -> item.getId())
                .collect(Collectors.toList()));
    }

    /**
     * 查询单条记录
     *
     * @param model 实体对象
     * @return 实体对象
     */
    public Model findOne(Model model) {
        return this.findOne(model, Boolean.FALSE);
    }

    /**
     * 查询单条记录
     *
     * @param model 实体对象
     * @param withDeleted 是否包含已删除的数据
     * @return 实体对象
     */
    public Model findOne(Model model, Boolean withDeleted) {
        if (!withDeleted) {
            model.setDeleted(Boolean.FALSE);
        }
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>(model);
        return this.findOne(queryWrapper);
    }

    /**
     * 查询单条记录
     *
     * @param queryWrapper 查询条件
     * @return 实体对象
     */
    public Model findOne(QueryWrapper<Model> queryWrapper) {
        queryWrapper.setEntityClass(this.modelClass);
        return mapper.selectOne(queryWrapper);
    }

    /**
     * 查询首条记录
     *
     * @param model 实体对象
     * @return 实体对象
     */
    public Model findFirst(Model model) {
        return this.findFirst(model, Boolean.FALSE);
    }

    /**
     * 查询首条记录
     *
     * @param model 实体对象
     * @param withDeleted 是否包含已删除的数据
     * @return 实体对象
     */
    public Model findFirst(Model model, boolean withDeleted) {
        if (!withDeleted) {
            model.setDeleted(Boolean.FALSE);
        }
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>(model);
        return this.findFirst(queryWrapper);
    }

    /**
     * 查询首条记录
     *
     * @param queryWrapper 查询条件
     * @return 实体对象
     */
    public Model findFirst(QueryWrapper<Model> queryWrapper) {
        queryWrapper.setEntityClass(this.modelClass);
        List<Model> models = mapper.selectList(queryWrapper);
        if (models.isEmpty()) {
            return null;
        }
        return models.get(0);
    }

    /**
     * 查询列表
     *
     * @param model 实体对象
     * @return 实体对象集合
     */
    public List<Model> findList(Model model) {
        return this.findList(model, Boolean.FALSE);
    }

    /**
     * 查询列表
     *
     * @param model 实体对象
     * @param withDeleted 是否包含已被逻辑删除的数据
     * @return 实体对象集合
     */
    public List<Model> findList(Model model, boolean withDeleted) {
        if (!withDeleted) {
            model.setDeleted(Boolean.FALSE);
        }
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>(model);
        return this.findList(queryWrapper);
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 查询条件
     * @return 实体对象集合
     */
    public List<Model> findList (QueryWrapper<Model> queryWrapper) {
        queryWrapper.setEntityClass(this.modelClass);
        return mapper.selectList(queryWrapper);
    }

    /**
     * 查询所有
     *
     * @return 实体对象集合
     */
    public List<Model> findAll () {
        return this.findAll(Boolean.FALSE);
    }

    /**
     * 查询所有
     *
     * @param withDeleted 是否包含已被逻辑删除的数据
     * @return 实体对象集合
     */
    public List<Model> findAll (Boolean withDeleted) {
        Model model = this.buildModel();
        return this.findList(model, withDeleted);
    }

    /**
     * 查询分页
     *
     * @param pageIndex 页码
     * @param capacity 页容量
     * @param model 实体对象
     * @return 分页数据对象
     */
    public PageData<Model> findPage (int pageIndex, int capacity, Model model) {
        return this.findPage(pageIndex, capacity, model, Boolean.FALSE);
    }

    /**
     * 查询分页
     *
     * @param pageIndex 页码
     * @param capacity 页容量
     * @param model 实体对象
     * @param withDeleted 是否包含已被逻辑删除的数据
     * @return 分页数据对象
     */
    public PageData<Model> findPage (int pageIndex, int capacity, Model model, boolean withDeleted) {
        if (!withDeleted) {
            model.setDeleted(Boolean.FALSE);
        }
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>(model);
        return this.findPage(pageIndex, capacity, queryWrapper);
    }

    /**
     * 查询分页
     *
     * @param pageIndex 页码
     * @param capacity 页容量
     * @param queryWrapper 查询条件
     * @return 分页数据对象
     */
    public PageData<Model> findPage (int pageIndex, int capacity, QueryWrapper<Model> queryWrapper) {
        queryWrapper.setEntityClass(this.modelClass);
        IPage<Model> page = new Page<>(pageIndex, capacity);
        return PageData.from(mapper.selectPage(page, queryWrapper));
    }

    /**
     * 统计总数
     *
     * @param model 实体对象
     * @return 总条数
     */
    public long count(Model model) {
        return this.count(model, Boolean.FALSE);
    }

    /**
     * 统计总数
     *
     * @param model 实体对象
     * @param withDeleted 是否包含已被逻辑删除的数据
     * @return 总条数
     */
    public long count(Model model, boolean withDeleted) {
        if (!withDeleted) {
            model.setDeleted(Boolean.FALSE);
        }
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>(model);
        return this.count(queryWrapper);
    }

    /**
     * 统计总数
     *
     * @param queryWrapper 统计条件
     * @return 总条数
     */
    public long count(QueryWrapper<Model> queryWrapper) {
        queryWrapper.setEntityClass(this.modelClass);
        return mapper.selectCount(queryWrapper);
    }

    /**
     * 判断记录是否已存在
     *
     * @param model 实体对象
     * @return boolean
     */
    public boolean exists (Model model) {
        return this.exists(model, Boolean.FALSE);
    }

    /**
     * 判断记录是否已存在
     * 判断逻辑，分为两种情况：
     *   未携带id字段：根据给定字段查询是否存在记录
     *   携带id字段：去掉id后查询是否存在记录，如果不存在则返回false，如果存在记录且id与给定的id一致，视为不存在（同一条记录），否则视为已存在
     *
     * @param model 实体对象
     * @param withDeleted 是否包含已被逻辑删除的数据
     * @return boolean
     */
    public boolean exists (Model model, boolean withDeleted) {
        Integer id = model.getId();
        if (!withDeleted) {
            model.setDeleted(Boolean.FALSE);
        }
        // 查询记录
        // - 去掉主键后再查询
        model.setId(null);
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>(model);
        List<Model> records = this.findList(queryWrapper);
        // - 没有记录，直接返回false
        if (records.isEmpty()) {
            return Boolean.FALSE;
        }
        // - 存在1条记录以上，直接返回true
        if (records.size() > 1) {
            return Boolean.TRUE;
        }
        // - 只有1条记录的情况，没有id，直接返回true
        if (id == null) {
            return Boolean.TRUE;
        }
        // - 只有1条记录的情况，有id，判断id是否相等，如果相等，则说明是同一条记录，视为不存在
        Model record = records.get(0);
        return !id.equals(record.getId());
    }

    /**
     * 构建实体对象
     *
     * @return 实体对象
     */
    private Model buildModel () {
        try {
            return modelClass.newInstance();
        } catch (Exception e) {
            log.error("构建实体对象失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取模块名称
     *
     * @return 模块名称
     */
    private String getModuleName () {
        ApiModel apiModel = modelClass.getAnnotation(ApiModel.class);
        if (apiModel == null) {
            return modelClass.getSimpleName();
        }
        return apiModel.value();
    }
}
