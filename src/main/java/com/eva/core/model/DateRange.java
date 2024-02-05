package com.eva.core.model;

import com.eva.core.utils.Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("日期范围对象")
public class DateRange {

    @JsonFormat(pattern = "yyyy/MM/dd")
    @ApiModelProperty("开始日期")
    private Date start;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @ApiModelProperty("结束日期")
    private Date end;

    /**
     * 获取开始时间，从00:00:00开始
     *
     * @return Date
     */
    public Date getStart () {
        if (this.start == null) {
            return null;
        }
        return Utils.Date.getStart(this.start);
    }

    /**
     * 获取结束时间，到23:59:59结束
     *
     * @return Date
     */
    public Date getEnd () {
        if (this.end == null) {
            return null;
        }
        return Utils.Date.getEnd(this.end);
    }
}
