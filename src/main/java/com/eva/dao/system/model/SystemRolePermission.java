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
@ApiModel("角色权限关联")
@TableName("`system_role_permission`")
public class SystemRolePermission extends BaseModel {

    @TableField("`role_id`")
    @ApiModelProperty(value = "角色ID", example = "1")
    private Integer roleId;

    @TableField("`permission_id`")
    @ApiModelProperty(value = "权限ID", example = "1")
    private Integer permissionId;

}
