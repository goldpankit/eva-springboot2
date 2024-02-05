package com.eva.dao.system.vo;

import com.eva.dao.system.model.SystemPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("系统角色列表视图")
public class SystemRoleVO implements Serializable {

    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;

    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色备注")
    private String remark;

    @ApiModelProperty(value = "是否为固定角色", hidden = true)
    private Boolean fixed;

    @ApiModelProperty(value="创建时间")
    private Date createdAt;

    @ApiModelProperty(value="更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "角色拥有的权限列表")
    private List<SystemPermission> permissions;

    @ApiModelProperty(value = "创建人ID")
    private Integer creatorId;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorRealName;

    @ApiModelProperty(value = "更新人ID")
    private Integer updaterId;

    @ApiModelProperty(value = "更新人姓名")
    private String updaterRealName;
}
