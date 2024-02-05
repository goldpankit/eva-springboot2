package com.eva.dao.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("字典数据列表视图")
public class SystemDictDataVO implements Serializable {

    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;

    @ApiModelProperty(value = "所属字典", example = "1")
    private Integer dictId;

    @ApiModelProperty(value = "数据值")
    private String value;

    @ApiModelProperty(value = "数据标签")
    private String label;

    @ApiModelProperty(value = "特殊配置")
    private String config;

    @TableField("`SORT`")
    private Integer sort;

    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value="创建时间")
    private Date createdAt;

    @ApiModelProperty(value="更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "创建人ID")
    private Integer creatorId;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorRealName;

    @ApiModelProperty(value = "更新人ID")
    private Integer updaterId;

    @ApiModelProperty(value = "更新人姓名")
    private String updaterRealName;
}
