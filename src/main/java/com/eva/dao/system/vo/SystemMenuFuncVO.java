package com.eva.dao.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel("菜单功能视图")
public class SystemMenuFuncVO {

    @ApiModelProperty(value="主键")
    private Integer id;

    @ApiModelProperty(value="菜单ID")
    private Integer menuId;

    @ApiModelProperty(value="权限ID")
    private Integer permissionId;

    @ApiModelProperty(value="权限标识符")
    private String permission;

    @ApiModelProperty(value="功能名称")
    private String name;

    @ApiModelProperty(value="功能备注")
    private String remark;

    @ApiModelProperty(value="创建时间")
    private Date createdAt;

    @ApiModelProperty(value="更新时间")
    private Date updatedAt;

    @ApiModelProperty(value="主键")
    private Integer creatorId;

    @ApiModelProperty(value="真实姓名")
    private String creatorRealName;

    @ApiModelProperty(value="主键")
    private Integer updaterId;

    @ApiModelProperty(value="真实姓名")
    private String updaterRealName;

}
