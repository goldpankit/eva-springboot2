package com.eva.dao.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eva.core.authorize.AuthorizeExpress;
import com.eva.core.authorize.EnableFieldAuthorize;
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

import java.util.Date;

@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("跟踪日志")
@TableName("`system_trace_log`")
@EnableFieldAuthorize
public class SystemTraceLog extends BaseModel {

    @TableField("`user_id`")
    @ApiModelProperty(value = "操作人ID", example = "1")
    private Integer userId;

    @TableField("`username`")
    @ApiModelProperty(value = "操作人用户名")
    private String username;

    @TableField("`user_roles`")
    @ApiModelProperty(value = "操作时用户拥有的角色")
    private String userRoles;

    @TableField("`user_permissions`")
    @ApiModelProperty(value = "操作时用户拥有的权限")
    private String userPermissions;

    @TableField("`opera_type`")
    @ApiModelProperty(value = "操作类型")
    private String operaType;

    @TableField("`opera_module`")
    @ApiModelProperty(value = "操作模块")
    @ExcelExportColumn(name="业务模块", index = 1)
    private String operaModule;

    @TableField("`opera_remark`")
    @ApiModelProperty(value = "操作备注")
    @ExcelExportColumn(name="操作说明", index = 2, width = 5)
    private String operaRemark;

    @TableField("`request_method`")
    @ApiModelProperty(value = "请求方式")
    @ExcelExportColumn(name="请求方式", index = 3)
    private String requestMethod;

    @TableField("`request_uri`")
    @ApiModelProperty(value = "请求地址")
    @ExcelExportColumn(name="请求地址", index = 4, width = 14)
    private String requestUri;

    @TableField("`request_params`")
    @ApiModelProperty(value = "请求参数")
    @AuthorizeExpress("isSuperAdmin()")
    @ExcelExportColumn(name="请求参数", index = 5, width = 16, authorize = "isSuperAdmin()")
    private String requestParams;

    @TableField("`request_result`")
    @ApiModelProperty(value = "请求结果")
    @AuthorizeExpress("isSuperAdmin()")
    @ExcelExportColumn(name="请求结果", index = 6, width = 10, authorize = "isSuperAdmin()")
    private String requestResult;

    @TableField("`status`")
    @ApiModelProperty(value = "状态（0=操作失败;1=操作成功;-1=未处理）", example = "1")
    @ExcelExportColumn(name="状态", index = 7, valueMapping = "0=操作失败;1=操作成功;-1=未处理", width = 4)
    private Byte status;

    @TableField("`exception_level`")
    @ApiModelProperty(value = "异常等级（0=低;5=中;10=高）")
    @ExcelExportColumn(name="异常等级", index = 8, dict = "TRACE_LOG_EXCEPTION_LEVEL")
    private Byte exceptionLevel;

    @TableField("`exception_stack`")
    @ApiModelProperty(value = "异常信息")
    @ExcelExportColumn(name="异常信息", index = 9, width = 16)
    private String exceptionStack;

    @TableField("`opera_duration`")
    @ApiModelProperty(value = "耗时", example = "1")
    @ExcelExportColumn(name="耗时（ms）", index = 10)
    private Integer operaDuration;

    @TableField("`user_real_name`")
    @ApiModelProperty(value = "操作人姓名")
    @ExcelExportColumn(name="操作人", index = 11)
    private String userRealName;

    @TableField("`opera_time`")
    @ApiModelProperty(value = "操作时间")
    @ExcelExportColumn(name="操作时间", index = 12, dateFormat = "yyyy/MM/dd HH:mm:ss", width = 10)
    private Date operaTime;

    @TableField("`platform`")
    @ApiModelProperty(value = "操作平台")
    @ExcelExportColumn(name="操作平台", index = 13)
    private String platform;

    @TableField("`system_version`")
    @ApiModelProperty(value = "接口版本")
    @ExcelExportColumn(name="接口版本", index = 14)
    private String systemVersion;

    @TableField("`server_ip`")
    @ApiModelProperty(value = "服务器IP")
    @ExcelExportColumn(name="处理服务器IP", index = 15, width = 10)
    private String serverIp;

    @TableField("`ip`")
    @ApiModelProperty(value = "IP")
    @ExcelExportColumn(name="用户IP", index = 16, width = 8)
    private String ip;

    @TableField("`client_info`")
    @ApiModelProperty(value = "客户端信息")
    @ExcelExportColumn(name="用户客户端", index = 17, width = 10)
    private String clientInfo;

    @TableField("`os_info`")
    @ApiModelProperty(value = "系统信息")
    @ExcelExportColumn(name="用户操作系统", index = 18)
    private String osInfo;

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
