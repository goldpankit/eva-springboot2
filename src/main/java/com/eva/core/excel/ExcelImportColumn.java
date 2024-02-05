package com.eva.core.excel;

import java.lang.annotation.*;

/**
 * 标记为Excel导入列
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImportColumn {

    /**
     * 列索引，指定字段读取Excel中的第几列，从0开始计数
     */
    int index() default -1;

    /**
     * 列名，指定字段读取Excel中的哪一列，如果存在index，则配置不生效
     */
    String name() default "";

    /**
     * 数据转换器
     */
    Class<? extends ExcelDataConverterAdapter> converter() default ExcelDataConverterAdapter.class;

    /**
     * 自定义数据处理器参数
     */
    String[] args() default {};

}
