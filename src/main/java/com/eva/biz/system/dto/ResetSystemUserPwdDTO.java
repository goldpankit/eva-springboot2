package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("重置用户密码参数")
public class ResetSystemUserPwdDTO implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private Integer id;

    @ApiModelProperty(value = "新密码")
    private String password;

    @ApiModelProperty(value = "操作人", hidden = true)
    private Integer operaUserId;
}
