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
@ApiModel("系统权限")
@TableName("`system_permission`")
public class SystemPermission extends BaseModel {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;

    @TableField("`code`")
    @ApiModelProperty(value = "权限编码")
    private String code;

    @TableField("`name`")
    @ApiModelProperty(value = "权限名称")
    private String name;

    @TableField("`remark`")
    @ApiModelProperty(value = "权限备注")
    private String remark;

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
