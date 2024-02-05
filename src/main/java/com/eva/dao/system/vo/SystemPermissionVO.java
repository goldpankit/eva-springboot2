package com.eva.dao.system.vo;

import com.eva.dao.system.model.SystemPermission;
import com.eva.dao.system.model.SystemUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统权限列表视图")
public class SystemPermissionVO extends SystemPermission {

    @ApiModelProperty(value = "类型，module模块，permission权限")
    private String type;

    @ApiModelProperty(value = "模块路径")
    private String modulePath;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "子权限列表")
    private List<SystemPermissionVO> children;

    @ApiModelProperty(value = "创建人信息")
    private SystemUser createUserInfo;

    @ApiModelProperty(value = "更新人信息")
    private SystemUser updateUserInfo;
}
