package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("修改系统菜单状态参数")
public class UpdateSystemMenuStatusDTO {

    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;

    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;
}
