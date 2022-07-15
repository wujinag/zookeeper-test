package com.redis.app.zk.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.service.Listener.CommentWriteHandler;
import com.service.Listener.EasyExcelListener;
import com.service.RedisService;
import com.utils.UUIDUtils;
import com.wuj.dto.ExcelError;
import com.wuj.dto.ExcelModel;
import com.wuj.dto.ExcelTemplateModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Api("EasyExcel学习")
@RequestMapping("/eayExcel")
public class ExcelController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private GsonBuilder gsonBuilder;

    private static final String PREFIX = "easyExcel_";
    public static final String SEND_LIST = "sendList_";
    public static final String SEND_LIST_ERROR = "_error";
    public static final Long EXPIRE_TIME = 60 * 10L;

    @GetMapping("/importTemplate")
    public Object importTemplate(MultipartFile file) throws IOException {
        EasyExcelListener listener = new EasyExcelListener();
        EasyExcel.read(file.getInputStream(), ExcelTemplateModel.class,listener).sheet().doRead();
        List<ExcelModel> listExcels = listener.getListExcels();
        Gson gson = gsonBuilder.create();
        if ((listener.getExcelErrorMap().size()>0)) {
            String uuid = UUIDUtils.getGeneratorUUID();
            String key =PREFIX + SEND_LIST+ uuid;
            redisService.set(key,gson.toJson(listExcels));
            redisService.expire(key,EXPIRE_TIME);
            return key;
        }
        return null;
    }

    @GetMapping("downloadExcelTemplate")
    @ApiOperation("下载Excel模板")
    public void export(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "中文.xlsx";
        //建议加上该段，否则可能会出现前端无法获取Content-disposition
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), ExcelTemplateModel.class).sheet("账号列表").doWrite(new ArrayList<ExcelTemplateModel>());
    }

    @GetMapping("downloadErrorExcel")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "uuid", dataType = "String", value = "校验参数失败后，返回的uuid")})
    @ApiOperation("下载批注后到错误excel")
    public void downloadErrorExcel(HttpServletResponse response,
                                   @RequestParam(value = "uuid", required = true) String uuid) throws IOException {
        SimpleDateFormat fDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fDate.format(new Date()) + ".xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        CommentWriteHandler commentWriteHandler = new CommentWriteHandler();
        String key = PREFIX + SEND_LIST + uuid;
        String listExcelJson = (String) redisService.get(key);
        String listExcelErrorlJson = (String) redisService.get(key + SEND_LIST_ERROR);
        Gson gson = gsonBuilder.create();
        if (listExcelJson != null && listExcelErrorlJson != null) {
            Type listExcelJsonType = new TypeToken<List<ExcelTemplateModel>>() {}.getType();
            List<ExcelTemplateModel> sendListExcels = gson.fromJson(listExcelJson, listExcelJsonType);
            Type listExcelErrorlJsonType = new TypeToken<Map<Integer, List<ExcelError>>>() { }.getType();
            Map<Integer, List<ExcelError>> errorMap = gson.fromJson(listExcelErrorlJson, listExcelErrorlJsonType);
            commentWriteHandler.setExcelErrorMap(errorMap);
            EasyExcel.write(response.getOutputStream(), ExcelTemplateModel.class)
                    .inMemory(Boolean.TRUE)
                    .sheet("sheet1")
                    //注册批注拦截器
                    .registerWriteHandler(commentWriteHandler)
                    .doWrite(sendListExcels);
        }
    }

    @GetMapping("readExcelExcel")
    public String readExcel(){
        String fileName =this.getClass().getResource("/").getPath()+"excel"+File.separator+ File.separator+ "demo.xlsx";
        EasyExcelListener listener = new EasyExcelListener();
        EasyExcel.read(fileName,ExcelTemplateModel.class,listener).sheet().doRead();
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        return JSON.toJSONString(listener.getListExcels(), SerializerFeature.PrettyFormat,SerializerFeature.WriteBigDecimalAsPlain,SerializerFeature.PrettyFormat);
    }
}
