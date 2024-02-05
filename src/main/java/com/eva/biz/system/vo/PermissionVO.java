package com.eva.biz.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("权限视图")
public class PermissionVO implements Serializable {

    @ApiModelProperty("菜单和功能权限节点")
    private List<PermissionNodeVO> menuNodes;

    @ApiModelProperty("系统配置项权限节点")
    private List<PermissionNodeVO> configNodes;
}
