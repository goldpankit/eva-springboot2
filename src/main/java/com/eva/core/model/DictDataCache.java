package com.eva.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("字典数据缓存对象")
public class DictDataCache implements Serializable {

    @ApiModelProperty(value = "数据ID")
    private Integer id;

    @ApiModelProperty(value = "数据值")
    private String value;

    @ApiModelProperty(value = "数据标签")
    private String label;

    @ApiModelProperty(value = "特殊配置")
    private String config;
}
