package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("修改系统字典参数")
public class UpdateSystemDictDTO extends CreateSystemDictDTO {

    @ApiModelProperty(value="主键")
    private Integer id;
}
