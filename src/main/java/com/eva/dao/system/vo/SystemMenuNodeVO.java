package com.eva.dao.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(
    value = "菜单节点视图",
    description = "菜单管理中的树和用户可访问的菜单树均使用该对象实现"
)
public class SystemMenuNodeVO extends SystemMenuVO {

    @ApiModelProperty(value = "菜单唯一标识")
    private String index;

    @ApiModelProperty(value = "子菜单")
    private List<SystemMenuNodeVO> children;

    @ApiModelProperty(value = "是否包含子菜单")
    private Boolean hasChildren;
}
