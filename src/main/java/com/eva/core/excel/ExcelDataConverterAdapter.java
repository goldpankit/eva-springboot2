package com.eva.core.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel数据格式处理适配器
 */
public interface ExcelDataConverterAdapter {

    /**
     * 格式化
     *
     * @param value 单元格数据
     * @param args ExcelImportColumn或ExcelExportColumn注解的args参数
     * @param cell 单元格对象
     * @param workbook 工作簿对象
     * @return String
     */
    Object convert(Object value, String[] args, Cell cell, Workbook workbook);
}
