package com.eva.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("系统配置缓存对象")
public class SystemConfigCache implements Serializable {

    @ApiModelProperty(value="主键")
    private Integer id;

    @ApiModelProperty(value="配置编码")
    private String code;

    @ApiModelProperty(value="配置值")
    private String value;
}
