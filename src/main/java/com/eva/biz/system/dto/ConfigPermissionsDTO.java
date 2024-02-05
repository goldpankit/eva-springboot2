package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("配置角色权限参数")
public class ConfigPermissionsDTO {

    @ApiModelProperty(value = "角色主键")
    private Integer roleId;

    @ApiModelProperty(value = "配置的菜单权限列表")
    private List<Integer> menuPermissionIds;

    @ApiModelProperty(value = "配置的功能权限列表")
    private List<Integer> funcPermissionIds;

    @ApiModelProperty(value = "配置的系统配置项权限列表")
    private List<Integer> systemConfigPermissionIds;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Integer createUser;
}
