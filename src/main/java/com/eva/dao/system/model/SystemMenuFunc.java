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

@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("菜单功能")
@TableName("`system_menu_func`")
public class SystemMenuFunc extends BaseModel {

    @TableField("`menu_id`")
    @ApiModelProperty(value="菜单ID")
    private Integer menuId;

    @TableField(value = "`permission_id`", updateStrategy= FieldStrategy.ALWAYS)
    @ApiModelProperty(value="权限ID")
    private Integer permissionId;

    @TableField("`name`")
    @ApiModelProperty(value="功能名称")
    private String name;

    @TableField("`remark`")
    @ApiModelProperty(value="功能备注")
    private String remark;

}
