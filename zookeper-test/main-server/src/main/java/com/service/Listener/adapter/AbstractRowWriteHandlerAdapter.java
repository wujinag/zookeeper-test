package com.service.Listener.adapter;

import com.alibaba.excel.write.handler.AbstractRowWriteHandler;
import com.wuj.dto.ExcelError;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * AbstractRowWriteHandlerAdapter适配器
 * @author wujiang
 * @date 2022/7/8 17:46
 */
public abstract class AbstractRowWriteHandlerAdapter extends AbstractRowWriteHandler {

    protected Map<Integer, List<ExcelError>> excelErrorMap = new HashMap<>();

    public void setExcelErrorMap(Map<Integer, List<ExcelError>> excelErrorMap) {
        this.excelErrorMap = excelErrorMap;
    }

    /**
     * 设置单元格批注
     * @param sheet sheet
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @param value 批注
     */
    protected void setCellCommon(Sheet sheet, int rowIndex, int colIndex, String value) {
        Workbook workbook = sheet.getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return;
        }
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }
        if (value == null) {
            cell.removeCellComment();
            return;
        }
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper factory = sheet.getWorkbook().getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        Row row1 = sheet.getRow(anchor.getRow1());
        if (row1 != null) {
            Cell cell1 = row1.getCell(anchor.getCol1());
            if (cell1 != null) {
                cell1.removeCellComment();
            }
        }
        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(value);
        comment.setString(str);
        comment.setAuthor("admin");
        cell.setCellComment(comment);
        cell.setCellStyle(cellStyle);
    }
}
