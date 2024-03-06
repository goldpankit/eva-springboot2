package com.eva.core.excel;

import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.annotation.AnnotationConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Excel导入实现
 */
public class ExcelImporter<T> {

    private Class<T> modelClass;

    private ExcelImporter () {}

    /**
     * 构造ExcelImporter对象
     *
     * @param modelClass 实体Class对象
     * @return ExcelImporter实例
     */
    public static <T> ExcelImporter<T> build(Class<T> modelClass) {
        ExcelImporter<T> excelImporter = new ExcelImporter<>();
        excelImporter.modelClass = modelClass;
        return excelImporter;
    }

    /**
     * 导入数据
     *
     * @param is 输入流
     * @param callback 回调
     * @param sync 是否同步已存在数据
     * @return 导入成功数
     */
    public int importData (InputStream is, ExcelImportCallback<T> callback, boolean sync) {
        return this.importData(is, 0, callback, sync);
    }

    /**
     * 导入数据
     *
     * @param is 输入流
     * @param sheetIndex sheet坐标
     * @param callback 回调
     * @param sync 是否同步已存在数据
     * @return 导入成功数
     */
    public int importData (InputStream is, int sheetIndex, ExcelImportCallback<T> callback, boolean sync) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            // 获取列信息
            List<ColumnInfo> columns = this.getColumns(sheet);
            List<T> data = new ArrayList<>();
            // 循环获取excel行记录
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                // 构造数据实例对象
                T instance = modelClass.newInstance();
                Row row = sheet.getRow(i);
                // 循环获取单元格信息
                for (ColumnInfo columnInfo : columns) {
                    Cell cell = row.getCell(columnInfo.getIndex());
                    if (cell == null) {
                        continue;
                    }
                    if (StringUtils.isBlank(cell.toString())) {
                        continue;
                    }
                    // 写入对象属性
                    columnInfo.getField().setAccessible(Boolean.TRUE);
                    columnInfo.getField().set(instance, this.getCellValue(cell, columnInfo, workbook));
                    columnInfo.getField().setAccessible(Boolean.FALSE);
                }
                // 如果是空行则结束行读取
                if (this.isEmptyRow(instance)) {
                    break;
                }
                data.add(instance);
            }
            // 执行回调函数
            return callback.callback(data, sync);
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.IMPORT_EXCEL_ERROR, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取Cell值
     *
     * @param cell 单元格对象
     * @param columnInfo 列配置
     * @param workbook 工作簿对象
     * @return 单元格值
     */
    private Object getCellValue (Cell cell, ColumnInfo columnInfo, Workbook workbook) {
        CellType cellType = cell.getCellType();
        Type fieldType = columnInfo.getField().getGenericType();
        Object value = null;
        if (fieldType.getTypeName().equals("java.util.Date")) {
            value = cell.getDateCellValue();
        } else if (cellType.equals(CellType.NUMERIC)) {
            value = cell.getNumericCellValue();
        } else if (cellType.equals(CellType.STRING)) {
            value = cell.getStringCellValue();
        } else if (cellType.equals(CellType.BOOLEAN)) {
            value = cell.getBooleanCellValue();
        } else if (cellType.equals(CellType.BLANK)) {
            value = "";
        } else if (cellType.equals(CellType.ERROR)) {
            value = cell.getErrorCellValue();
        }
        // 调用转换器
        if (!columnInfo.columnConfig.converter().equals(ExcelDataConverterAdapter.class)) {
            try {
                Object instance = columnInfo.columnConfig.converter().newInstance();
                Method convertMethod = columnInfo.columnConfig.converter()
                        .getMethod("convert", Object.class, String[].class, Cell.class, Workbook.class);
                value = convertMethod.invoke(instance, value, columnInfo.columnConfig.args(), cell, workbook);
            } catch (Exception e) {
                throw new RuntimeException("导入数据时，使用" + columnInfo.columnConfig.converter() + "转换器转换数据失败！", e);
            }
        }
        return value;
    }

    /**
     * 获取列集合
     */
    private List<ColumnInfo> getColumns (Sheet sheet) {
        Map<Integer, ColumnInfo> sortedColumns = new TreeMap<>();
        Field[] fields = modelClass.getDeclaredFields();
        // 获取列头
        Row row = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        int i = 0;
        while (row.getCell(i) != null) {
            headers.add(row.getCell(i).getStringCellValue());
            i++;
        }
        int index = 0;
        for (Field field : fields) {
            ExcelImportColumn excelColumn = field.getAnnotation(ExcelImportColumn.class);
            if (excelColumn == null) {
                continue;
            }
            // 获取字段对应的列索引
            int columnIndex = excelColumn.index();
            if (columnIndex == -1) {
                columnIndex = index;
                if (StringUtils.isNotBlank(excelColumn.name())) {
                    columnIndex = headers.indexOf(excelColumn.name());
                    if (columnIndex == -1) {
                        throw new RuntimeException("找不到配置列'" + excelColumn.name() + "'");
                    }
                }
            }
            if (sortedColumns.get(columnIndex) != null) {
                throw new AnnotationConfigurationException(String.format("导入配置有误，存在冲突的列索引%d", columnIndex));
            }
            sortedColumns.put(columnIndex, new ColumnInfo(columnIndex, excelColumn, field));
            index++;
        }
        return new ArrayList<>(sortedColumns.values());
    }

    /**
     * 判断是否为空行
     *
     * @param row 行对象
     * @return Boolean
     */
    private boolean isEmptyRow(Object row) throws IllegalAccessException{
        Field[] fields = row.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(Boolean.TRUE);
            if (field.get(row) != null) {
                field.setAccessible(Boolean.FALSE);
                return Boolean.FALSE;
            }
            field.setAccessible(Boolean.FALSE);
        }
        return Boolean.TRUE;
    }

    /**
     * 列信息
     */
    @Data
    @AllArgsConstructor
    private static class ColumnInfo {

        // 列索引
        private Integer index;

        // 列配置
        private ExcelImportColumn columnConfig;

        // 字段信息
        private Field field;
    }
}
