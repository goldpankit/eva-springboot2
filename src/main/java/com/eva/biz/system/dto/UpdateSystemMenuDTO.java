package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("创建系统菜单参数")
public class UpdateSystemMenuDTO extends CreateSystemMenuDTO {

    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;
}
