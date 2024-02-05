package com.eva.dao.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel("查询系统配置参数")
public class QuerySystemConfigDTO {

    @ApiModelProperty(value = "备注")
    private String keyword;

    @ApiModelProperty(value = "用户菜单功能权限ID集", hidden = true)
    private Set<Integer> permissionIds;

    @ApiModelProperty(value = "当前登录用户ID")
    private Integer loginUserId;

    @ApiModelProperty(value = "是否为超级管理员", hidden = true)
    private Boolean isSuperAdmin;

}
