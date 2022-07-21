package com.wuj.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated()
@ContentRowHeight(10)
@HeadRowHeight(20)
public class ExcelTemplateModel extends ExcelModel{

    @ExcelProperty({"账号"})
    @ColumnWidth(20)
    private String account;

    @ExcelProperty("编号")
    @ColumnWidth(30)
    private String templateCode;

    @ExcelProperty("类型")
    @ColumnWidth(15)
    private String accountType;

    @ExcelProperty("日期")
    @ColumnWidth(25)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private String date;
}
