package com.eva.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@ApiModel("基础实体")
public class BaseModel implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Integer id;

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
