package com.eva.dao.system.model;

import com.eva.dao.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@ApiModel("系统菜单图标")
@TableName("`system_menu_icon`")
public class SystemMenuIcon extends BaseModel {

    @TableField("`name`")
    @ApiModelProperty(value="图标名称")
    private String name;

    @TableField("`class_name`")
    @ApiModelProperty(value="图标CLASS")
    private String className;

    @TableField("`uri`")
    @ApiModelProperty(value="网络路径")
    private String uri;

    @TableField("`access_type`")
    @ApiModelProperty(value="访问类型，CLASS=样式;URI=网络")
    private String accessType;

}
