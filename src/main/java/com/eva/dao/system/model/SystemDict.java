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
@ApiModel("字典")
@TableName("`system_dict`")
public class SystemDict extends BaseModel {

    @TableField("`code`")
    @ApiModelProperty(value = "字典编码")
    private String code;

    @TableField("`name`")
    @ApiModelProperty(value = "字典名称")
    private String name;

    @TableField("`scope`")
    @ApiModelProperty(value = "作用域")
    private String scope;

    @TableField("`remark`")
    @ApiModelProperty(value = "备注")
    private String remark;

}
