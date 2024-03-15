package com.eva.dao.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel("查询菜单功能参数")
public class QuerySystemMenuFuncDTO {

    @ApiModelProperty(value="用户ID", hidden = true)
    private Integer userId;

    @ApiModelProperty(value="菜单ID")
    private Integer menuId;

    @ApiModelProperty(value = "用户菜单权限ID集", hidden = true)
    private Set<Integer> permissionIds;

    @ApiModelProperty(value = "是否为超级管理员", hidden = true)
    private Boolean isSuperAdmin;

}
