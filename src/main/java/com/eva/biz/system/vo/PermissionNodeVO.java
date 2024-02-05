package com.eva.biz.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
@ApiModel("权限节点视图")
public class PermissionNodeVO implements Serializable {

    @ApiModelProperty("权限主键")
    private Integer id;

    @ApiModelProperty("权限标识符")
    private String permission;

    @ApiModelProperty("节点类型")
    private String type;

    @ApiModelProperty("节点名称")
    private String label;

    @ApiModelProperty("子节点")
    private List<PermissionNodeVO> children;
}
