package com.eva.dao.system.vo;

import com.eva.dao.system.model.SystemDict;
import com.eva.dao.system.model.SystemDictData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("字典视图")
public class SystemDictWithDataVO extends SystemDict {

    @ApiModelProperty("字典数据")
    private List<SystemDictData> dataList;
}
