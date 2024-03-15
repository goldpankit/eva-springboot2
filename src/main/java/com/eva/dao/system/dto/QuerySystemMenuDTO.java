package com.eva.dao.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@ApiModel("查询用户菜单参数")
public class QuerySystemMenuDTO implements Serializable {

    @ApiModelProperty(value = "用户ID", hidden = true)
    private Integer userId;

    @ApiModelProperty(value = "用户菜单权限ID集", hidden = true)
    private Set<Integer> permissionIds;

    @ApiModelProperty(value = "是否为超级管理员", hidden = true)
    private Boolean isSuperAdmin;
}
