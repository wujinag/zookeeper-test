package com.wuj.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated()
@ContentRowHeight(10)
@HeadRowHeight(20)
public class ExcelTemplateModel extends ExcelModel{

    @ExcelProperty(value = "账号",index = 0)
    @ColumnWidth(20)
    private String account;

    @ExcelProperty(value = "编号",index = 1)
    @ColumnWidth(30)
    private String templateCode;

    @ExcelProperty(value = "类型",index = 2)
    @ColumnWidth(15)
    private String accountType;
}
