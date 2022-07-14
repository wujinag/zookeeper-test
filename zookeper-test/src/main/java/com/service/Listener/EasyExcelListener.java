package com.service.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.service.Listener.adapter.AnalysisEventListenerAdapter;
import com.utils.EasyExcelUtils;
import com.wuj.dto.ExcelModel;
import com.wuj.dto.ExcelTemplateModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理过程中的监听器
 * @author wujiang
 * @date 2022/7/8 17:36

 */
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
            isMatch = true;
            Integer accountCellIndex = EasyExcelUtils.getCellIndex(sle, "account");
            if (accountCellIndex != null) {
                if (StringUtils.isAllBlank(sle.getAccount())) {
                    setExcelErrorMaps(i, accountCellIndex, "账号不能为空！");
                }
            }
            Integer templateCodeCellIndex = EasyExcelUtils.getCellIndex(sle, "templateCode");
            if (templateCodeCellIndex != null) {
                if (StringUtils.isAllBlank(sle.getTemplateCode())) {
                    setExcelErrorMaps(i, templateCodeCellIndex, "编号不能为空！");
                }
            }
            Integer accountTypeCellIndex = EasyExcelUtils.getCellIndex(sle, "accountType");
            if (accountTypeCellIndex != null) {
                if (StringUtils.isAllBlank(sle.getAccountType())) {
                    setExcelErrorMaps(i, accountTypeCellIndex, "类型不能为空！");
                } else {
                    if ("sms".equals(sle.getAccountType()) || "email".equals(sle.getAccountType()) || "wechat".equals(sle.getAccountType())) {
                        isMatch = false;
                    }
                    if (isMatch) {
                        setExcelErrorMaps(i, accountTypeCellIndex, "类型只允许：sms、email、wechat");
                    }
                }
            }
        }
    }


}
