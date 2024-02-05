package com.eva.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(
        value = "客户端配置",
        description = "用于客户端（前端）一次性获取系统的配置信息，方便在前端做配置判断和转换"
)
public class ClientConfig {

    @ApiModelProperty("字典列表")
    private List<Dict> dictList;

    @ApiModelProperty("系统配置列表")
    private List<Config> configs;

    @Data
    @ApiModel("系统配置")
    public static class Config {

        @ApiModelProperty("配置编码")
        private String code;

        @ApiModelProperty("配置值")
        private String value;
    }

    @Data
    @ApiModel("字典")
    public static class Dict {

        @ApiModelProperty("字典ID")
        private Integer id;

        @ApiModelProperty("字典编码")
        private String code;

        @ApiModelProperty("字典名称")
        private String name;

        @ApiModelProperty("数据列表")
        private List<DictData> dataList;
    }

    @Data
    public static class DictData {

        @ApiModelProperty("数据值")
        private String value;

        @ApiModelProperty("数据名称")
        private String label;

        @ApiModelProperty("是否已禁用")
        private Boolean disabled;
    }
}
