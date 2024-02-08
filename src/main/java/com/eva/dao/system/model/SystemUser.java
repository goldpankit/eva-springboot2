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

import java.util.Date;

@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统用户")
@TableName("`system_user`")
public class SystemUser extends BaseModel {

    @TableField("`username`")
    @ApiModelProperty(value = "用户名")
    private String username;

    @TableField("`real_name`")
    @ApiModelProperty(value = "姓名")
    private String realName;

    @TableField("`emp_no`")
    @ApiModelProperty(value = "工号")
    private String empNo;

    @TableField("`birthday`")
    @ApiModelProperty(value = "生日")
    private Date birthday;

    @TableField("`gender`")
    @ApiModelProperty(value = "性别")
    private String gender;

    @TableField("`email`")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @TableField("`email_digest`")
    @ApiModelProperty(value = "邮箱摘要")
    private String emailDigest;

    @TableField("`mobile`")
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @TableField("`mobile_digest`")
    @ApiModelProperty(value = "手机号码摘要")
    private String mobileDigest;

    @TableField("`avatar`")
    @ApiModelProperty(value = "头像")
    private String avatar;

    @TableField("`password`")
    @ApiModelProperty(value = "密码")
    private String password;

    @TableField("`salt`")
    @ApiModelProperty(value = "盐")
    private String salt;

}
