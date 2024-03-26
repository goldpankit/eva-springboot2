package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@ApiModel("创建用户角色参数")
public class CreateUserRoleDTO implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "角色ID集")
    private Set<Integer> roleIds;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Integer createUser;
}
