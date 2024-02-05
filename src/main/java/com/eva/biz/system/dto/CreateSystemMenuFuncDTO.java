package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建菜单功能参数")
public class CreateSystemMenuFuncDTO {

    @ApiModelProperty(value="菜单ID")
    private Integer menuId;

    @ApiModelProperty(value="权限标识符")
    private String permission;

    @ApiModelProperty(value="功能名称")
    private String name;

    @ApiModelProperty(value="功能备注")
    private String remark;

}
