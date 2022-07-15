package com.service.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.fastjson.JSON;
import com.service.Listener.adapter.AnalysisEventListenerAdapter;
import com.wuj.dto.ExcelModel;
import com.wuj.dto.ExcelTemplateModel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理过程中的监听器
 * @author wujiang
 * @date 2022/7/8 17:36

 */
@Slf4j
public class EasyExcelListener extends AnalysisEventListenerAdapter<ExcelModel> {

    private List<ExcelModel> excelModelList =new ArrayList<ExcelModel>();

    public EasyExcelListener() {
        super();
        excelModelList.clear();
        excelErrorMap.clear();
    }



    public List<ExcelModel> getListExcels() {
        return excelModelList;
    }

    public void invoke(ExcelModel t, AnalysisContext analysisContext) {
        excelModelList.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        ExcelTemplateModel sle = null;
        boolean isMatch = true;
        for (int i = 0; i < excelModelList.size(); i++) {
             sle = (ExcelTemplateModel)excelModelList.get(i);
             log.info("读取到第={}行数据，",JSON.toJSONString(sle));
        }
    }


}
