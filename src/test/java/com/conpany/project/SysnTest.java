package com.conpany.project;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.Application;
import com.company.project.model.Person;
import com.company.project.model.Plan1;
import com.company.project.model.Tel;
import com.company.project.service.PersonService;
import com.company.project.service.Plan1Service;
import com.company.project.service.TelService;
import com.company.project.utils.crm.CrmUtils;
import com.company.project.utils.string.StrUtils;
import com.spire.ms.System.Collections.IEnumerator;
import com.spire.xls.*;
import com.spire.xls.collections.WorksheetsCollection;
import lombok.SneakyThrows;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SysnTest {
    private TelService telService;

    private Plan1Service plan1Service;

    private PersonService personService;


    final JSONObject allYwyInfo = CrmUtils.getAllCrmAccountInfos();

    public PersonService getPersonService() {
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

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
            if (file.getName().startsWith("~")) {
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

        Thread.sleep(20000000);


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


                        if (lastName.startsWith("后续合作推进计划")) {
                            baifang.put("plan", stringCellValue);
                            if (baifang.containsKey("date")) {
                                Object clone = baifang.clone();

                                baiFangArr.add(clone);
                                baifang = new JSONObject();
                            }
                        }


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


                    if ((k > 15 && j == dateRow && stringCellValue.startsWith("20")) || lastName.startsWith("拜访总结")) {


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
                            try {
                                baifang.put("date", simpleDateFormat.format(simpleDateFormat2.parse(stringCellValue)));
                            } catch (Exception e) {
                                try {
                                    baifang.put("date", simpleDateFormat.format(simpleDateFormat.parse(stringCellValue)));
                                } catch (Exception parseException) {
                                    System.err.println(sheetName + file.getName());
                                }
                            }
                        }
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


                    }
                    if (lastName.startsWith("其他信息获取")) {
                        baifang.put("happen", stringCellValue);
                    }


                    switch (lastName.trim()) {


                        case "拜访目的":
                            baifang.put("target", stringCellValue);
                            break;
                        case "拜访结果":
                            baifang.put("result", stringCellValue);
                            break;
                        case "拜访人姓名":
                        case "拜访/来访人姓名":
                            stringCellValue = stringCellValue.contains("、") ? stringCellValue.substring(0, stringCellValue.indexOf("、")) : stringCellValue;
                            stringCellValue = stringCellValue.contains("（") ? stringCellValue.substring(0, stringCellValue.indexOf("（")) : stringCellValue;
                            stringCellValue = stringCellValue.contains("/") ? stringCellValue.substring(0, stringCellValue.indexOf("/")) : stringCellValue;
                            res.put("ywy", stringCellValue.trim());
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
                        case "月平均原料用量（吨）":
                            res.put("@danh_17", stringCellValue);
                            break;

                        case "是否为新客户":
                        case "是否为新供应商":

                            if (!res.containsKey("@meju_27")) {
                                if (lastName.contains("供应商")) {
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


                            res.put("@IsNot_15", isNew ? '是' : '否');
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
                                                if (text_.charAt(g) == '二' || text_.charAt(g) == '供' || text_.charAt(g) == '客') {
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
                                                    break;
                                                }


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
                                            obj.put("mobile", split[++i].contains("手机")?"":split[i]);
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
                                                    obj.put("mobile", split1[1].contains("手机")?"":split1[1]);
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
            System.out.println(res);
            array.add(res);
            System.out.println(res);

        }

        importToCrm(array);
    }


    public void importToCrm(JSONArray arr) throws ParseException {
        //登录Crm
        CrmUtils.crmLogin();
        for (int j = 0; j < arr.size(); j++) {
            try {
                JSONObject obj = arr.getJSONObject(j);

                if (!obj.containsKey("name")) {
                    continue;
                }
                final String name = obj.getString("name");
                if (StrUtils.isNull(name)) {
                    return;
                }
                //1.查询有没有当前客户
                Condition condition = new Condition(Tel.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andEqualTo("name", name);
                criteria.andEqualTo("del", 1);
                List<Tel> tels = telService.findByCondition(condition);
                String address = StrUtils.isNull(obj.getString("address")) ? "" : obj.getString("address");
                String ywy = StrUtils.isNull(obj.getString("ywy")) ? "" : obj.getString("ywy");
                int ywyId = allYwyInfo.getIntValue(ywy);
                String product = StrUtils.isNull(obj.getString("product")) ? "" : obj.getString("product");
                String c2 = StrUtils.isNull(obj.getString("c2")) ? "" : obj.getString("c2");
                String c3 = StrUtils.isNull(obj.getString("c3")) ? "" : obj.getString("c3");
                Integer personId = null;
                JSONObject linkMan = obj.getJSONObject("linkMan");
                String person_name = StrUtils.isNull(linkMan.getString("person_name")) ? "" : linkMan.getString("person_name");
                String mobile = StrUtils.isNull(linkMan.getString("mobile")) ? "" : linkMan.getString("mobile");
                String sex = StrUtils.isNull(linkMan.getString("sex")) ? "" : linkMan.getString("sex");
                String job = StrUtils.isNull(linkMan.getString("job")) ? "" : linkMan.getString("job");
                String hobby = StrUtils.isNull(linkMan.getString("hobby")) ? "" : linkMan.getString("hobby");
                Tel tel = null;
                if (!CollectionUtils.isEmpty(tels)) {
                    //更新客户

                    tel = tels.get(0);

                    //联系人
                    Integer ord = tel.getOrd();
                    //根据手机号和名称查找联系人是否存在

                    condition = new Condition(Person.class);
                    criteria = condition.createCriteria();
                    criteria.andEqualTo("name", person_name);
                    criteria.andEqualTo("del", 1);
                    criteria.andEqualTo("mobile", mobile);
                    criteria.andEqualTo("company", ord);

                    List<Person> persons = personService.findByCondition(condition);
                    //更新联系人信息
                    if (!CollectionUtils.isEmpty(persons)) {
                        Person person = persons.get(0);
                        if (!StrUtils.isNull(sex)) {
                            person.setSex(sex);
                        }
                        if (!StrUtils.isNull(hobby)) {
                            person.setMsn(hobby);
                        }
                        if (!StrUtils.isNull(job)) {
                            person.setJob(job);
                        }
                        personId = person.getOrd();
                        personService.update(person);
                        //以下是联系人不存在情况 新增联系人
                    } else {
                        Person person = new Person();
                        person.setSex(sex);
                        person.setName(person_name);
                        person.setMobile(mobile);
                        person.setJob(job);
                        person.setMsn(hobby);
                        person.setCompany(ord);
                        person.setAddress(address);
                        person.setSort(3 + "");
                        person.setSort1(21 + "");
                        person.setOrder1(2);
                        person.setDate7(new Date());
                        person.setDel(1);
                        person.setBirthdayType(1);
                        person.setBDays(-1);
                        person.setCateid(allYwyInfo.getIntValue(ywy));
                        person = personService.insert(person);
                        personId = person.getOrd();
                        System.out.println(person.getOrd());

                    }


                }
                //客户不存在的情况,新增客户
                if (tel == null) {
                    tel = new Tel();
                    tel.setDate2(new Date());
                    tel.setDate1(new Date());
                    tel.setSort2(1);
                    tel.setSort(3 + "");
                    tel.setCateadd(ywyId);
                    tel.setCateorder1(ywyId);
                    tel.setPerson(personId);
                    tel.setEmail(address);
                    tel.setAddress(address);
                    Tel tel1 = telService.insertKey(tel);

                    tel = tel1;

                    //联系人
                    Integer ord = tel.getOrd();
                    //根据手机号和名称查找联系人是否存在

                    condition = new Condition(Person.class);
                    criteria = condition.createCriteria();
                    criteria.andEqualTo("name", person_name);
                    criteria.andEqualTo("del", 1);
                    criteria.andEqualTo("mobile", mobile);
                    criteria.andEqualTo("company", ord);

                    List<Person> persons = personService.findByCondition(condition);
                    //更新联系人信息
                    if (!CollectionUtils.isEmpty(persons)) {
                        Person person = persons.get(0);
                        if (!StrUtils.isNull(sex)) {
                            person.setSex(sex);
                        }
                        if (!StrUtils.isNull(hobby)) {
                            person.setMsn(hobby);
                        }
                        if (!StrUtils.isNull(job)) {
                            person.setJob(job);
                        }
                        personId = person.getOrd();
                        personService.update(person);
                        //以下是联系人不存在情况 新增联系人
                    } else {
                        Person person = new Person();
                        person.setSex(sex);
                        person.setName(person_name);
                        person.setMobile(mobile);
                        person.setJob(job);
                        person.setMsn(hobby);
                        person.setCompany(ord);
                        person.setAddress(address);
                        person.setSort(3 + "");
                        person.setSort1(21 + "");
                        person.setOrder1(2);
                        person.setDate7(new Date());
                        person.setDel(1);
                        person.setBirthdayType(1);
                        person.setBDays(-1);
                        person.setCateid(allYwyInfo.getIntValue(ywy));
                        person = personService.insert(person);
                        personId = person.getOrd();
                        System.out.println(person.getOrd());

                    }


                }
                tel.setPerson(personId);
                tel.setName(name);
                tel.setProduct(product);
                tel.setC2(c2);
                tel.setC3(c3);
                tel.setCateid(ywyId);
                tel.setDel(1);

                Map<String, String[]> map = new HashMap<>();
                Set<String> strings = obj.keySet();
                for (String string : strings) {
                    if (string.startsWith("@")) {
                        map.put(string, new String[]{obj.get(string) + ""});
                    }
                }
                map.put("name", new String[]{name});
                telService.updateCustInfo(tel, map);


                //客户指派
                CrmUtils.CrmCustomerDesignate(tel.getOrd() + "", ywyId + "", false);


                //新增洽谈
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                JSONArray baifang = obj.getJSONArray("baifang");
                if (!CollectionUtils.isEmpty(baifang)) {
                    for (int i = 0; i < baifang.size(); i++) {
                        Plan1 plan1 = new Plan1();
                        JSONObject jsonObject = baifang.getJSONObject(i);
                        String date = jsonObject.getString("date");
                        String result = jsonObject.getString("result");
                        String plan = jsonObject.getString("plan");
                        String others = jsonObject.getString("others");
                        String target = jsonObject.getString("target");
                        JSONObject intro = new JSONObject();
                        intro.put("result", result);
                        intro.put("plan", plan);
                        intro.put("target", target);
                        plan1.setIntro(intro.toString());
                        plan1.setOthers(others);
                        plan1.setOrder1(ywyId + "");
                        plan1.setCompany(tel.getOrd());
                        plan1.setPerson(personId);
                        plan1.setAddress(address);
                        plan1.setCateid(ywyId);
                        plan1.setType("上门拜访");

                        try {
                            plan1Service.savePlan1OneKey(plan1, simpleDateFormat.parse(date.substring(0, 10)).getTime(), ywy, person_name);
                        } catch (Exception e) {
                            System.out.println(date);
                            System.out.println(ywy + "" + name);
                            e.printStackTrace();
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }


}









