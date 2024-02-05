package com.eva.dao.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.eva.dao.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统菜单")
@TableName("`system_menu`")
public class SystemMenu extends BaseModel {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;

    @TableField(value = "parent_id", updateStrategy = FieldStrategy.ALWAYS)
    @ApiModelProperty(value = "上一级菜单", example = "1")
    private Integer parentId;

    @TableField(value = "permission_id", updateStrategy = FieldStrategy.ALWAYS)
    @ApiModelProperty(value = "权限ID", example = "1")
    private Integer permissionId;

    @TableField("`name`")
    @ApiModelProperty(value = "菜单名称")
    private String name;

    @TableField("`type`")
    @ApiModelProperty(value = "菜单类型")
    private String type;

    @TableField("`uri`")
    @ApiModelProperty(value = "访问路径")
    private String uri;

    @TableField(value = "`icon_id`", updateStrategy = FieldStrategy.ALWAYS)
    @ApiModelProperty(value = "图标ID")
    private Integer iconId;

    @TableField("`remark`")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField("`disabled`")
    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;

    @TableField("`sort`")
    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    @TableField("`created_by`")
    @ApiModelProperty(value="创建人")
    private Integer createdBy;

    @TableField("`created_at`")
    @ApiModelProperty(value="创建时间")
    private Date createdAt;

    @TableField("`updated_by`")
    @ApiModelProperty(value="更新人")
    private Integer updatedBy;

    @TableField("`updated_at`")
    @ApiModelProperty(value="更新时间")
    private Date updatedAt;

    @TableField("`deleted`")
    @ApiModelProperty(value="是否已删除")
    private Boolean deleted;

}
