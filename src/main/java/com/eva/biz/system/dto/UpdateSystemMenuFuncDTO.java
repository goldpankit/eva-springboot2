package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("修改系统菜单功能参数")
public class UpdateSystemMenuFuncDTO extends CreateSystemMenuFuncDTO{

    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;
}
