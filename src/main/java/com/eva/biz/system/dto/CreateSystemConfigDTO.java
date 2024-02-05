package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建系统配置参数")
public class CreateSystemConfigDTO {

    @ApiModelProperty(value="配置编码")
    private String code;

    @ApiModelProperty(value="配置名称")
    private String name;

    @ApiModelProperty(value="配置值")
    private String value;

    @ApiModelProperty(value="值类型")
    private String valueType;

    @ApiModelProperty(value="作用域")
    private String scope;

    @ApiModelProperty(value="权限标识符")
    private String permission;

    @ApiModelProperty(value="备注")
    private String remark;
}
