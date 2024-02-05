package com.eva.service.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("下载本地文件参数")
public class DownloadLocalFileDTO {

    @ApiModelProperty("文件全路径")
    private String path;

    @ApiModelProperty("下载后的文件名称")
    private String name;
}
