package com.eva.core.excel;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.*;

/**
 * 标记为Excel导出列
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExportColumn {

    /**
     * 列名
     */
    String name();

    /**
     * 列宽（单位为字符），-1按字段反射顺序排序
     */
    int width() default -1;

    /**
     * 到处列的排序，值越小越靠前
     */
    int index();

    /**
     * 对齐方式
     */
    HorizontalAlignment align() default HorizontalAlignment.LEFT;

    /**
     * 列头背景色
     */
    IndexedColors backgroundColor() default IndexedColors.GREY_25_PERCENT;

    /**
     * 数据单元格的背景色
     */
    IndexedColors dataBackgroundColor() default IndexedColors.WHITE;

    /**
     * 字体颜色
     */
    IndexedColors color () default IndexedColors.BLACK;

    /**
     * 字体大小（像素）
     */
    short fontSize () default 12;

    /**
     * 是否加粗
     */
    boolean bold () default false;

    /**
     * 是否倾斜
     */
    boolean italic () default false;

    /**
     * 值映射，如0=女;1=男
     */
    String valueMapping() default "";

    /**
     * 数据前缀
     */
    String prefix() default "";

    /**
     * 数据后缀
     */
    String suffix() default "";

    /**
     * 日期格式，只有数据为java.util.Date时才生效
     */
    String dateFormat() default "yyyy/MM/dd";

    /**
     * 数据格式
     */
    String format() default "";

    /**
     * 字典编码
     */
    String dict () default "";

    /**
     * 数据转换器
     */
    Class<? extends ExcelDataConverterAdapter> converter() default ExcelDataConverterAdapter.class;

    /**
     * 数据转换器参数
     */
    String[] args() default {};

    /**
     * 权限表达式
     */
    String authorize() default "";

}
