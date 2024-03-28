package com.eva.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 登录用户信息
 */
@Data
@ApiModel("登录用户信息")
public class LoginUserInfo implements Serializable {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("生日")
    private Date birthday;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("角色标识符集")
    private Set<String> roles;

    @ApiModelProperty("权限标识符集")
    private Set<String> permissions;

    @ApiModelProperty("是否为超级管理员")
    private Boolean isSuperAdmin;

    @JsonIgnore
    @ApiModelProperty("菜单权限主键集")
    private Set<Integer> menuPermissionIds;

    @JsonIgnore
    @ApiModelProperty("功能权限主键集")
    private Set<Integer> menuFuncPermissionIds;

    @JsonIgnore
    @ApiModelProperty("系统配置权限主键集")
    private Set<Integer> systemConfigPermissionIds;

    @JsonIgnore
    @ApiModelProperty("盐值")
    private String salt;

    @JsonIgnore
    @ApiModelProperty("密码")
    private String password;

}
