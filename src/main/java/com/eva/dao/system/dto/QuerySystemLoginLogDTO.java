package com.eva.dao.system.dto;

import com.eva.core.model.DateTimeRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询系统登录日志参数")
public class QuerySystemLoginLogDTO {

    @ApiModelProperty("登录时间")
    private DateTimeRange loginTime;

    @ApiModelProperty(value = "登录用户名")
    private String loginUsername;

    @ApiModelProperty(value = "登录IP")
    private String ip;

    @ApiModelProperty(value = "服务器IP")
    private String serverIp;

    @ApiModelProperty(value = "是否登录成功")
    private Boolean success;
}
