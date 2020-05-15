package com.conpany.project;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.Application;
import com.company.project.model.Tel;
import com.company.project.service.Plan1Service;
import com.company.project.service.TelService;
import com.company.project.utils.string.StrUtils;
import com.spire.ms.System.Collections.IEnumerator;
import com.spire.xls.*;
import com.spire.xls.collections.WorksheetsCollection;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SysnTest {
    private TelService telService;

    private Plan1Service plan1Service;

    public Plan1Service getPlan1Service() {
        return plan1Service;
    }

    public void setPlan1Service(Plan1Service plan1Service) {
        this.plan1Service = plan1Service;
    }

    public TelService getTelService() {
        return telService;
    }

    public void setTelService(TelService telService) {
        this.telService = telService;
    }

    // 等待队列
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(500);
    // 创建一个大小为5的线程池
    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, workQueue,
            new ThreadPoolExecutor.DiscardPolicy());


    public void test2() throws IOException, ParseException, InterruptedException {
        //用来存储最终的结果


        String fileName = "C:\\Users\\Administrator\\Desktop\\拜访源数据20200513\\拜访源数据20200513\\钢材部拜访数据";
        String fileName2 = "C:\\Users\\Administrator\\Desktop\\拜访源数据20200513\\拜访源数据20200513\\客户拜访塑料原始数据\\客户拜访汇总表";

        File floder = new File(fileName);
        File floder2 = new File(fileName2);

        File[] files = floder.listFiles();
        File[] files2 = floder2.listFiles();
        File[] files1 = ArrayUtils.addAll(files, files2);
        for (File file : files1) {
            if (!file.getName().startsWith("周春")) {
                continue;
            }

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        parseBaiFangExcel(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        Thread.sleep(2000000);


    }


    public void parseBaiFangExcel(File file) throws FileNotFoundException, ParseException {


        JSONArray array = new JSONArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy/MM/dd");


        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new Workbook();
        workbook.loadFromFile(file.getAbsolutePath());

        WorksheetsCollection worksheets = workbook.getWorksheets();
        IEnumerator sheetIterator = worksheets.iterator();
        String lastName = "";

        while (sheetIterator.hasNext()) {
            int dateRow = 1; //日期列
            JSONObject baifang = new JSONObject();
            JSONArray baiFangArr = new JSONArray();
            JSONObject res = new JSONObject();
            Worksheet sheet = (Worksheet) sheetIterator.next();
            //表格名称
            String sheetName = sheet.getName();
            if (sheetName.toLowerCase().startsWith("sheet")) {
                continue;
            }
            //所有行
            CellRange[] rows = sheet.getRows();
            for (int k = 0; k < rows.length; k++) {


                CellRange row = rows[k];


                CellRange[] columns = row.getColumns();
                for (int j = 0; j < columns.length; j++) {
                    CellRange cell = columns[j];


                    String stringCellValue = cell.getValue();

                    try {

                        if (StrUtils.isNull(stringCellValue.trim())) {
                            continue;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                    //记录日期的列
                    if (stringCellValue.startsWith("日期")) {
                        dateRow = j;
                    }


                    if (lastName.startsWith("其他信息获取")) {
                        baifang.put("happen", stringCellValue);
                    } else if (lastName.startsWith("后续合作推进计划")) {
                        baifang.put("plan", stringCellValue);
                        baiFangArr.add(baifang);
                    } else if (lastName.startsWith("企业主要上游供应商")) {

                        JSONArray product = new JSONArray();

                        boolean judge = false;
                        while ("供应商名称".equals(sheet.getCellRange(k + 1, j + 1).getText())) {
                            //供应商名称
                            String text = sheet.getCellRange(k + 1, j + 2).getText().trim();
                            //供应材料
                            String tex2 = sheet.getCellRange(k + 1, j + 4).getText().trim();
                            if (!StrUtils.isNull(text) || !StrUtils.isNull(tex2)) {
                                JSONObject p = new JSONObject();
                                p.put("name", text);
                                p.put("material", tex2);
                                product.add(p);
                            }
                            k++;
                            judge = true;
                        }
                        if (judge) {
                            k--;
                        }

                        if (product.size() > 0) {
                            res.put("product", product);
                        }

                    } else if (lastName.startsWith("企业主要下游客户")) {

                        JSONArray c2 = new JSONArray();

                        boolean judge = false;
                        while ("下游客户".equals(sheet.getCellRange(k + 1, j + 1).getText())) {
                            //供应商名称
                            String text = sheet.getCellRange(k + 1, j + 2).getText().trim();
                            //供应材料
                            String tex2 = sheet.getCellRange(k + 1, j + 4).getText().trim();
                            if (!StrUtils.isNull(text) || !StrUtils.isNull(tex2)) {
                                JSONObject p = new JSONObject();
                                p.put("name", text);
                                p.put("material", tex2);
                                c2.add(p);
                            }
                            k++;
                            judge = true;
                        }
                        if (judge) {
                            k--;
                        }

                        if (c2.size() > 0) {
                            res.put("c2", c2);
                        }

                    } else if (lastName.startsWith("企业其他材料需求")) {


                        //姓名： 类别：钢材      牌号及月用量：DX51D+Z  600吨     竞争供应商及背景：
                        JSONObject obj = new JSONObject();
                        //去除空格

                        //利用冒号切割
                        JSONArray c3 = new JSONArray();

                        boolean judge = false;
                        while (sheet.getCellRange(k + 1, j + 1).getText().startsWith("类别")) {
                            //供应商名称
                            JSONObject o = new JSONObject();
                            String text = sheet.getCellRange(k + 1, j + 1).getText().trim();
                            String[] split_ = text.split("\\s+");
                            for (String s : split_) {
                                String[] split = s.split("：");
                                switch (split[0]) {
                                    case "类别":
                                        if (split.length > 1) {
                                            o.put("sort", split[1]);
                                        }
                                        break;
                                    case "竞争供应商及背景":
                                        if (split.length > 1) {
                                            o.put("background", split[1]);
                                        }
                                        break;
                                    case "牌号及月用量":
                                        if (split.length > 2) {
                                            o.put("grade", split[1]);
                                            o.put("used", split[2]);

                                        } else if (split.length > 1) {
                                            o.put("grade", split[1]);
                                        }

                                }
                            }
                            k++;
                            c3.add(o);
                            judge = true;
                        }
                        if (judge) {
                            k--;
                        }

                        if (c3.size() > 0) {
                            res.put("c3", c3);
                        }


                    } else if (k > 15 && j == dateRow && stringCellValue.startsWith("20")) {

                        baifang = new JSONObject();
                        //2019/10/9（韩非，朱文健陪同）
                        if (stringCellValue.contains("陪同") || stringCellValue.contains("(") || stringCellValue.contains("（")) {
                            String[] split = null;
                            if (stringCellValue.contains("（")) {
                                split = stringCellValue.split("（");
                            } else if (stringCellValue.contains("(")) {
                                split = stringCellValue.split("\\(");
                            } else if (stringCellValue.contains("\n")) {
                                split = stringCellValue.split("\n");
                            }

                            try {
                                baifang.put("date", simpleDateFormat.format(simpleDateFormat2.parse(split[0])));
                            } catch (Exception e) {
                                try {
                                    baifang.put("date", simpleDateFormat.format(simpleDateFormat.parse(split[0])));
                                } catch (Exception parseException) {
                                    System.err.println(file.getName() + " " + sheetName + " " + res.getString("name"));
                                    parseException.printStackTrace();
                                }
                            }

                            String replace = split[1].replaceAll("人", "").replaceAll("、", ",").replaceAll("：", "").replaceAll("陪同", "").replaceAll("）", "").replaceAll("\\)", "").replace("，", "");
                            baifang.put("others", replace);
                        } else {
                            baifang.put("date", stringCellValue.split(" "));
                        }
                    }

                    switch (lastName.trim()) {

                        case "拜访总结":
                            baifang = new JSONObject();
                            //2019/10/9（韩非，朱文健陪同）
                            if (stringCellValue.contains("陪同") || stringCellValue.contains("(") || stringCellValue.contains("（")) {
                                String[] split = null;
                                if (stringCellValue.contains("（")) {
                                    split = stringCellValue.split("（");
                                } else if (stringCellValue.contains("(")) {
                                    split = stringCellValue.split("\\(");
                                } else if (stringCellValue.contains("\n")) {
                                    split = stringCellValue.split("\n");
                                }

                                try {
                                    baifang.put("date", simpleDateFormat.format(simpleDateFormat2.parse(split[0])));
                                } catch (Exception e) {
                                    try {
                                        baifang.put("date", simpleDateFormat.format(simpleDateFormat.parse(split[0])));
                                    } catch (Exception parseException) {
                                        System.err.println(sheetName + file.getName());
                                    }
                                }
                                String replace = split[1].replaceAll("人", "").replaceAll("、", ",").replaceAll("：", "").replaceAll("陪同", "").replaceAll("）", "").replaceAll("\\)", "").replace("，", "");
                                baifang.put("others", replace);
                            } else {
                                baifang.put("date", stringCellValue);
                            }

                            break;


                        case "拜访目的":
                            baifang.put("target", stringCellValue);
                            break;
                        case "拜访结果":
                            baifang.put("result", stringCellValue);
                            break;
                        case "拜访人姓名":
                        case "拜访/来访人姓名":
                            res.put("ywy", stringCellValue);
                            break;
                        case "企业全称":
                            res.put("name", stringCellValue);
                            break;
                        case "企业人数（人）":
                            res.put("@danh_16", stringCellValue);
                            break;
                        case "企业规模（吨）":
                            res.put("@danh_34", stringCellValue);
                            break;
                        case "我司月平均合作数量（吨）":
                            res.put("@danh_24", stringCellValue);
                            break;
                        case "企业月销售量（吨）":
                            res.put("@danh_17", stringCellValue);
                            break;

                        case "是否为新客户":
                        case "是否为新供应商":

                            if (!res.containsKey("@meju_27")) {
                                if (stringCellValue.contains("供应商")) {
                                    res.put("@meju_27", 28);
                                } else {
                                    res.put("@meju_27", 27);
                                }
                            }

                            boolean isNew = false;
                            RichText richText = cell.getRichText();
                            boolean breaks = false;
                            String text = richText.getText();
                            if (text.contains("√")) {
                                int i = text.indexOf('√');
                                char c = text.charAt(i + 1);
                                isNew = '是' == c ? true : false;
                                breaks = true;
                            } else if (text.contains("■")) {
                                String s = text.replaceAll("\\s", "");
                                int i = s.indexOf('■');
                                if ('是' == s.charAt((i + 1)) || '否' == s.charAt(i + 1)) {
                                    isNew = '是' == s.charAt(i + 1) ? true : false;

                                }
                            } else {
                                for (int c = 0; c < text.length(); c++) {
                                    if ('□' == (text.charAt(c))) {
                                        ExcelFont font = richText.getFont(c);

                                        Color color = font.getColor();
                                        if (color.getRed() != 0) {
                                            for (int g = c + 1; g < text.length(); g++) {
                                                if ('是' == text.charAt(g) || '否' == text.charAt(g)) {
                                                    isNew = '是' == text.charAt(g) ? true : false;
                                                    breaks = true;
                                                    break;
                                                }

                                            }
                                        }

                                    }
                                    if (breaks)
                                        break;
                                }
                            }


                            res.put("isNewCustomer", isNew);
                            break;
                        case "企业基本情况":


                            RichText richText_ = cell.getRichText();
                            boolean breaks_ = false;
                            String text_ = richText_.getText();

                            if (text_.contains("■")) {
                                String s = text_.replaceAll("\\s", "");
                                int i = s.indexOf('■');
                                if (s.charAt(i + 1) == '二' || s.charAt(i + 1) == '供' || s.charAt(i + 1) == '客')
                                    switch (s.charAt(i + 1)) {
                                        case '二':
                                            res.put("@meju_27", 29);
                                            break;
                                        case '客':
                                            res.put("@meju_27", 27);
                                            break;
                                        case '供':
                                            res.put("@meju_27", 28);
                                            break;
                                    }
                            } else
                                for (int c = 0; c < text_.length(); c++) {
                                    if ('□' == (text_.charAt(c))) {
                                        ExcelFont font = richText_.getFont(c);

                                        Color color = font.getColor();
                                        if (color.getRed() != 0) {
                                            for (int g = c + 1; g < text_.length(); g++) {
                                                if (text_.charAt(g) == '二' || text_.charAt(g) == '供' || text_.charAt(g) == '客')
                                                    switch (text_.charAt(g)) {
                                                        case '二':
                                                            res.put("@meju_27", 29);
                                                            break;
                                                        case '客':
                                                            res.put("@meju_27", 27);
                                                            break;
                                                        case '供':
                                                            res.put("@meju_27", 28);
                                                            break;
                                                    }
                                                breaks_ = true;

                                            }

                                        }
                                    }
                                    if (breaks_)
                                        break;
                                }
                            break;
                        case "企业详细地址":
                            res.put("address", stringCellValue);
                            break;
                        case "企业联系人信息":
                            //姓名： 李经理       性别：   女     部门及职位： 采购经理    手机号码： 18124638123
                            JSONObject obj = new JSONObject();
                            //去除空格
                            String s = stringCellValue.trim();
                            //利用冒号切割
                            String[] split = s.split("\\s+");
                            for (int i = 0; i < split.length; i++) {
                                String a_ = split[i];
                                switch (a_) {
                                    case "姓名：":
                                        obj.put("person_name", split[++i]);
                                        break;
                                    case "性别：":
                                        obj.put("sex", split[++i]);
                                        break;
                                    case "部门及职位：":
                                        obj.put("job", split[++i]);
                                        break;
                                    case "手机号码：":
                                    case "联系方式：":
                                        try {
                                            obj.put("mobile", split[++i]);
                                        } catch (Exception e) {

                                        }

                                        break;


                                    default:
                                        if (a_.length() - 1 > a_.indexOf("：")) {
                                            String[] split1 = a_.split("：");
                                            switch (split1[0]) {
                                                case "姓名":
                                                    obj.put("person_name", split1[1]);
                                                    break;
                                                case "性别":
                                                    obj.put("sex", split1[1]);
                                                    break;
                                                case "部门及职位":
                                                    obj.put("job", split1[1]);
                                                    break;
                                                case "手机号码":
                                                case "联系方式":
                                                    obj.put("mobile", split1[1]);
                                                    break;
                                            }

                                        }
                                        break;
                                }

                                //获取爱好
                                CellRange row1 = rows[k + 1];
                                CellRange[] columns1 = row1.getColumns();
                                CellRange cellRange = columns1[j];
                                String value = cellRange.getValue();

                                //去除空格
                                value = value.trim();
                                //利用冒号切割
                                try {
                                    String[] split_ = value.split("：");
                                    obj.put("hobby", split_[1]);
                                } catch (Exception e) {

                                }
                                res.put("linkMan", obj);

                            }

                            break;
                    }
                    if (!StrUtils.isNull(stringCellValue.trim())) {
                        lastName = stringCellValue;
                    }

                }

            }
            res.put("baifang", baiFangArr);
            array.add(res);

        }

        importToCrm(array);
    }


    public void importToCrm(JSONArray arr) {
        for (int i = 0; i < arr.size(); i++) {
             JSONObject obj = arr.getJSONObject(i);


            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String name = obj.getString("name");
                   if(StrUtils.isNull(name)){
                       return;
                   }
                    //1.查询有没有当前客户
                    Condition condition = new Condition(Tel.class);
                    Example.Criteria criteria = condition.createCriteria();
                    criteria.andEqualTo("name", name);
                    criteria.andEqualTo("del", 1);
                    List<Tel> tels = telService.findByCondition(condition);
                    if (!CollectionUtils.isEmpty(tels)) {


                    }

                }
            });


        }


    }
}
