package com.eva.core.excel;

import com.eva.core.authorize.Authorizer;
import com.eva.core.constants.Constants;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.annotation.AnnotationConfigurationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel导出实现
 */
@Data
public class ExcelExporter<T> {

    private static final String DEFAULT_SHEET_NAME = "Sheet1";

    private Class<T> modelClass;

    private Authorizer authorizer = new Authorizer();

    private ExcelExporter(){}

    /**
     * 构造器
     *
     * @param modelClass 实体Class对象
     */
    public static <T> ExcelExporter<T> build(Class<T> modelClass) {
        ExcelExporter<T> excelExporter = new ExcelExporter<>();
        excelExporter.setModelClass(modelClass);
        return excelExporter;
    }

    /**
     * 导出到指定输出流
     *
     * @param data 数据
     * @param sheetName Sheet名称
     * @param os 输出流
     */
    public void exportData(List<T> data, String sheetName, OutputStream os) {
        SXSSFWorkbook sxssfWorkbook;
        try {
            sxssfWorkbook = new SXSSFWorkbook();
            Sheet sheet = sxssfWorkbook.createSheet(sheetName);
            // 创建列头
            sheet.createFreezePane(0, 1);
            Row header = sheet.createRow(0);
            List<ColumnInfo> columns = this.getColumns();
            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                ColumnInfo column = columns.get(columnIndex);
                Cell cell = header.createCell(columnIndex);
                cell.setCellValue(column.columnConfig.name());
                // 列宽设置
                if (column.columnConfig.width() == -1) {
                    sheet.setColumnWidth(columnIndex, column.columnConfig.name().length() * 2 * 256);
                } else {
                    sheet.setColumnWidth(columnIndex, column.columnConfig.width() * 2 * 256);
                }
                // 设置列头单元格
                configHeaderCell(sxssfWorkbook, cell, column.columnConfig);
            }
            // 创建数据记录
            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                    T dataLine = data.get(rowIndex);
                    ColumnInfo column = columns.get(columnIndex);
                    Cell cell = row.createCell(columnIndex);
                    Method getMethod = dataLine.getClass().getMethod("get" + StringUtils.capitalize(column.field.getName()));
                    Object value = getMethod.invoke(dataLine);
                    if (value != null) {
                        // 数字类型
                        if (value instanceof Double || value instanceof BigDecimal || value instanceof Integer || value instanceof Long) {
                            cell.setCellValue(getNumberCellData(column, dataLine, cell, sxssfWorkbook));
                        }
                        // 其它类型，统一归纳为字符串类型
                        else {
                            cell.setCellValue(getCellData(column, dataLine, cell, sxssfWorkbook));
                        }
                    }
                    // 设置数据单元格
                    configDataCell(sxssfWorkbook, cell, column.columnConfig);
                }
            }
            sxssfWorkbook.write(os);
            os.close();
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.EXPORT_EXCEL_ERROR, e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导出至响应流
     *
     * @param data 数据
     * @param fileName Excel文件名
     * @param sheetName Sheet名称
     * @param response HttpServletResponse对象
     */
    public void exportData(List<T> data, String fileName, String sheetName, HttpServletResponse response) {
        try {
            String encodeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()) + ".xlsx";
            response.setHeader("Content-Disposition","attachment;filename=" + encodeFileName);
            response.setContentType("application/octet-stream");
            response.setHeader(Constants.HEADER_OPERA_TYPE, "download");
            response.setHeader(Constants.HEADER_DOWNLOAD_FILENAME, encodeFileName);
            this.exportData(data, sheetName, response.getOutputStream());
        } catch (IOException e) {
            throw new BusinessException(ResponseStatus.EXPORT_EXCEL_ERROR, e);
        }
    }

    /**
     * 导出至响应流
     *
     * @param data 数据
     * @param fileName Excel文件名
     * @param response HttpServletResponse对象
     */
    public void exportData(List<T> data, String fileName, HttpServletResponse response) {
        this.exportData(data, fileName, DEFAULT_SHEET_NAME, response);
    }

    /**
     * 获取列集合
     */
    private List<ColumnInfo> getColumns () {
        Map<Integer, ColumnInfo> sortedFields = new TreeMap<>();
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            ExcelExportColumn excelColumn = field.getAnnotation(ExcelExportColumn.class);
            if (excelColumn == null) {
                continue;
            }
            if (sortedFields.get(excelColumn.index()) != null) {
                throw new AnnotationConfigurationException(String.format("导入配置有误，存在冲突的列索引%d", excelColumn.index()));
            }
            // 存在授权，则验证是否有权限
            if (StringUtils.isNotBlank(excelColumn.authorize())) {
                if (!authorizer.checkExpress(excelColumn.authorize())) {
                    continue;
                }
            }
            sortedFields.put(excelColumn.index(), new ColumnInfo(excelColumn, field));
        }
        return new ArrayList<>(sortedFields.values());
    }

    /**
     * 配置数据单元格
     */
    private void configDataCell (SXSSFWorkbook workbook, Cell cell, ExcelExportColumn columnConfig) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(columnConfig.align());
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置背景
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(columnConfig.dataBackgroundColor().getIndex());
        // 字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints(columnConfig.fontSize());
        // 字体颜色
        font.setColor(columnConfig.color().getIndex());
        // 粗体
        font.setBold(columnConfig.bold());
        // 斜体
        font.setItalic(columnConfig.italic());
        style.setFont(font);
        // 边框
        configCellBorder(style);
        cell.setCellStyle(style);
        // 设置数据格式
        if (!"".equals(columnConfig.format())) {
            style.setDataFormat(workbook.createDataFormat().getFormat(columnConfig.format()));
        }
    }

    /**
     * 配置列头单元格
     */
    private void configHeaderCell (SXSSFWorkbook workbook, Cell cell, ExcelExportColumn columnConfig) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(columnConfig.align());
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置背景
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(columnConfig.backgroundColor().getIndex());
        // 字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints(columnConfig.fontSize());
        style.setFont(font);
        // 设置边框
        configCellBorder(style);
        cell.setCellStyle(style);
    }

    /**
     * 配置单元格边框
     */
    private void configCellBorder (CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
    }

    /**
     * 处理单元格数据
     */
    private String getCellData (ColumnInfo columnInfo, T row, Cell cell, Workbook workbook) throws Exception {
        Method getMethod = row.getClass().getMethod("get" + StringUtils.capitalize(columnInfo.field.getName()));
        Object value = getMethod.invoke(row);
        if (value == null) {
            return "";
        }
        String stringValue = value.toString();
        // 存在自定义数据处理器
        if (!columnInfo.columnConfig.converter().equals(ExcelDataConverterAdapter.class)) {
            try {
                Object instance = columnInfo.columnConfig.converter().newInstance();
                Method convertMethod = columnInfo.columnConfig.converter()
                        .getMethod("convert", Object.class, String[].class, Cell.class, Workbook.class);
                value = convertMethod.invoke(instance, value, columnInfo.columnConfig.args(), cell, workbook);
                stringValue = value.toString();
            } catch (Exception e) {
                throw new RuntimeException("导出数据时，使用" + columnInfo.columnConfig.converter() + "转换器转换数据失败！", e);
            }
        }
        // 存在字典编码设置
        if (!"".equals(columnInfo.columnConfig.dict())) {
            stringValue = Utils.Dict.getDictDataLabel(columnInfo.columnConfig.dict(), stringValue);
        }
        // 日期处理
        if (!"".equals(columnInfo.columnConfig.dateFormat()) && value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(columnInfo.columnConfig.dateFormat());
            stringValue = sdf.format((Date) value);
        }
        // 值映射
        if (!"".equals(columnInfo.columnConfig.valueMapping())) {
            String[] segs = columnInfo.columnConfig.valueMapping().split(";");
            for (String seg : segs) {
                String[] mapping = seg.split("=");
                if (value.toString().equals(mapping[0].trim())) {
                    stringValue = mapping[1].trim();
                }
            }
        }
        // 前缀处理
        stringValue = columnInfo.columnConfig.prefix() + stringValue;
        // 后缀处理
        stringValue = stringValue + columnInfo.columnConfig.suffix();
        return stringValue;
    }


    /**
     * 获取数值数据
     */
    private Double getNumberCellData (ColumnInfo columnInfo, T row, Cell cell, Workbook workbook) throws Exception{
        Method getMethod = row.getClass().getMethod("get" + StringUtils.capitalize(columnInfo.field.getName()));
        Object value = getMethod.invoke(row);
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        }
        if (value instanceof Long) {
            return ((Long) value).doubleValue();
        }
        return Double.parseDouble(value.toString());
    }

    /**
     * 列信息
     */
    @Data
    @AllArgsConstructor
    private static class ColumnInfo {

        // 列配置
        private ExcelExportColumn columnConfig;

        // 字段信息
        private Field field;
    }

}
