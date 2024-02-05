package com.eva.dao.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@ApiModel("系统角色")
@TableName("`system_role`")
public class SystemRole extends BaseModel {

    @TableField("`code`")
    @ApiModelProperty(value = "角色编码")
    private String code;

    @TableField("`name`")
    @ApiModelProperty(value = "角色名称")
    private String name;

    @TableField("`remark`")
    @ApiModelProperty(value = "角色备注")
    private String remark;

    @TableField("`fixed`")
    @ApiModelProperty(value = "是否为固定角色", hidden = true)
    private Boolean fixed;

}
