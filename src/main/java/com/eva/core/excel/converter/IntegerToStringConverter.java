package com.eva.core.excel.converter;

import com.eva.core.excel.ExcelDataConverterAdapter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 整数转字符串
 */
public class IntegerToStringConverter implements ExcelDataConverterAdapter {

    @Override
    public Object convert(Object value, String[] args, Cell cell, Workbook workbook) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return value;
        }
        return String.valueOf(((Double) value).longValue());
    }
}
