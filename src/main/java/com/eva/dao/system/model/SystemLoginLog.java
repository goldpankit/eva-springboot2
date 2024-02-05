package com.eva.dao.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.eva.core.excel.ExcelExportColumn;
import com.eva.dao.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Date;

@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("登录日志")
@TableName("`system_login_log`")
public class SystemLoginLog extends BaseModel {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键", example = "1")
    private Integer id;

    @TableField("`user_id`")
    @ApiModelProperty(value = "登录用户ID", example = "1")
    private Integer userId;

    @TableField("`login_username`")
    @ApiModelProperty(value = "登录用户名")
    @ExcelExportColumn(name="登录用户名", index = 1)
    private String loginUsername;

    @TableField("`ip`")
    @ApiModelProperty(value = "登录IP")
    @ExcelExportColumn(name="登录IP", index = 2, color = IndexedColors.RED, width = 8)
    private String ip;

    @TableField("`location`")
    @ApiModelProperty(value = "登录地区")
    @ExcelExportColumn(name="登录地区", index = 3, width = 10)
    private String location;

    @TableField("`client_info`")
    @ApiModelProperty(value = "客户端")
    @ExcelExportColumn(name="客户端", index = 4, width = 10)
    private String clientInfo;

    @TableField("`os_info`")
    @ApiModelProperty(value = "操作系统")
    @ExcelExportColumn(name="操作系统", index = 5)
    private String osInfo;

    @TableField("`platform`")
    @ApiModelProperty(value = "登录平台")
    @ExcelExportColumn(name="登录平台", index = 6)
    private String platform;

    @TableField("`system_version`")
    @ApiModelProperty(value = "系统版本")
    @ExcelExportColumn(name="系统版本", index = 7)
    private String systemVersion;

    @TableField("`server_ip`")
    @ApiModelProperty(value = "服务器IP")
    @ExcelExportColumn(name="服务器IP", index = 8, width = 8)
    private String serverIp;

    @TableField("`success`")
    @ApiModelProperty(value = "是否登录成功")
    @ExcelExportColumn(name="是否登录成功", index = 9, valueMapping = "true=是;false=否", align = HorizontalAlignment.CENTER)
    private Boolean success;

    @TableField("`reason`")
    @ApiModelProperty(value = "失败原因")
    @ExcelExportColumn(name="失败原因", index = 10, color = IndexedColors.RED, width = 16)
    private String reason;

    @TableField("`login_time`")
    @ApiModelProperty(value = "登录时间")
    @ExcelExportColumn(name="登录时间", index = 11, dateFormat = "yyyy/MM/dd HH:mm:ss", width = 10)
    private Date loginTime;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value="创建人")
    private Integer createdBy;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value="创建时间")
    private Date createdAt;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value="更新人")
    private Integer updatedBy;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value="更新时间")
    private Date updatedAt;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value="是否已删除")
    private Boolean deleted;

}
