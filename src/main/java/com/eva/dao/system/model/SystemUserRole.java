package com.eva.dao.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eva.dao.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel("用户角色关联")
@TableName("`system_user_role`")
public class SystemUserRole extends BaseModel {

    @ApiModelProperty(value = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("`user_id`")
    @ApiModelProperty(value = "用户ID", example = "1")
    private Integer userId;

    @TableField("`role_id`")
    @ApiModelProperty(value = "角色ID", example = "1")
    private Integer roleId;

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
