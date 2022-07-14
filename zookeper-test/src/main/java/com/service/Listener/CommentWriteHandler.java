package com.service.Listener;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.service.Listener.adapter.AbstractRowWriteHandlerAdapter;
import com.wuj.dto.ExcelError;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/** 
 * 将参数校验失败后的excels,添加批注后到导出
 * @author wujiang
 * @date 2022/7/11 10:21
 */

public class CommentWriteHandler extends AbstractRowWriteHandlerAdapter {
    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        if (!isHead){
            Sheet sheet = writeSheetHolder.getSheet();
            if (excelErrorMap.containsKey(relativeRowIndex)){
                List<ExcelError> excelErrors = excelErrorMap.get(relativeRowIndex);
                excelErrors.forEach(obj->{
                    setCellCommon(sheet,obj.getRow()+1,obj.getColumn(),obj.getErrorMsg());
                });
            }
        }
    }
}
