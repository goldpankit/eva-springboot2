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
@ApiModel("系统配置")
@TableName("`system_config`")
public class SystemConfig extends BaseModel {

    @TableField("`permission_id`")
    @ApiModelProperty(value="权限ID")
    private Integer permissionId;

    @TableField("`code`")
    @ApiModelProperty(value="配置编码")
    private String code;

    @TableField("`name`")
    @ApiModelProperty(value="配置名称")
    private String name;

    @TableField("`value`")
    @ApiModelProperty(value="配置值")
    private String value;

    @TableField("`value_type`")
    @ApiModelProperty(value="值类型")
    private String valueType;

    @TableField("`scope`")
    @ApiModelProperty(value="作用域")
    private String scope;

    @TableField("`remark`")
    @ApiModelProperty(value="备注")
    private String remark;
}
