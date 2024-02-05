package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建系统菜单参数")
public class CreateSystemMenuDTO {

    @ApiModelProperty(value = "上一级菜单", example = "1")
    private Integer parentId;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单类型")
    private String type;

    @ApiModelProperty(value = "访问路径")
    private String uri;

    @ApiModelProperty(value = "权限标识符")
    private String permission;

    @ApiModelProperty(value = "图标ID")
    private Integer iconId;

    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "备注")
    private String remark;
}
