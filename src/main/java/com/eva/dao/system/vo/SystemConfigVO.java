package com.eva.dao.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel("系统配置视图")
public class SystemConfigVO {

    @ApiModelProperty(value="主键")
    private Integer id;

    @ApiModelProperty(value="权限ID")
    private Integer permissionId;

    @ApiModelProperty(value="权限标识符")
    private String permission;

    @ApiModelProperty(value="配置编码")
    private String code;

    @ApiModelProperty(value="配置名称")
    private String name;

    @ApiModelProperty(value="配置值")
    private String value;

    @ApiModelProperty(value="值类型")
    private String valueType;

    @ApiModelProperty(value="作用域")
    private String scope;

    @ApiModelProperty(value="备注")
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
