package com.eva.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("日期时间范围对象")
public class DateTimeRange {

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private Date start;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private Date end;
}
