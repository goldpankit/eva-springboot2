package com.eva.core.excel.converter;

import com.eva.core.excel.ExcelDataConverterAdapter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 小数转字符串
 */
public class DoubleToStringConverter implements ExcelDataConverterAdapter {


    @Override
    public Object convert(Object value, String[] args, Cell cell, Workbook workbook) {
        if (value == null) {
            return null;
        }
        BigDecimal decimalValue = new BigDecimal(String.valueOf((value)));
        // 精度处理
        if (args.length > 0) {
            decimalValue = decimalValue.setScale(Integer.parseInt(args[0]), RoundingMode.HALF_UP);
        }
        return decimalValue.toPlainString();
    }
}
