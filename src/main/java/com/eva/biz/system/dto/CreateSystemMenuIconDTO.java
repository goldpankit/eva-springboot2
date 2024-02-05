package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建系统菜单图标参数")
public class CreateSystemMenuIconDTO {

    @ApiModelProperty(value="图标名称")
    private String name;

    @ApiModelProperty(value="图标CLASS")
    private String className;

    @ApiModelProperty(value="网络路径")
    private String uri;

    @ApiModelProperty(value="访问类型，CLASS=样式;URI=网络")
    private String accessType;

}
