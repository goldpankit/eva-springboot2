package com.eva.dao.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel("菜单图标视图")
public class SystemMenuIconVO {

    @ApiModelProperty(value="主键")
    private Integer id;

    @ApiModelProperty(value="图标名称")
    private String name;

    @ApiModelProperty(value="图标CLASS")
    private String className;

    @ApiModelProperty(value="文件路径")
    private String filePath;

    @ApiModelProperty(value="网络路径")
    private String uri;

    @ApiModelProperty(value="访问类型，CLASS=样式;FILE_PATH=文件;URI=网络")
    private String accessType;

    @ApiModelProperty(value="创建时间")
    private Date createdAt;

    @ApiModelProperty(value="更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "创建人ID")
    private Integer creatorId;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorRealName;

    @ApiModelProperty(value = "更新人ID")
    private Integer updaterId;

    @ApiModelProperty(value = "更新人姓名")
    private String updaterRealName;

}
