package com.eva.dao.system.dto;

import com.eva.core.model.DateTimeRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询跟踪日志参数")
public class QuerySystemTraceLogDTO {

    @ApiModelProperty(value = "操作人姓名")
    private String userRealName;

    @ApiModelProperty(value = "操作模块")
    private String operaModule;

    @ApiModelProperty(value = "请求地址")
    private String requestUri;

    @ApiModelProperty(value = "状态（0操作失败，1操作成功，-1未得到处理）", example = "1")
    private Byte status;

    @ApiModelProperty(value = "异常等级")
    private Byte exceptionLevel;

    @ApiModelProperty("操作时间")
    private DateTimeRange operaTime;
}
