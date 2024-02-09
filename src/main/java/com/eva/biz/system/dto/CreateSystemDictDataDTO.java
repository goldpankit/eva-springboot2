package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新增字典数据参数")
public class CreateSystemDictDataDTO {

    @ApiModelProperty(value = "所属字典", example = "1")
    private Integer dictId;

    @ApiModelProperty(value = "数据值")
    private String value;

    @ApiModelProperty(value = "数据标签")
    private String label;

    @ApiModelProperty(value = "其它配置")
    private String config;

    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "备注")
    private String remark;
}
