package com.eva.dao.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询菜单图标参数")
public class QuerySystemMenuIconDTO {

    @ApiModelProperty(value="图标名称")
    private String name;

}
