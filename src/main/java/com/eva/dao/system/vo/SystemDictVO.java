package com.eva.dao.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("字典列表视图")
public class SystemDictVO implements Serializable {

    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "作用域")
    private String scope;

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
