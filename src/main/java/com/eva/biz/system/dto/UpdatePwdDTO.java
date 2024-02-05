package com.eva.biz.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("修改密码参数")
public class UpdatePwdDTO implements Serializable {

    @ApiModelProperty(value = "原始密码")
    private String oldPwd;

    @ApiModelProperty(value = "新密码")
    private String newPwd;
}
