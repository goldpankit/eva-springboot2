package com.eva.dao.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("菜单列表视图")
public class SystemMenuVO implements Serializable {

    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;

    @ApiModelProperty(value = "上一级菜单", example = "1")
    private Integer parentId;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单类型")
    private String type;

    @ApiModelProperty(value = "菜单访问路径")
    private String uri;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "图标ID")
    private Integer iconId;

    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    @ApiModelProperty(value = "是否为固定菜单", hidden = true)
    private Boolean fixed;

    @ApiModelProperty(value="创建时间")
    private Date createdAt;

    @ApiModelProperty(value="更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "图标")
    private Icon icon;

    @ApiModelProperty(value = "权限主键")
    private Integer permissionId;

    @ApiModelProperty(value = "权限标识符")
    private String permission;

    @ApiModelProperty(value = "创建人ID")
    private Integer creatorId;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorRealName;

    @ApiModelProperty(value = "更新人ID")
    private Integer updaterId;

    @ApiModelProperty(value = "更新人姓名")
    private String updaterRealName;

    @Data
    @ApiModel("图标")
    public static class Icon {
        @ApiModelProperty(value="主键")
        private Integer id;

        @ApiModelProperty(value="图标CLASS")
        private String className;

        @ApiModelProperty(value="网络路径")
        private String uri;

        @ApiModelProperty(value="访问类型，CLASS=样式;URI=网络")
        private String accessType;
    }
}
