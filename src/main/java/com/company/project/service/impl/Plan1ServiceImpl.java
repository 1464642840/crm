package com.company.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.core.MyThread;
import com.company.project.core.ServiceException;
import com.company.project.dao.ErpCustomvaluesMapper;
import com.company.project.dao.Plan1Mapper;
import com.company.project.dao.ReplyMapper;
import com.company.project.dao.TelMapper;
import com.company.project.model.ErpCustomvalues;
import com.company.project.model.Gate;
import com.company.project.model.Plan1;
import com.company.project.model.Reply;
import com.company.project.service.GateService;
import com.company.project.service.Plan1Service;
import com.company.project.core.AbstractService;
import com.company.project.utils.erp.StatisticsUtils;
import com.company.project.utils.poi.Excel2PDF;
import com.company.project.utils.poi.PDFUtils;
import com.company.project.utils.string.StrUtils;
import com.company.project.utils.zip.ZipUtils;
import com.github.pagehelper.PageHelper;
import com.spire.xls.CellRange;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.spire.xls.collections.WorksheetsCollection;
import com.spire.xls.packages.sprtFC;
import lombok.SneakyThrows;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by CodeGenerator on 2020/04/24.
 */
@Service
@Transactional
public class Plan1ServiceImpl extends AbstractService<Plan1> implements Plan1Service {
    @Resource
    private Plan1Mapper plan1Mapper;
    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private GateService gateService;

    @Resource
    private ErpCustomvaluesMapper erpCustomvaluesMapper;

    @Resource
    private TelMapper telMapper;

    public final String path = "E:/springboot";

    @Override
    public List<Plan1> findByMyCondition(Map<String, Object> map) {
        //用户对象
        Gate by = gateService.findBy("username", map.get("ywy"));

        map.put("username", by.getUsername());

        String position = map.get("position").toString();


        if (map.containsKey("position")) {
            if ("部门长".equals(position) || "综合主管".equals(position)) {
                //1.获得部门长所在的部门id

                Integer sorce = by.getSorce();
                map.put("sorce", sorce);
                map.remove("ywy");
            } else if ("总经理".equals(position) || "风控".equals(position)) {
                String bumen = map.containsKey("bumen") ? map.get("bumen").toString() : "";


                map.put("sorce", bumen.startsWith("钢材") ? 3 : 2);
                if (StrUtils.isNull(bumen)) {
                    map.remove("sorce");
                }
                map.remove("ywy");
            }
        }

        //2.拿到业务员id
        if (map.containsKey("ywy")) {
            Integer ord = by.getOrd();
            map.put("ywyId", ord);
        }

        //1.查出拜访列表
        PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString()));
        List<Plan1> byMyCondition = plan1Mapper.findByMyCondition(map);
        List<Integer> plan1Ids = new ArrayList<>();
        for (Plan1 plan1 : byMyCondition) {
            plan1Ids.add(plan1.getOrd());
        }


        return byMyCondition;
    }

    @Override
    public String tags(String ord) {
        Plan1 plan1 = plan1Mapper.selectByPrimaryKey(ord);
        if (plan1 == null) {
            return "";
        }
        return null;
    }

    @Override
    public JSONObject statistics(HashMap<String, Object> map) {
        //用户对象
        Gate by = gateService.findBy("ord", map.get("ywyId"));
        if (map.containsKey("position")) {
            String position = map.get("position").toString();
            if ("部门长".equals(position) || "综合主管".equals(position)) {
                //1.获得部门长所在的部门id

                Integer sorce = by.getSorce();
                map.put("sorce", sorce);
                map.remove("ywyId");
            } else if ("总经理".equals(position) || "风控".equals(position)) {
                String bumen = map.containsKey("部门") ? map.get("bumen").toString() : "";
                map.remove("ywyId");
            }
        }


        HashMap<String, Object> result = plan1Mapper.statistics(map);
        return (JSONObject) JSONObject.toJSON(result);
    }

    @Override
    public Plan1 savePlan1OneKey(Plan1 plan1, Long nowDate, String name, String name2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(nowDate);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);

        plan1.setGate(4);
        plan1.setSort1(1);

        try {
            plan1.setDate1(sdf.parse(sdf.format(date)));
        } catch (ParseException e) {
        }
        plan1.setDate8(new Date(nowDate));
        Date baiFangDate = new Date(nowDate);
        plan1.setDate7(baiFangDate);
        plan1.setIsxunhuan(0);
        plan1.setStarttime1(instance.get(Calendar.HOUR_OF_DAY) - 1 + "");
        plan1.setStarttime2(instance.get(Calendar.MINUTE) + "");
        plan1.setTime1(instance.get(Calendar.HOUR_OF_DAY) + 1 + "");
        plan1.setTime2(instance.get(Calendar.MINUTE) + "");
        plan1.setComplete(1 + "");
        plan1.setCateid3(3);
        plan1.setComplete("2");

        plan1.setOption1(0);
        JSONArray arr = new JSONArray();
        JSONObject o = new JSONObject();
        o.put(name, plan1.getIntro());
        arr.add(o);
        plan1.setIntro2(arr.toString());
        //去匹配有没有关联拜访的
        //1.主拜访人员
        try {
            Integer compnayId = plan1.getCompany();
            if (plan1.getIsPeitong() == 1 && !StrUtils.isNull(plan1.getOthers())) {

                //根据公司id和最近三天查询有没有陪同
                Condition c = new Condition(Plan1.class);
                Example.Criteria criteria = c.createCriteria();
                criteria.andEqualTo("company", compnayId);
                criteria.andLessThan("date7", new Date(baiFangDate.getTime() + 3600 * 24 * 1000 * 2));
                criteria.andGreaterThan("date7", new Date(baiFangDate.getTime() - 3600 * 24 * 1000 * 2));
                criteria.andEqualTo("isPeitong", 0);
                List<Plan1> plan1s = plan1Mapper.selectByCondition(c);
                if (!CollectionUtils.isEmpty(plan1s)) {
                    JSONArray array = JSONArray.parseArray(plan1.getIntro2());
                    for (Plan1 plan11 : plan1s) {
                        String intro = plan11.getIntro();
                        Gate byId = gateService.findById(plan11.getCateid());
                        JSONObject o1 = new JSONObject();
                        o1.put(byId.getUsername(), intro);
                        array.add(o1);
                    }
                    plan1.setIntro2(array.toString());

                }
                //陪同人员
            } else if (plan1.getIsPeitong() == 0) {
                Condition c = new Condition(Plan1.class);
                Example.Criteria criteria = c.createCriteria();
                criteria.andEqualTo("company", compnayId);
                criteria.andLessThan("date7", new Date(baiFangDate.getTime() + 3600 * 24 * 1000 * 2));
                criteria.andGreaterThan("date7", new Date(baiFangDate.getTime() - 3600 * 24 * 1000 * 2));
                criteria.andEqualTo("isPeitong", 1);
                List<Plan1> plan1s = plan1Mapper.selectByCondition(c);
                if (!CollectionUtils.isEmpty(plan1s)) {

                    Plan1 plan11 = plan1s.get(0);
                    if (plan11.getOthers().contains(name)) {
                        String intro2 = plan11.getIntro2();
                        JSONArray array1 = JSONArray.parseArray(intro2);
                        JSONObject object = new JSONObject();
                        object.put(name, plan1.getIntro());
                        array1.add(object);
                        plan11.setIntro2(array1.toString());
                        plan1Mapper.updateByPrimaryKeySelective(plan11);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("关联陪同人员失败");
            e.printStackTrace();
        }


        try {
            plan1.setDate4(sdf.parse(sdf.format(date)));
            plan1.setStartdate1(sdf.parse(sdf.format(date)));
        } catch (ParseException e) {
        }
        plan1.setOrder1(plan1.getCateid() + "");
        int i = plan1Mapper.insertUseGeneratedKeys(plan1);

        //2.保存洽谈
        Calendar c = Calendar.getInstance();
        Reply reply = new Reply();
        c.setTime(date);
        reply.setDate7(date);
        reply.setSort1(1);
        reply.setTime1(c.get(Calendar.HOUR_OF_DAY));
        reply.setIntro(plan1.getIntro());
        reply.setPlan1(plan1.getOrd());
        reply.setDel(1);
        reply.setOrd(plan1.getCompany());
        reply.setOrd2(plan1.getPerson());
        reply.setCateid(Integer.parseInt(plan1.getOrder1()));
        reply.setName(name);
        reply.setName2(name2);
        //设置是否为新客户
        List<HashMap<String, Object>> tels = telMapper.selectByPrimaryKeyInfo(plan1.getCompany());
        if (!CollectionUtils.isEmpty(tels)) {
            HashMap<String, Object> stringObjectHashMap = tels.get(0);
            String fenzu = stringObjectHashMap.get("fenzu") + "";
            String companyName = stringObjectHashMap.get("name") + "";
            String xin = stringObjectHashMap.get("xin") + "";
            if (fenzu.equals("客户")) {
                try {
                    if (StatisticsUtils.isNewCustomer(companyName, date)) {
                        reply.setIsNew(1);
                    } else {
                        reply.setIsNew(0);
                    }
                } catch (Exception e) {

                }
            } else if (fenzu.equals("供应商")) {
                try {
                    if (StatisticsUtils.isNewGys(companyName, date)) {
                        reply.setIsNew(1);
                    } else {
                        reply.setIsNew(0);
                    }
                } catch (Exception e) {

                }
            } else {
                try {
                    if (StatisticsUtils.isNewCustomer(companyName, date)) {
                        reply.setIsNew(1);
                        if (StatisticsUtils.isNewGys(companyName, date)) {
                            reply.setIsNew(1);
                        } else {
                            reply.setIsNew(0);
                        }
                    } else {
                        reply.setIsNew(0);
                    }

                } catch (Exception e) {

                }

            }

            if (StrUtils.isNull(xin)) {
                ErpCustomvalues erpCustomvalues = new ErpCustomvalues();
                erpCustomvalues.setFvalue("是");
                erpCustomvalues.setOrderid(plan1.getCompany());
                erpCustomvalues.setFieldsid(15);
                erpCustomvaluesMapper.insert(erpCustomvalues);
            } else if (!xin.equals(reply.getIsNew() == 1 ? "是" : "否")) {
                erpCustomvaluesMapper.upDateFileds(15, plan1.getCompany(), reply.getIsNew() == 1 ? "是" : "否");
            }

        }

        replyMapper.insertSelective(reply);

        return plan1;
    }


    @Override
    public Plan1 upDatePlan1OneKey(Plan1 plan1, Long nowDate, String name, String name2, Integer replyId) {


        //1.非本人不能修改
        //当前登录的用户id

        Integer cateId = plan1.getCateid();
        Plan1 plan112 = plan1Mapper.selectByPrimaryKey(plan1.getOrd());
        if (cateId.intValue() != plan112.getCateid().intValue()) {
            throw new ServiceException("非本人不能修改记录!");
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(nowDate);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);

        plan1.setGate(4);
        plan1.setSort1(1);

        try {
            plan1.setDate1(sdf.parse(sdf.format(date)));
        } catch (ParseException e) {
        }
        plan1.setDate8(new Date(nowDate));
        plan1.setDate7(new Date(nowDate));
        plan1.setIsxunhuan(0);
        plan1.setStarttime1(instance.get(Calendar.HOUR_OF_DAY) - 1 + "");
        plan1.setStarttime2(instance.get(Calendar.MINUTE) + "");
        plan1.setTime1(instance.get(Calendar.HOUR_OF_DAY) + 1 + "");
        plan1.setTime2(instance.get(Calendar.MINUTE) + "");
        plan1.setComplete(1 + "");
        plan1.setCateid3(3);
        plan1.setComplete("2");

        Date baiFangDate = new Date(nowDate);

        JSONArray arr = new JSONArray();
        JSONObject o = new JSONObject();
        o.put(name, plan1.getIntro());
        arr.add(o);
        plan1.setIntro2(arr.toString());
        //去匹配有没有关联拜访的
        //1.主拜访人员
        try {
            Integer compnayId = plan1.getCompany();
            if (plan1.getIsPeitong() == 1 && !StrUtils.isNull(plan1.getOthers())) {

                //根据公司id和最近三天查询有没有陪同
                Condition c = new Condition(Plan1.class);
                Example.Criteria criteria = c.createCriteria();
                criteria.andEqualTo("company", compnayId);
                criteria.andLessThan("date7", new Date(baiFangDate.getTime() + 3600 * 24 * 1000 * 2));
                criteria.andGreaterThan("date7", new Date(baiFangDate.getTime() - 3600 * 24 * 1000 * 2));
                criteria.andEqualTo("isPeitong", 0);
                List<Plan1> plan1s = plan1Mapper.selectByCondition(c);
                if (!CollectionUtils.isEmpty(plan1s)) {
                    JSONArray array = JSONArray.parseArray(plan1.getIntro2());
                    for (Plan1 plan11 : plan1s) {
                        String intro = plan11.getIntro();
                        Gate byId = gateService.findById(plan11.getCateid());
                        JSONObject o1 = new JSONObject();
                        o1.put(byId.getUsername(), intro);
                        array.add(o1);
                    }
                    plan1.setIntro2(array.toString());

                }
                //陪同人员
            } else if (plan1.getIsPeitong() == 0) {
                Condition c = new Condition(Plan1.class);
                Example.Criteria criteria = c.createCriteria();
                criteria.andEqualTo("company", compnayId);
                criteria.andLessThan("date7", new Date(baiFangDate.getTime() + 3600 * 24 * 1000 * 2));
                criteria.andGreaterThan("date7", new Date(baiFangDate.getTime() - 3600 * 24 * 1000 * 2));
                criteria.andEqualTo("isPeitong", 1);
                List<Plan1> plan1s = plan1Mapper.selectByCondition(c);
                if (!CollectionUtils.isEmpty(plan1s)) {

                    Plan1 plan11 = plan1s.get(0);
                    if (plan11.getOthers().contains(name)) {

                        String intro2 = plan11.getIntro2();

                        JSONArray array = JSONArray.parseArray(intro2);
                        JSONObject x = new JSONObject();
                        x.put(name, plan1.getIntro());
                        Boolean isOk = false;
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            if (jsonObject.keySet().contains(name)) {
                                array.set(i, x);
                                isOk = true;
                                break;
                            }
                        }
                        if (!isOk) {
                            array.add(x);
                        }

                        plan11.setIntro2(array.toString());
                        plan1Mapper.updateByPrimaryKeySelective(plan11);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("关联陪同人员失败");
            e.printStackTrace();
        }


        plan1.setOption1(0);
        try {
            plan1.setDate4(sdf.parse(sdf.format(date)));
            plan1.setStartdate1(sdf.parse(sdf.format(date)));
        } catch (ParseException e) {
        }
        plan1.setOrder1(plan1.getCateid() + "");
        plan1Mapper.updateByPrimaryKeySelective(plan1);

        //2.保存洽谈
        Calendar c = Calendar.getInstance();
        Reply reply = new Reply();
        c.setTime(date);
        reply.setDate7(date);
        reply.setSort1(1);
        reply.setTime1(c.get(Calendar.HOUR_OF_DAY));
        reply.setIntro(plan1.getIntro());
        reply.setPlan1(plan1.getOrd());
        reply.setDel(1);
        reply.setOrd(plan1.getCompany());
        reply.setOrd2(plan1.getPerson());
        reply.setCateid(Integer.parseInt(plan1.getOrder1()));
        reply.setName(name);
        reply.setName2(name2);
        reply.setId(replyId);
        replyMapper.updateByPrimaryKeySelective(reply);

        return plan1;
    }

    @Override
    public String doExport(Date startDate, Date endDate, String ywyIds, String type, String bumen) {

        ThreadPoolExecutor instance = MyThread.getInstance();
        String[] split = ywyIds.split(",");
        List<Integer> ids = new ArrayList<>();
        List<File> zipList = new ArrayList<>();


        File directory = new File("src/main/resources");
        String reportPath = null;
        try {
            reportPath = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f = new File(reportPath + "/file/xxx-合肥圆融供应链管理有限公司拜访记录表.xlsx");


        final int[] allCount = {0};
        for (String s : split) {

            instance.execute(new Runnable() {
                @Override
                public void run() {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 3);
                    Date colseThreeMonth = calendar.getTime();
                    List<HashMap<String, Object>> data = plan1Mapper.selectDateExport(Integer.parseInt(s), startDate, endDate, colseThreeMonth);
                    synchronized (this) {
                        allCount[0] += data.size();
                    }
                    JSONObject res = new JSONObject();
                    //用作去重
                    HashSet<String> hashSet = new HashSet<>();
                    for (HashMap<String, Object> datum : data) {
                        String cateid = datum.get("cateid").toString();
                        String date7 = datum.get("date7").toString().substring(0, 10);
                        String companyName = datum.get("companyName").toString();
                        String intro = datum.get("intro").toString();
                        String intro2 = datum.get("intro2").toString();
                        String others = datum.containsKey("others") ? datum.get("others").toString() : "";
                        if (!hashSet.add(intro + companyName + date7)) {
                            continue;
                        }
                        JSONObject ywyObj = res.containsKey(cateid) ? res.getJSONObject(cateid) : new JSONObject();
                        JSONObject companyObj = ywyObj.containsKey(companyName) ? ywyObj.getJSONObject(companyName) : new JSONObject();
                        companyObj.put("info", datum);
                        JSONArray baifangArr = companyObj.containsKey("baifang") ? companyObj.getJSONArray("baifang") : new JSONArray();
                        JSONObject object = JSONObject.parseObject(intro);
                        object.put("date", date7);
                        object.put("intro2", intro2);
                        object.put("others", others);
                        baifangArr.add(object);
                        companyObj.put("baifang", baifangArr);
                        ywyObj.put(companyName, companyObj);
                        res.put(cateid, ywyObj);

                    }
                    try {
                        drawBaifangExcel(res, zipList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        instance.shutdown();
        //等待完成
        try {
            instance.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (allCount[0] == 0) {
            for (File file : zipList) {
                file.delete();
            }
            throw new ServiceException("查询条件暂无拜访记录");
        }

        String zipFileName = "";
        if ("zip".equals(type)) {
            String endDateString = new SimpleDateFormat("yyyy年M月d日").format(new Date(endDate.getTime() - 3600 * 24 * 1000));
            zipFileName = endDateString + bumen + "部拜访统计.zip" + "VVV" + UUID.randomUUID().toString();
            try {
                ZipUtils.toZip(zipList, new FileOutputStream((new File(path + "/" + zipFileName))));
                for (File file : zipList) {
                    file.delete();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if ("pdf".equals(type)) {
            String endDateString = new SimpleDateFormat("yyyy年M月d日").format(new Date(endDate.getTime() - 3600 * 24 * 1000));
            zipFileName = endDateString + bumen + "部拜访统计记录.pdf" + "VVV" + UUID.randomUUID().toString();
            PDFMergerUtility mergePdf = new PDFMergerUtility();
            mergePdf.setDestinationFileName(path + "/" + zipFileName);
            ArrayList<String> fileList = new ArrayList<>();
            for (File file : zipList) {
                String path0 = path + "/" + UUID.randomUUID().toString() + ".pdf";
                Excel2PDF.excel2pdf(file.getAbsolutePath(), path0);
                mergePdf.addSource(path0);
                fileList.add(path0);
                file.delete();

            }
            try {
                mergePdf.mergeDocuments();
                PDFUtils.addPageNum(path + "/" + zipFileName, path + "/" + zipFileName + "_1");

                new File(path + "/" + zipFileName).delete();
                zipFileName += "_1";

            } catch (IOException e) {
                e.printStackTrace();
            } catch (COSVisitorException e) {
                e.printStackTrace();
            } finally {
                for (String s : fileList) {
                    new File(s).delete();
                }
            }

        }
        return zipFileName;
    }


    public void drawBaifangExcel(JSONObject res, List<File> zipList) throws FileNotFoundException {


        File directory = new File("src/main/resources");
        String reportPath = null;
        try {
            reportPath = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f = new File(reportPath + "/file/xxx-合肥圆融供应链管理有限公司拜访记录表.xlsx");
        Set<String> strings = res.keySet();
        for (String cateId : strings) {
            JSONObject ywyObj = res.getJSONObject(cateId);
            Set<String> companyName = ywyObj.keySet();
            String fileName = f.getName();
            Workbook workbook = new Workbook();

            workbook.loadFromStream(new FileInputStream(f));
            WorksheetsCollection worksheets = workbook.getWorksheets();
            Worksheet sheet = worksheets.get("sheet");
            for (String s : companyName) {
                if (StrUtils.isNull(s.trim()) || s.length() > 30) {
                    continue;
                }
                JSONObject company = ywyObj.getJSONObject(s);
                HashMap<String, Object> info = (HashMap<String, Object>) company.get("info");
                String ywy = info.get("ywy").toString();
                fileName = fileName.replace("xxx", ywy);


                //拿到样例子工作表

                Worksheet emptySheet = workbook.createEmptySheet(s.replace("/", "|"));
                emptySheet.copyFrom(sheet);


//                emptySheet.getAllocatedRange().autoFitRows();

                //设置拜访人姓名
                emptySheet.getCellRange(2, 3).setText(info.get("ywy").toString());

                //所在部门
                if (info.get("sorce").toString().equals("2")) {
                    emptySheet.getCellRange(2, 6).setText("塑料部");
                } else if (info.get("sorce").toString().equals("3")) {
                    emptySheet.getCellRange(2, 6).setText("钢材部");
                }
                //企业全称
                emptySheet.getCellRange(4, 3).setText(s);
                //企业详细地址
                emptySheet.getCellRange(5, 3).setText(StrUtils.isNull(StrUtils.isNull(info.get("address") + "") ? "" : info.get("address") + "") ? "" : info.get("address").toString());

                //企业基本情况
                String leixing = info.get("leixing").toString();
                CellRange cellRange = emptySheet.getCellRange(6, 3);
                switch (leixing) {
                    case "客户":
                        cellRange.setText("■客户          □供应商          □二者皆是");
                        break;
                    case "供应商":
                        cellRange.setText("□客户          ■供应商          □二者皆是");
                        break;
                    case "二者皆是":
                        cellRange.setText("□客户          □供应商          ■二者皆是");
                        break;
                }
                //是否是新客户
                String isNew = info.get("isNew").toString();
                CellRange cellRange1 = emptySheet.getCellRange(7, 3);
                if (isNew.equals("是")) {
                    cellRange1.setText("■是            □否");
                } else {
                    cellRange1.setText("□是            ■否");
                }
                //企业人数
                emptySheet.getCellRange(8, 3).setText(StrUtils.isNull(info.get("companyPersonNum") + "") ? "" : info.get("companyPersonNum") + "");
                //企业规模
                emptySheet.getCellRange(8, 6).setText(StrUtils.isNull(info.get("companyGuimo") + "") ? "" : info.get("companyGuimo") + "");
                //联系人信息
                emptySheet.getCellRange(9, 3).setText("姓名：" + (StrUtils.isNull(info.get("linkName") + "") ? "" : info.get("linkName")) + "          性别：" + (StrUtils.isNull(info.get("linkSex") + "") ? "" : info.get("linkSex")) +
                        "      部门及职位： " + (StrUtils.isNull(info.get("linkManJob") + "") ? "" : info.get("linkManJob")) + "     手机号码：" + (StrUtils.isNull(info.get("mobile") + "") ? "" : info.get("mobile")));
                //爱好
                emptySheet.getCellRange(10, 3).setText("爱好及其他信息： " + (StrUtils.isNull(info.get("linkHobby") + "") ? "" : info.get("linkHobby")));
                //月平均原料用量（吨）
                emptySheet.getCellRange(11, 3).setText(StrUtils.isNull(info.get("yzjyl") + "") ? "" : info.get("yzjyl") + "");
                //我司月平均合作数量（吨）
                emptySheet.getCellRange(11, 6).setText(StrUtils.isNull(info.get("wshz") + "") ? "" : info.get("wshz") + "");
                try {
                    //企业主要上游供应商及供应材料
                    JSONArray array = JSONArray.parseArray(info.get("shangyou").toString());
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        emptySheet.getCellRange(12 + i, 4).setText(jsonObject.containsKey("name") ? jsonObject.getString("name") : "");
                        emptySheet.getCellRange(12 + i, 6).setText(jsonObject.containsKey("material") ? jsonObject.getString("material") : "");
                    }
                } catch (Exception e) {
                }
                try {
                    //企业主要下游客户及供应材料
                    JSONArray array = JSONArray.parseArray(info.get("xiayou").toString());
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        emptySheet.getCellRange(15 + i, 4).setText(jsonObject.containsKey("name") ? jsonObject.getString("name") : "");
                        emptySheet.getCellRange(15 + i, 6).setText(jsonObject.containsKey("material") ? jsonObject.getString("material") : "");
                    }
                } catch (Exception e) {
                }
                try {
                    //企业主要下游客户及供应材料
                    JSONArray array = JSONArray.parseArray(info.get("qita").toString());
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String sort = jsonObject.containsKey("sort") ? jsonObject.getString("sort") : "";
                        String grade = jsonObject.containsKey("grade") ? jsonObject.getString("grade") : "";
                        String background = jsonObject.containsKey("background") ? jsonObject.getString("background") : "";
                        String used = jsonObject.containsKey("used") ? jsonObject.getString("used") : "";
                        emptySheet.getCellRange(18 + i, 3).setText("类别：" + sort + "      牌号及月用量：" + grade + " " + used + "          竞争供应商及背景：" + background);
                    }
                } catch (Exception e) {
                }

                //拜访总结
                //1.拜访日期以及陪同人
                CellRange tempRange = emptySheet.getCellRange(22, 3);
                tempRange.autoFitRows();
                tempRange.isWrapText(true);
                JSONArray arr = (JSONArray) company.get("baifang");
                for (int i = 0; i < arr.size(); i++) {


                    if (i >= 1) {
                        emptySheet.copy(emptySheet.getCellRange(23, 2, 26, 6), emptySheet.getCellRange(23 + 4 * i, 2, 26 + 4 * i, 6), true);
                        emptySheet.getCellRange(23 + 4 * i, 2, 23 + 4 * i + 3, 2).merge();
                        emptySheet.getCellRange(23 + 4 * i, 3, 23 + 4 * i + 3, 3).merge();
                        for (int j = 0; j < 4; j++) {
                            CellRange cellRange2 = emptySheet.getCellRange(23 + 4 * i + j, 5, 23 + 4 * i + j, 6);
                            cellRange2.merge();
                            cellRange2.isWrapText(true);
                            cellRange2.autoFitColumns();
                            cellRange2.autoFitRows();

                        }
                    }


                    JSONObject jsonObject = arr.getJSONObject(i);
                    String date = jsonObject.getString("date");


                    try {
                        JSONArray intro2 = JSONArray.parseArray(jsonObject.get("intro2").toString());
                        String ywyS = intro2.size() > 1 ? ywy + ":" : "";

                        String result = jsonObject.containsKey("result") ? ywyS + jsonObject.getString("result") : "";
                        String plan = jsonObject.containsKey("plan") ? ywyS + jsonObject.getString("plan") : "";
                        String target = jsonObject.containsKey("target") ? ywyS + jsonObject.getString("target") : "";
                        String happen = jsonObject.containsKey("happen") ? ywyS + jsonObject.getString("happen") : "";
                        String others = jsonObject.get("others") + "";
                        for (int j = 1; j < intro2.size(); j++) {
                            JSONObject jsonObject1 = intro2.getJSONObject(j);
                            String peiTongName = jsonObject1.keySet().iterator().next();
                            JSONObject jsonObject2 = jsonObject1.getJSONObject(peiTongName);

                            result += ((jsonObject2.containsKey("result") ? "\r\n" + peiTongName + "(陪同):" + jsonObject2.getString("result") : ""));
                            plan += ((jsonObject2.containsKey("plan") ? "\r\n" + peiTongName + "(陪同):" + jsonObject2.getString("plan") : ""));
                            target += ((jsonObject2.containsKey("target") ? "\r\n" + peiTongName + "(陪同):" + jsonObject2.getString("target") : ""));
                            happen += ((jsonObject2.containsKey("happen") ? "\r\n" + peiTongName + "(陪同):" + jsonObject2.getString("happen") : ""));

                        }


                        if (!StrUtils.isNull(others)) {
                            emptySheet.getCellRange(23 + (4 * i), 2).setText(date + "（陪同人：" + others + "）");
                        } else {
                            emptySheet.getCellRange(23 + (4 * i), 2).setText(date);
                        }


                        CellRange rangTarger = emptySheet.getCellRange(23 + (4 * i), 5);
                        rangTarger.setText(target);
                        tempRange.setText(target);
                        tempRange.autoFitRows();
                        rangTarger.setRowHeight(tempRange.getRowHeight() * intro2.size() / 2.5 > 30 ? tempRange.getRowHeight() * intro2.size() / 2 : 30);


                        CellRange resultRanger = emptySheet.getCellRange(24 + (4 * i), 5);
                        resultRanger.setText(result);
                        tempRange.setText(result);
                        tempRange.autoFitRows();
                        resultRanger.setRowHeight(tempRange.getRowHeight() * intro2.size() / 2.5 > 30 ? tempRange.getRowHeight() * intro2.size() / 2.5 : 30);

                        CellRange happenRanger = emptySheet.getCellRange(25 + (4 * i), 5);
                        happenRanger.setText(happen);
                        tempRange.setText(happen);
                        tempRange.autoFitRows();
                        happenRanger.setRowHeight(tempRange.getRowHeight() * intro2.size() / 2.5 > 49.5 ? tempRange.getRowHeight() * intro2.size() / 2.5 : 49.5);

                        CellRange planRanger = emptySheet.getCellRange(26 + (4 * i), 5);
                        planRanger.setText(plan);
                        tempRange.setText(plan);
                        tempRange.autoFitRows();
                        planRanger.setRowHeight(tempRange.getRowHeight() * intro2.size() / 2.5 > 60.75 ? tempRange.getRowHeight() * intro2.size() / 2.5 : 60.75);
                    } catch (Exception e) {
                        String result = jsonObject.containsKey("result") ? jsonObject.getString("result") : "";
                        String plan = jsonObject.containsKey("plan") ? jsonObject.getString("plan") : "";
                        String target = jsonObject.containsKey("target") ? jsonObject.getString("target") : "";
                        String happen = jsonObject.containsKey("happen") ? jsonObject.getString("happen") : "";
                        String others = jsonObject.get("others") + "";

                        if (!StrUtils.isNull(others)) {
                            emptySheet.getCellRange(23 + (4 * i), 2).setText(date + "（陪同人：" + others + "）");
                        } else {
                            emptySheet.getCellRange(23 + (4 * i), 2).setText(date);
                        }


                        CellRange rangTarger = emptySheet.getCellRange(23 + (4 * i), 5);
                        rangTarger.setText(target);
                        tempRange.setText(target);
                        tempRange.autoFitRows();
                        rangTarger.setRowHeight(tempRange.getRowHeight() / 2.5 > 30 ? tempRange.getRowHeight() / 2 : 30);


                        CellRange resultRanger = emptySheet.getCellRange(24 + (4 * i), 5);
                        resultRanger.setText(result);
                        tempRange.setText(result);
                        tempRange.autoFitRows();
                        resultRanger.setRowHeight(tempRange.getRowHeight() / 2.5 > 30 ? tempRange.getRowHeight() / 2.5 : 30);

                        CellRange happenRanger = emptySheet.getCellRange(25 + (4 * i), 5);
                        happenRanger.setText(happen);
                        tempRange.setText(happen);
                        tempRange.autoFitRows();
                        happenRanger.setRowHeight(tempRange.getRowHeight() / 2.5 > 49.5 ? tempRange.getRowHeight() / 2.5 : 49.5);

                        CellRange planRanger = emptySheet.getCellRange(26 + (4 * i), 5);
                        planRanger.setText(plan);
                        tempRange.setText(plan);
                        tempRange.autoFitRows();
                        planRanger.setRowHeight(tempRange.getRowHeight() / 2.5 > 60.75 ? tempRange.getRowHeight() / 2.5 : 60.75);
                    }


                }

                tempRange.setText("是否宴请或送礼");
                tempRange.setRowHeight(25.5);


            }
            sheet.remove();
            File file = new File(path + "/" + fileName);
            synchronized (Plan1.class) {
                workbook.saveToFile(path + "/" + fileName);
                zipList.add(file);

            }


        }


    }

    @SneakyThrows
    public static void main(String[] args) {
        Workbook workbook = new Workbook();
        workbook.loadFromStream(new FileInputStream("C:\\Users\\Administrator\\Desktop\\1.xlsx"));
        WorksheetsCollection worksheets = workbook.getWorksheets();
        Worksheet worksheet = worksheets.get(0);
        System.out.println(worksheet.getCellRange(25, 4).getRowHeight());
        System.out.println(worksheet.getCellRange(23, 4).getRowHeight());
        System.out.println(worksheet.getCellRange(26, 4).getRowHeight());
    }

}
