package com.eva.core.model;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页请求参数
 */
@Data
@ApiModel("分页请求参数")
public class PageWrap<M> implements Serializable {

    // 降序
    public static final String DESC = "DESC";

    // 升序
    public static final String ASC = "ASC";

    @ApiModelProperty("条件参数")
    private M model;

    @ApiModelProperty(value = "目标页", example = "1")
    private int page;

    @ApiModelProperty(value = "一页多少行", example = "10")
    private int capacity;

    @ApiModelProperty(value = "排序参数", example = "[]")
    private List<SortData> sorts;

    /**
     * 获取排序列表
     */
    public List<SortData> getSorts () {
        List<SortData> sorts = new ArrayList<>();
        if (this.sorts == null) {
            return sorts;
        }
        for (SortData sort: this.sorts) {
            if (sort.getProperty() == null || sort.getProperty().trim().length() == 0) {
                continue;
            }
            if (sort.getDirection() == null || sort.getDirection().trim().length() == 0 || (!sort.getDirection().trim().equalsIgnoreCase("asc") && !sort.getDirection().trim().equalsIgnoreCase("desc"))) {
                continue;
            }
            sorts.add(sort);
        }
        return sorts;
    }

    /**
     * 获取页码
     */
    public int getPage () {
        return page <= 0 ? 1 : page;
    }

    /**
     * 获取页容量
     */
    public int getCapacity () {
        return capacity <= 0 ? 10 : capacity;
    }

    /**
     * 获取排序字符串
     */
    @ApiModelProperty(hidden = true)
    public String getOrderByClause () {
        List<SortData> sorts = this.getSorts();
        StringBuilder stringBuilder = new StringBuilder();
        for (SortData sortData: sorts) {
            // 防注入
            if (!sortData.getProperty().matches("[a-zA-Z0-9_\\.]+")) {
                continue;
            }
            stringBuilder.append(sortData.getProperty().trim());
            stringBuilder.append(" ");
            stringBuilder.append(sortData.getDirection().trim());
            stringBuilder.append(",");
        }
        if (stringBuilder.length() == 0) {
            return null;
        }
        return "ORDER BY " + stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    @Data
    @ApiModel("排序对象")
    public static class SortData {

        @ApiModelProperty("排序字段")
        private String property;

        @ApiModelProperty("排序方向（ASC：升序，DESC：降序）")
        private String direction;
    }
}
