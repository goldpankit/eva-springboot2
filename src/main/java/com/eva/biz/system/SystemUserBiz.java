package com.eva.biz.system;

import com.eva.biz.system.dto.*;
import com.eva.core.exception.BusinessException;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.model.LoginUserInfo;
import com.eva.core.model.PageData;
import com.eva.core.model.PageWrap;
import com.eva.core.utils.AssertUtil;
import com.eva.core.utils.Utils;
import com.eva.dao.system.SystemUserMapper;
import com.eva.dao.system.dto.*;
import com.eva.dao.system.model.SystemRole;
import com.eva.dao.system.model.SystemUser;
import com.eva.dao.system.model.SystemUserRole;
import com.eva.dao.system.vo.SystemUserVO;
import com.eva.service.system.SystemRoleService;
import com.eva.service.system.SystemUserRoleService;
import com.eva.service.system.SystemUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemUserBiz {

    @Resource
    private SystemUserService systemUserService;

    @Resource
    private SystemUserRoleService systemUserRoleService;

    @Resource
    private SystemUserMapper systemUserMapper;

    @Resource
    private SystemRoleService systemRoleService;

    /**
     * 创建用户
     *
     * @param dto 创建用户参数
     */
    @Transactional
    public void create(CreateSystemUserDTO dto) {
        AssertUtil.notEmpty(dto.getUsername(), "用户名不能为空");
        AssertUtil.notEmpty(dto.getPassword(), "密码不能为空");
        AssertUtil.notEmpty(dto.getRealName(), "姓名不能为空");
        AssertUtil.notEmpty(dto.getGender(), "性别不能为空");
        // 验证手机号码格式
        if (StringUtils.isNotBlank(dto.getMobile())) {
            AssertUtil.isPhoneNumber(Utils.Secure.decryptField(dto.getMobile()));
        }
        // 验证邮箱格式
        if (StringUtils.isNotBlank(dto.getEmail())) {
            AssertUtil.isEmail(Utils.Secure.decryptField(dto.getEmail()));
        }
        // 验证用户名是否已存在
        if (systemUserService.exists(new SystemUser().setUsername(dto.getUsername()))) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "用户名已存在");
        }
        // 验证工号是否已存在
        if (StringUtils.isNotBlank(dto.getEmpNo())) {
            if (systemUserService.exists(new SystemUser().setEmpNo(dto.getEmpNo()))) {
                throw new BusinessException(ResponseStatus.DATA_EXISTS, "工号已存在");
            }
        }
        // 验证手机号码是否已存在
        if (StringUtils.isNotBlank(dto.getMobile())) {
            if (systemUserService.exists(new SystemUser().setMobile(dto.getMobile()))) {
                throw new BusinessException(ResponseStatus.DATA_EXISTS, "手机号已存在");
            }
        }
        // 验证邮箱是否已存在
        if (StringUtils.isNotBlank(dto.getEmail())) {
            if (systemUserService.exists(new SystemUser().setEmail(dto.getEmail()))) {
                throw new BusinessException(ResponseStatus.DATA_EXISTS, "邮箱已存在");
            }
        }
        // 执行创建
        SystemUser newUser = new SystemUser();
        BeanUtils.copyProperties(dto, newUser);
        // - 生成密码盐
        String salt = RandomStringUtils.randomAlphabetic(6);
        // - 生成密码
        newUser.setPassword(Utils.Secure.encryptPassword(dto.getPassword(), salt));
        newUser.setSalt(salt);
        // - 设置手机号码摘要
        if (StringUtils.isNotBlank(dto.getMobile())) {
            String mobile = Utils.Secure.decryptField(dto.getMobile());
            newUser.setMobileDigest(Utils.Digest.digestMobile(mobile));
        }
        // - 设置邮箱摘要
        if (StringUtils.isNotBlank(dto.getEmail())) {
            String email = Utils.Secure.decryptField(dto.getEmail());
            newUser.setEmailDigest(Utils.Digest.digestEmail(email));
        }
        systemUserService.create(newUser);
    }

    /**
     * 创建用户角色关联
     *
     * @param dto 创建用户角色关联参数
     */
    @Transactional
    public void createUserRole(CreateUserRoleDTO dto) {
        // 越权验证
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        if (!userInfo.getIsSuperAdmin()) {
            // 非超级管理员不可配置超级管理员角色
            List<SystemRole> roles = systemRoleService.findByIds(dto.getRoleIds());
            for (SystemRole role : roles) {
                if (role.getCode().equals(Utils.AppConfig.getSuperAdminRole())) {
                    throw new BusinessException(ResponseStatus.NOT_ALLOWED, "不可配置超级管理员角色");
                }
            }
            // 非超级管理员不可为自己配置角色
            if (userInfo.getId().equals(dto.getUserId())) {
                throw new BusinessException(ResponseStatus.NOT_ALLOWED, "不可为自己配置角色");
            }
        }
        // 删除关联角色
        SystemUserRole deleteDto = new SystemUserRole();
        deleteDto.setUserId(dto.getUserId());
        systemUserRoleService.delete(deleteDto);
        // 新增新的角色
        for (Integer roleId : dto.getRoleIds()) {
            SystemUserRole newUserRole = new SystemUserRole();
            newUserRole.setUserId(dto.getUserId());
            newUserRole.setRoleId(roleId);
            systemUserRoleService.create(newUserRole);
        }
    }

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    @Transactional
    public void deleteById(Integer id) {
        AssertUtil.notEmpty(id, "主键不能为空");
        SystemUser user = systemUserService.findById(id);
        if (user == null) {
            return;
        }
        // 不可删除自己
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        if (userInfo.getId().equals(id)) {
            throw new BusinessException(ResponseStatus.NOT_ALLOWED, "请勿删除自己");
        }
        // 非超级管理员不可删除超级管理员
        if (!userInfo.getRoles().contains(Utils.AppConfig.getSuperAdminRole())) {
            List<SystemRole> roles = systemRoleService.findByUserId(user.getId());
            roles.stream().findFirst().ifPresent(role -> {
                if (role.getCode().equals(Utils.AppConfig.getSuperAdminRole())) {
                    throw new BusinessException(ResponseStatus.NOT_ALLOWED, "您无权删除超级管理员用户");
                }
            });
        }
        systemUserService.deleteById(id);
    }

    /**
     * 根据主键批量删除
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
     * 修改密码
     *
     * @param dto 修改密码参数
     */
    public void updatePwd(UpdatePwdDTO dto) {
        AssertUtil.notEmpty(dto.getOldPwd(), "原始密码不能为空");
        AssertUtil.notEmpty(dto.getNewPwd(), "新密码不能为空");
        LoginUserInfo userInfo = Utils.Session.getLoginUser();
        SystemUser user = systemUserService.findById(userInfo.getId());
        AssertUtil.notEmpty(user, ResponseStatus.DATA_EMPTY);
        // 验证原始密码
        if (!user.getPassword().equals(Utils.Secure.encryptPassword(dto.getOldPwd(), user.getSalt()))) {
            throw new BusinessException(ResponseStatus.PWD_INCORRECT, "原始密码不正确");
        }
        // 修改密码
        SystemUser newUser = new SystemUser();
        newUser.setId(userInfo.getId());
        newUser.setPassword(Utils.Secure.encryptPassword(dto.getNewPwd(), user.getSalt()));
        systemUserService.updateById(newUser);
    }

    /**
     * 重置密码
     *
     * @param dto 重置密码参数
     */
    public void resetPwd(ResetSystemUserPwdDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "用户主键不能为空");
        AssertUtil.notEmpty(dto.getPassword(), "密码不能为空");
        AssertUtil.notEmpty(dto.getOperaUserId(), "操作人不能为空");
        SystemUser systemUser = systemUserService.findById(dto.getId());
        AssertUtil.notEmpty(systemUser, ResponseStatus.DATA_EMPTY);
        // 修改密码
        SystemUser updateUserDto = new SystemUser();
        updateUserDto.setId(dto.getId());
        updateUserDto.setPassword(Utils.Secure.encryptPassword(dto.getPassword(), systemUser.getSalt()));
        systemUserService.updateById(updateUserDto);
    }

    /**
     * 修改用户
     *
     * @param dto 修改用户参数
     */
    public void updateById(UpdateSystemUserDTO dto) {
        AssertUtil.notEmpty(dto.getId(), "主键不能为空");
        AssertUtil.notEmpty(dto.getUsername(), "用户名不能为空");
        AssertUtil.notEmpty(dto.getRealName(), "姓名不能为空");
        AssertUtil.notEmpty(dto.getGender(), "性别不能为空");
        // 验证手机号码格式
        if (StringUtils.isNotBlank(dto.getMobile())) {
            AssertUtil.isPhoneNumber(Utils.Secure.decryptField(dto.getMobile()));
        }
        // 验证邮箱格式
        if (StringUtils.isNotBlank(dto.getEmail())) {
            AssertUtil.isEmail(Utils.Secure.decryptField(dto.getEmail()));
        }
        // 验证用户名是否已存在
        SystemUser user = SystemUser.builder().id(dto.getId()).username(dto.getUsername()).build();
        if (systemUserService.exists(user)) {
            throw new BusinessException(ResponseStatus.DATA_EXISTS, "用户名已存在");
        }
        // 验证工号是否已存在
        if (StringUtils.isNotBlank(dto.getEmpNo())) {
            user = SystemUser.builder().id(dto.getId()).empNo(dto.getEmpNo()).build();
            if (systemUserService.exists(user)) {
                throw new BusinessException(ResponseStatus.DATA_EXISTS, "工号已存在");
            }
        }
        // 验证手机号码是否已存在
        if (StringUtils.isNotBlank(dto.getMobile())) {
            user = SystemUser.builder().id(dto.getId()).mobile(dto.getMobile()).build();
            if (systemUserService.exists(user)) {
                throw new BusinessException(ResponseStatus.DATA_EXISTS, "手机号已存在");
            }
        }
        // 验证邮箱是否已存在
        if (StringUtils.isNotBlank(dto.getMobile())) {
            user = SystemUser.builder().id(dto.getId()).email(dto.getEmail()).build();
            if (systemUserService.exists(user)) {
                throw new BusinessException(ResponseStatus.DATA_EXISTS, "邮箱已存在");
            }
        }
        // 修改用户
        SystemUser newUser = SystemUser.builder().build();
        BeanUtils.copyProperties(dto, newUser);
        // - 处理手机号码摘要
        newUser.setMobileDigest("");
        if (StringUtils.isNotBlank(dto.getMobile())) {
            String mobile = Utils.Secure.decryptField(dto.getMobile());
            newUser.setMobileDigest(Utils.Digest.digestMobile(mobile));
        }
        // - 处理邮箱摘要
        newUser.setEmailDigest("");
        if (StringUtils.isNotBlank(dto.getEmail())) {
            String email = Utils.Secure.decryptField(dto.getEmail());
            newUser.setEmailDigest(Utils.Digest.digestEmail(email));
        }
        systemUserService.updateById(newUser);
    }

    /**
     * 分页查询
     *
     * @param pageWrap 分页参数
     * @return 用户列表
     */
    public PageData<SystemUserVO> findPage(PageWrap<QuerySystemUserDTO> pageWrap) {
        // 执行查询
        PageHelper.startPage(pageWrap.getPage(), pageWrap.getCapacity());
        List<SystemUserVO> userList = systemUserMapper.search(pageWrap.getModel(), pageWrap.getOrderByClause());
        for (SystemUserVO user : userList) {
            // 查询用户角色列表
            user.setRoles(systemRoleService.findByUserId(user.getId()));
        }
        return PageData.from(new PageInfo<>(userList));
    }
}
