package com.eva.biz.system.dto;

import com.eva.core.secure.field.EnableSecureField;
import com.eva.core.secure.field.SecureField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("创建用户参数")
@EnableSecureField
public class CreateSystemUserDTO {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "工号")
    private String empNo;

    @ApiModelProperty(value = "生日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date birthday;

    @ApiModelProperty(value = "性别")
    private String gender;

    @SecureField
    @ApiModelProperty(value = "邮箱")
    private String email;

    @SecureField
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "密码")
    private String password;
}
