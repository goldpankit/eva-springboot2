package com.eva.dao.system.vo;

import com.eva.core.secure.field.EnableSecureField;
import com.eva.core.secure.field.SecureField;
import com.eva.dao.system.model.SystemRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("系统用户列表视图")
@EnableSecureField
public class SystemUserVO implements Serializable {

    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;

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

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "是否为固定用户", hidden = true)
    private Boolean fixed;

    @ApiModelProperty(value="创建时间")
    private Date createdAt;

    @ApiModelProperty(value="更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "角色")
    private List<SystemRole> roles;

    @ApiModelProperty(value = "创建人ID")
    private Integer creatorId;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorRealName;

    @ApiModelProperty(value = "更新人ID")
    private Integer updaterId;

    @ApiModelProperty(value = "更新人姓名")
    private String updaterRealName;

}
