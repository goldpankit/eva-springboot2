package com.eva.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("字典缓存对象")
public class DictCache implements Serializable {

    @ApiModelProperty(value = "字典ID")
    private Integer id;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典数据列表")
    private List<DictDataCache> dataList;
}
