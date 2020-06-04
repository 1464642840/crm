package com.company.project.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.model.Tel;
import com.company.project.service.StatisticsService;
import com.company.project.service.TelService;
import com.company.project.utils.erp.ErpDataUtils;
import com.company.project.utils.poi.XssFUtils;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spire.ms.System.Collections.IEnumerator;
import com.spire.xls.*;
import com.spire.xls.CellRange;
import com.spire.xls.Workbook;
import com.spire.xls.collections.WorksheetsCollection;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.soap.Addressing;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by CodeGenerator on 2020/04/21.
 */
@RestController
@RequestMapping("/zb/statistics/")
public class StatisticsController {


    @Autowired
    private StatisticsService statisticsService;

    public static void main(String[] args) {
        String[] fileds = {"FSalerId.FName", "FQty"};
        JSONArray sale_data = ErpDataUtils.getDateByParam("SAL_SaleOrder", "", fileds, "");
        System.out.println(sale_data);

    }

    /**
     * 业务员拜访以及订单统计
     */
    @RequestMapping("/saleAndVisitData")
    public Result saleManDataStatistics(@RequestParam String type, @RequestParam Long startDate, @RequestParam Long endDate) throws ParseException {
        Date sD = new Date(startDate);
        Date eD = new Date(endDate + 3600 * 24 * 1000);
        JSONObject obj = statisticsService.getSaleManDataStatistics(type, sD, eD);
        return ResultGenerator.genSuccessResult(obj);

    }

    /**
     * 客户拜访数据统计
     */
    @RequestMapping("/customerVisitData")
    public Result customerVisitStatistics(@RequestParam String type, @RequestParam Long startDate, @RequestParam Long endDate) throws ParseException {
        Date sD = new Date(startDate);
        Date eD = new Date(endDate + 3600 * 24 * 1000);
        JSONObject obj = statisticsService.getcustomerVisitStatistics(type, sD, eD);
        return ResultGenerator.genSuccessResult(obj);

    }

    /**
     * 导出拜访数量汇总表
     * */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping("/doExportBaiFang")
    public Result doExport(@RequestParam String date) throws ParseException {
        return ResultGenerator.genSuccessResult(statisticsService.getYwyVisitTodayStatistics(date));

    }


}
