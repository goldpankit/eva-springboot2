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
@ApiModel("字典数据")
@TableName("`system_dict_data`")
public class SystemDictData extends BaseModel {

    @TableField("`dict_id`")
    @ApiModelProperty(value = "所属字典", example = "1")
    private Integer dictId;

    @TableField("`value`")
    @ApiModelProperty(value = "数据值")
    private String value;

    @TableField("`label`")
    @ApiModelProperty(value = "数据标签")
    private String label;

    @TableField("`config`")
    @ApiModelProperty(value = "其它配置")
    private String config;

    @TableField("`sort`")
    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    @TableField("`disabled`")
    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;

    @TableField("`remark`")
    @ApiModelProperty(value = "备注")
    private String remark;

}
