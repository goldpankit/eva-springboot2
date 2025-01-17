package com.eva.dao.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("查询字典数据参数")
public class QuerySystemDictDataDTO implements Serializable {

    @ApiModelProperty(value = "字典ID")
    private Integer dictId;
}
