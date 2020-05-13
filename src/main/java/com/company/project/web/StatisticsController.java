package com.company.project.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.model.Tel;
import com.company.project.service.TelService;
import com.company.project.utils.erp.ErpDataUtils;
import com.company.project.utils.poi.XssFUtils;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by CodeGenerator on 2020/04/21.
 */
@RestController
@RequestMapping("/zb/statistics/")
public class StatisticsController {


    public static void main(String[] args) {
        String[] fileds = {"FSalerId.FName", "FQty"};
        JSONArray sale_data = ErpDataUtils.getDateByParam("SAL_SaleOrder", "", fileds, "");
        System.out.println(sale_data);

    }


    @Test
    public void test2() throws IOException {
        //用来存储最终的结果

        XssFUtils xssInstance = XssFUtils.getInstance();
        JSONArray array = new JSONArray();
        String fileName = "C:/周春-合肥圆融供应链管理有限公司拜访记录表.xlsx";
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            JSONObject res = new JSONObject();
            Sheet sheet = sheetIterator.next();
            //表格名称
            String sheetName = sheet.getSheetName();
            //所有行
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                String lastName = "";
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String stringCellValue = "";
                    try {
                        stringCellValue = cell.getStringCellValue();
                        System.out.println(stringCellValue);
                    } catch (Exception e) {
                        continue;
                    }
                    switch (lastName) {
                        case "拜访人姓名":
                        case "拜访/来访人姓名":
                            res.put("ywy", stringCellValue);
                            break;
                        case "企业全称":
                            res.put("name", stringCellValue);
                            break;
                        case "企业详细地址":
                            res.put("address", stringCellValue);
                            break;
                        case "企业联系人信息":
                            //姓名： 李经理       性别：   女     部门及职位： 采购经理    手机号码： 18124638123
                            JSONObject obj = new JSONObject();
                            //去除空格
                            String s = stringCellValue.replaceAll(" ", "");
                            //利用冒号切割
                            String[] split = s.split("：");
                            for (int i = 0; i < split.length; i++) {
                                String a_ = split[i];
                                switch (a_) {
                                    case "姓名":
                                        obj.put("person_name", split[++i]);
                                        break;
                                    case "性别":
                                        obj.put("sex", split[++i]);
                                        break;
                                    case "部门及职位":
                                        obj.put("job", split[++i]);
                                        break;
                                    case "手机号码":
                                        obj.put("mobile", split[++i]);
                                        break;
                                }
                                res.put("linkMan",obj);

                            }

                            break;
                    }
                    lastName = stringCellValue;
                }
            }

            array.add(res);

        }

        System.out.println(array);

    }
}
