package com.company.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.Constants;
import com.company.project.dao.GateMapper;
import com.company.project.dao.Plan1Mapper;
import com.company.project.dao.ReplyMapper;
import com.company.project.service.StatisticsService;
import com.company.project.utils.erp.ErpDataUtils;
import com.company.project.utils.erp.StatisticsUtils;
import com.company.project.utils.string.NumberUtils;
import com.spire.xls.CellRange;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.spire.xls.collections.WorksheetsCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private Plan1Mapper plan1Mapper;

    @Autowired
    private GateMapper gateMapper;

    @Override
    public JSONObject getSaleManDataStatistics(String type, Date startDate, Date endDate) throws ParseException {

        //1,根据日期范围获得日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        //客户拜访记录
        List<Map<Object, Object>> visitList = new ArrayList<>();


        //拿到业务员列表
        List<String> ywyListS = new ArrayList<>();

        JSONArray dateByParam = new JSONArray();
        if ("塑料".equals(type)) {
            String[] fields = {"FSalerId.FName", "FPriceUnitQty"};
            String condition = "FDate>='" + sdf.format(startDate) + "' and FDate<= '" + sdf.format(endDate) + "' and FCustId.FName not in('第八元素环境技术有限公司','期初资源调整','合肥圆融供应链管理有限公司','HONGKONG WINYOND CO.,LIMILED') and FPriceUnitQty>0.001  and   FSaleDeptId.Fname in('电商部-深圳中心','电商部-业务中心')";

            String order = "FDate desc";
            dateByParam = ErpDataUtils.getDateByParam("SAL_SaleOrder", order, fields, condition);

            visitList = replyMapper.selectGroupByYwy(startDate, endDate, "2");
            ywyListS = replyMapper.getYwyList("2");

        } else if ("钢材".equals(type)) {
            String[] fields = {"FSalerId.FName", "FPriceUnitQty"};
            String condition = "FDate>='" + sdf.format(startDate) + "' and FDate<= '" + sdf.format(endDate) + "' and FCustId.FName not in('第八元素环境技术有限公司','期初资源调整','合肥圆融供应链管理有限公司','HONGKONG WINYOND CO.,LIMILED') and FPriceUnitQty>0.001  and   FSaleDeptId.Fname like'钢材%'";

            String order = "FDate desc";
            dateByParam = ErpDataUtils.getDateByParam("SAL_SaleOrder", order, fields, condition);
            visitList = replyMapper.selectGroupByYwy(startDate, endDate, "3");
            ywyListS = replyMapper.getYwyList("3");
        }
        //1.得到每位销售员的销售数据
        JSONObject res = new JSONObject();

        final String SALECOUNKEY = "saleCount";

        //2.得到每个业务员的拜访量

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        //用户成交状态
        JSONObject slCustomerStatusDate = StatisticsUtils.getCUSTOMERSTATUS();


        for (Map<Object, Object> o : visitList) {

            Integer times = 0;
            String ywy = o.get("ywy").toString();


            String isNew = "1";
            try {
                times = Integer.parseInt(o.get("times").toString());
                isNew = o.get("is_new").toString();
            } catch (Exception e) {

            }


            JSONObject ywyObj;
            if (res.containsKey(ywy)) {
                ywyObj = res.getJSONObject(ywy);
            } else {
                ywyObj = new JSONObject();
                ywyObj.put(SALECOUNKEY, 0);
            }
            Integer newVisit = ywyObj.getInteger("newVisit");
            Integer oldVisit = ywyObj.getInteger("oldVisit");
            if ("1".equals(isNew)) {
                ywyObj.put("newVisit", newVisit == null ? times : times + newVisit);
                res.put(ywy, ywyObj);
                continue;
            } else if ("0".equals(isNew)) {
                ywyObj.put("oldVisit", newVisit == null ? times : times + oldVisit);
                res.put(ywy, ywyObj);
                continue;
            }
            res.put(ywy, ywyObj);

        }


        for (String ywyList : ywyListS) {
            if (!res.containsKey(ywyList)) {
                JSONObject ywyObj = new JSONObject();
                ywyObj.put(SALECOUNKEY, 0);
                ywyObj.put("newVisit", 0);
                ywyObj.put("oldVisit", 0);
                res.put(ywyList, ywyObj);
            }

        }
        for (int i = 0; i < dateByParam.size(); i++) {
            JSONArray jsonArray = dateByParam.getJSONArray(i);
            String ywy = jsonArray.getString(0);
            if (!res.containsKey(ywy)) {
                continue;
            }


            Double sl = jsonArray.getDouble(1) / 1000;

            JSONObject ywyObj = res.containsKey(ywy) ? res.getJSONObject(ywy) : new JSONObject();
            ywyObj.put(SALECOUNKEY, NumberUtils.add(sl, ywyObj.containsKey(SALECOUNKEY) ? ywyObj.getDouble(SALECOUNKEY) : 0));
            res.put(ywy, ywyObj);
        }


        //对 res 的key(业务员)按照拜访总量统计
        Set<String> strings = res.keySet();
        List<String> ywyList = new ArrayList<>();
        ywyList.addAll(strings);
        Collections.sort(ywyList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                JSONObject jsonObject1 = res.getJSONObject(o1);
                Integer newVisit1 = jsonObject1.getInteger("newVisit");
                Integer oldVisit1 = jsonObject1.getInteger("oldVisit");
                Integer all1 = (newVisit1 == null ? 0 : newVisit1) + (oldVisit1 == null ? 0 : oldVisit1);
                JSONObject jsonObject2 = res.getJSONObject(o2);
                Integer newVisit2 = jsonObject2.getInteger("newVisit");
                Integer oldVisit2 = jsonObject2.getInteger("oldVisit");
                Integer all2 = (newVisit2 == null ? 0 : newVisit2) + (oldVisit2 == null ? 0 : oldVisit2);
                return all1 - all2;

            }
        });

        System.out.println(ywyList);
        JSONObject res_obj = new JSONObject();
        JSONArray series = new JSONArray();

        JSONObject x = new JSONObject();
        x.put("name", "x");
        x.put("data", ywyList);


        //新拜访数数组
        JSONArray newArr = new JSONArray();
        //老拜访数数组
        JSONArray oldArr = new JSONArray();
        //订单销量数组
        JSONArray orderSaleArr = new JSONArray();


        //遍历每个业务员的数据,重新组装接送
        for (String s : ywyList) {
            JSONObject jsonObject = res.getJSONObject(s);
            newArr.add(jsonObject.containsKey("newVisit") ? jsonObject.getIntValue("newVisit") : 0);
            oldArr.add(jsonObject.containsKey("oldVisit") ? jsonObject.getIntValue("oldVisit") : 0);
            orderSaleArr.add(jsonObject.get(SALECOUNKEY));
        }

        JSONObject newVivist = new JSONObject();
        newVivist.put("name", "新客户拜访");
        newVivist.put("data", newArr);


        JSONObject oldVivist = new JSONObject();
        oldVivist.put("name", "老客户拜访");
        oldVivist.put("data", oldArr);

        JSONObject saleorder = new JSONObject();

        saleorder.put("name", "订单销量");
        saleorder.put("data", orderSaleArr);


        series.add(x);
        series.add(newVivist);
        series.add(oldVivist);
        series.add(saleorder);
        res_obj.put("series", series);

        return res_obj;
    }

    @Override
    public JSONObject getcustomerVisitStatistics(String type, Date startDate, Date endDate) {
        JSONArray dateByParam = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //1.获取根据客户数量
        List<Map<Object, Object>> visitList = new LinkedList<>();
        //拿到业务员列表
        List<String> ywyListS = new ArrayList<>();
        if ("塑料".equals(type)) {
            String[] fields = {"FSalerId.FName", "FCustId.FName"};
            String condition = "FDate>='" + sdf.format(startDate) + "' and FDate<= '" + sdf.format(endDate) + "' and FCustId.FName not in('第八元素环境技术有限公司','个人','合肥融达环境技术有限公司','期初资源调整','合肥圆融供应链管理有限公司','HONGKONG WINYOND CO.,LIMILED') and FPriceUnitQty>0.001  and   FSaleDeptId.Fname in('电商部-深圳中心','电商部-业务中心')";

            String order = "FDate desc";
            dateByParam = ErpDataUtils.getDateByParam("SAL_SaleOrder", order, fields, condition);

            visitList = replyMapper.selectVisitCountGroupByYwy(startDate, endDate, "2");
            ywyListS = replyMapper.getYwyList("2");

        } else if ("钢材".equals(type)) {
            String[] fields = {"FSalerId.FName", "FCustId.FName"};
            String condition = "FDate>='" + sdf.format(startDate) + "' and FDate<= '" + sdf.format(endDate) + "' and FCustId.FName not in('第八元素环境技术有限公司','个人','合肥融达环境技术有限公司','期初资源调整','合肥圆融供应链管理有限公司','HONGKONG WINYOND CO.,LIMILED') and FPriceUnitQty>0.001  and   FSaleDeptId.Fname like'钢材%'";

            String order = "FDate desc";
            dateByParam = ErpDataUtils.getDateByParam("SAL_SaleOrder", order, fields, condition);
            visitList = replyMapper.selectVisitCountGroupByYwy(startDate, endDate, "3");
            ywyListS = replyMapper.getYwyList("3");
        }

        JSONObject res = new JSONObject();

        //拿到业务员拜访客户次数
        for (Map<Object, Object> objectObjectMap : visitList) {
            String ywy = objectObjectMap.get("ywy").toString();
            Integer times = Integer.parseInt(objectObjectMap.get("times").toString());
            JSONObject object = res.containsKey(ywy) ? res.getJSONObject(ywy) : new JSONObject();
            object.put("visitCustomerCount", times);
            res.put(ywy, object);
        }
        for (String ywyList : ywyListS) {
            if (!res.containsKey(ywyList)) {
                JSONObject ywyObj = new JSONObject();
                ywyObj.put("visitCustomerCount", 0);
                res.put(ywyList, ywyObj);
            }

        }

        //全部的销售数据
        JSONObject customerstatus = StatisticsUtils.getCUSTOMERSTATUS();

        //拿到业务员的销售数据,统计客户个数
        for (int i = 0; i < dateByParam.size(); i++) {
            JSONArray jsonArray = dateByParam.getJSONArray(i);
            String ywy = jsonArray.getString(0);
            if (!res.containsKey(ywy)) {
                continue;
            }
            String custName = jsonArray.getString(1);
            JSONObject object = res.containsKey(ywy) ? res.getJSONObject(ywy) : new JSONObject();

            HashSet<String> custSet = object.containsKey("custSet") ? (HashSet<String>) object.get("custSet") : new HashSet<>();
            if (custSet.add(custName)) {
                try {
                    JSONArray date = customerstatus.getJSONObject(custName).getJSONArray("date");
                    String string = date.getString(date.size() - 1);
                    if (string.compareTo(sdf.format(startDate)) > 0) {
                        object.put("AddCustomerCount", 1 + (object.containsKey("AddCustomerCount") ? object.getIntValue("AddCustomerCount") : 0));
                    }
                } catch (Exception e) {
                    object.put("AddCustomerCount", 1 + (object.containsKey("AddCustomerCount") ? object.getIntValue("AddCustomerCount") : 0));

                }
            }
            object.put("custSet", custSet);
            res.put(ywy, object);
        }


        Set<String> ywySet = res.keySet();
        List<String> ywyList = new ArrayList<>();
        ywyList.addAll(ywySet);
        Collections.sort(ywyList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Integer b1 = res.getJSONObject(o1).containsKey("visitCustomerCount") ? res.getJSONObject(o1).getIntValue("visitCustomerCount") : 0;
                Integer b2 = res.getJSONObject(o2).containsKey("visitCustomerCount") ? res.getJSONObject(o2).getIntValue("visitCustomerCount") : 0;
                return b1 - b2;
            }
        });


        JSONObject res_obj = new JSONObject();
        JSONArray series = new JSONArray();

        JSONObject x = new JSONObject();
        x.put("name", "x");
        x.put("data", ywyList);


        //拜访数数组
        JSONArray baifangCount = new JSONArray();
        //成交客户数
        JSONArray chengJiaoCount = new JSONArray();
        //新增客户数
        JSONArray addCustomerCount = new JSONArray();


        //遍历每个业务员的数据,重新组装接送
        for (String s : ywyList) {
            JSONObject jsonObject = res.getJSONObject(s);
            addCustomerCount.add(jsonObject.containsKey("AddCustomerCount") ? jsonObject.getIntValue("AddCustomerCount") : 0);
            baifangCount.add(jsonObject.containsKey("visitCustomerCount") ? jsonObject.getIntValue("visitCustomerCount") : 0);
            chengJiaoCount.add(jsonObject.containsKey("custSet") ? jsonObject.getJSONArray("custSet").size() : 0);

        }

        JSONObject baifang = new JSONObject();
        baifang.put("name", "跟进客户数量");
        baifang.put("data", baifangCount);


        JSONObject AddCustomer = new JSONObject();
        AddCustomer.put("name", "新增客户数量");
        AddCustomer.put("data", addCustomerCount);

        JSONObject chengjiao = new JSONObject();

        chengjiao.put("name", "成交客户数量");
        chengjiao.put("data", chengJiaoCount);


        series.add(x);
        series.add(baifang);
        series.add(AddCustomer);
        series.add(chengjiao);
        res_obj.put("series", series);

        return res_obj;


    }

    @Override
    public String getYwyVisitTodayStatistics(String date) throws ParseException {

        Date today = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date + " 23:59:59").getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date siDate = sdf.parse("2020-06-04");

        //获得本年的第一天
        Date year = sdf.parse(new SimpleDateFormat("yyyy").format(today) + "-01-01");
        //获取本月的第一天
        Date month = sdf.parse(new SimpleDateFormat("yyyy-MM").format(today) + "-01");


        List<Map<Object, Object>> yearData = null;
        List<Map<Object, Object>> monthData = null;
        if (year.getTime() < siDate.getTime()) {
            yearData = plan1Mapper.getGroupByYwy(siDate, today);
        } else {
            yearData = plan1Mapper.getGroupByYwy(year, today);
        }
        if (month.getTime() < siDate.getTime()) {
            monthData = plan1Mapper.getGroupByYwy(siDate, today);
        } else {
            monthData = plan1Mapper.getGroupByYwy(month, today);
        }
        List<Map<Object, Object>> todayData = plan1Mapper.getGroupByYwy(new SimpleDateFormat("yyyy-MM-dd").parse(date), today);

        //1.获取所有业务员

        List<Map<Object, Object>> ywyList = gateMapper.selectSlGandGcywy();

        JSONObject res = new JSONObject();
        for (Map<Object, Object> objectObjectMap : ywyList) {
            Object username = objectObjectMap.get("username");
            Object sorce = objectObjectMap.get("sorce");
            Object del = objectObjectMap.get("del");
            Object workPosition = objectObjectMap.get("workPosition");
            if (sorce == null) {
                continue;
            }
            JSONObject obj = new JSONObject();
            obj.put("sorce", sorce);
            obj.put("del", del);
            obj.put("workPosition", workPosition);
            res.put(username.toString(), obj);
        }

        for (Map<Object, Object> yearDatum : yearData) {
            String name = yearDatum.get("name").toString();
            String isNew = yearDatum.get("isNew").toString();
            Object sl = yearDatum.get("sl");
            JSONObject ywyObj = null;
            if (!res.containsKey(name)) {
                continue;
            }
            ywyObj = res.getJSONObject(name);
            JSONObject yearObj = ywyObj.containsKey("year") ? ywyObj.getJSONObject("year") : new JSONObject();
            yearObj.put(isNew, sl);
            ywyObj.put("year", yearObj);
            res.put(name, ywyObj);
        }
        for (Map<Object, Object> yearDatum : monthData) {
            String name = yearDatum.get("name").toString();
            String isNew = yearDatum.get("isNew").toString();
            Object sl = yearDatum.get("sl");
            JSONObject ywyObj = null;
            if (!res.containsKey(name)) {
                continue;
            }
            ywyObj = res.getJSONObject(name);
            JSONObject yearObj = ywyObj.containsKey("month") ? ywyObj.getJSONObject("month") : new JSONObject();
            yearObj.put(isNew, sl);
            ywyObj.put("month", yearObj);
            res.put(name, ywyObj);
        }
        for (Map<Object, Object> yearDatum : todayData) {
            String name = yearDatum.get("name").toString();
            String isNew = yearDatum.get("isNew").toString();
            Object sl = yearDatum.get("sl");
            JSONObject ywyObj = null;
            if (!res.containsKey(name)) {
                continue;
            }
            ywyObj = res.getJSONObject(name);
            JSONObject yearObj = ywyObj.containsKey("today") ? ywyObj.getJSONObject("today") : new JSONObject();
            yearObj.put(isNew, sl);
            ywyObj.put("today", yearObj);
            res.put(name, ywyObj);
        }


        JSONObject baiFangTongji = Constants.baiFangTongji;
        Set<String> strings = baiFangTongji.keySet();
        for (String ywy : strings) {
            JSONObject jsonObject = baiFangTongji.getJSONObject(ywy);

            if (res.containsKey(ywy)) {
                JSONObject jsonObject1 = res.getJSONObject(ywy);
                if (month.getTime() < siDate.getTime()) {
                    JSONObject month1 = jsonObject.getJSONObject("month");
                    JSONObject month2 = jsonObject1.getJSONObject("month");
                    if (month2 == null) {
                        month2 = month1;
                    } else {
                        month2.put("0", month1.getIntValue("0") + (month2.containsKey("0") ? month2.getIntValue("0") : 0));
                        month2.put("1", month1.getIntValue("1") + (month2.containsKey("1") ? month2.getIntValue("1") : 0));
                    }
                    jsonObject1.put("month", month2);
                    res.put(ywy, jsonObject1);
                }
                if (year.getTime() < siDate.getTime()) {
                    JSONObject month1 = jsonObject.getJSONObject("year");
                    JSONObject month2 = jsonObject1.getJSONObject("year");
                    if (month2 == null) {
                        month2 = month1;
                    } else {
                        month2.put("0", month1.getIntValue("0") + (month2.containsKey("0") ? month2.getIntValue("0") : 0));
                        month2.put("1", month1.getIntValue("1") + (month2.containsKey("1") ? month2.getIntValue("1") : 0));
                    }
                    jsonObject1.put("year", month2);
                    res.put(ywy, jsonObject1);
                }

            } else {
                Calendar instance = Calendar.getInstance();
                instance.setTime(today);
                if (instance.get(Calendar.YEAR) == 2020) {
                    res.put(ywy, jsonObject);
                }
            }

        }


        Set<String> strings1 = res.keySet();
        Object[] objects = strings1.toArray();
        for (Object s : objects) {

            JSONObject jsonObject = res.getJSONObject(s.toString());

            Integer sorce = jsonObject.getIntValue("sorce");
            Integer del = jsonObject.getIntValue("del");
            int workPosition = jsonObject.getIntValue("workPosition");
            int t_0 = 0;
            int t_1 = 0;
            int m_0 = 0;
            int m_1 = 0;
            int y_0 = 0;
            int y_1 = 0;
            try {

            } catch (Exception e) {
                t_0 = jsonObject.getJSONObject("today").getIntValue("0");
            }
            try {
                t_1 = jsonObject.getJSONObject("today").getIntValue("1");
            } catch (Exception e) {

            }
            try {
                m_0 = jsonObject.getJSONObject("month").getIntValue("0");
            } catch (Exception e) {

            }
            try {
                m_1 = jsonObject.getJSONObject("month").getIntValue("1");
            } catch (Exception e) {

            }
            try {
                y_0 = jsonObject.getJSONObject("year").getIntValue("0");
            } catch (Exception e) {

            }
            try {
                y_1 = jsonObject.getJSONObject("year").getIntValue("1");
            } catch (Exception e) {

            }
            if (sorce != 3 && sorce != 2) {

                JSONObject object = res.containsKey("其他") ? res.getJSONObject("其他") : new JSONObject();

                JSONObject mon1 = object.containsKey("month") ? object.getJSONObject("month") : new JSONObject();
                mon1.put("0", m_0 + (mon1.containsKey("0") ? mon1.getIntValue("0") : 0));
                mon1.put("1", m_1 + (mon1.containsKey("1") ? mon1.getIntValue("1") : 0));
                JSONObject year1 = object.containsKey("year") ? object.getJSONObject("year") : new JSONObject();
                year1.put("0", y_0 + (year1.containsKey("0") ? year1.getIntValue("0") : 0));
                year1.put("1", y_1 + (year1.containsKey("1") ? year1.getIntValue("1") : 0));
                JSONObject today1 = object.containsKey("today") ? object.getJSONObject("today") : new JSONObject();
                today1.put("0", t_0 + (today1.containsKey("0") ? today1.getIntValue("0") : 0));
                today1.put("1", t_1 + (today1.containsKey("1") ? today1.getIntValue("1") : 0));
                object.put("sorce", 100);
                object.put("today", today1);
                object.put("year", year1);
                object.put("month", mon1);
                res.put("其他", object);

                res.remove(s);
            } else if (sorce == 3 && (del != 1 || workPosition == 903)) {
                JSONObject object = res.containsKey("钢材其他") ? res.getJSONObject("钢材其他") : new JSONObject();

                JSONObject mon1 = object.containsKey("month") ? object.getJSONObject("month") : new JSONObject();
                mon1.put("0", m_0 + (mon1.containsKey("0") ? mon1.getIntValue("0") : 0));
                mon1.put("1", m_1 + (mon1.containsKey("1") ? mon1.getIntValue("1") : 0));
                JSONObject year1 = object.containsKey("year") ? object.getJSONObject("year") : new JSONObject();
                year1.put("0", y_0 + (year1.containsKey("0") ? year1.getIntValue("0") : 0));
                year1.put("1", y_1 + (year1.containsKey("1") ? year1.getIntValue("1") : 0));
                JSONObject today1 = object.containsKey("today") ? object.getJSONObject("today") : new JSONObject();
                today1.put("0", t_0 + (today1.containsKey("0") ? today1.getIntValue("0") : 0));
                today1.put("1", t_1 + (today1.containsKey("1") ? today1.getIntValue("1") : 0));
                object.put("sorce", 3);
                object.put("today", today1);
                object.put("year", year1);
                object.put("month", mon1);
                res.put("钢材其他", object);

                res.remove(s);
            } else if (sorce == 2 && (del != 1 || workPosition == 903)) {
                JSONObject object = res.containsKey("塑料其他") ? res.getJSONObject("塑料其他") : new JSONObject();

                JSONObject mon1 = object.containsKey("month") ? object.getJSONObject("month") : new JSONObject();
                System.out.println(mon1);
                System.out.println(m_0 + (mon1.containsKey("0") ? mon1.getIntValue("0") : 0));
                System.out.println(m_1 + (mon1.containsKey("1") ? mon1.getIntValue("1") : 0));
                mon1.put("0", m_0 + (mon1.containsKey("0") ? mon1.getIntValue("0") : 0));
                mon1.put("1", m_1 + (mon1.containsKey("1") ? mon1.getIntValue("1") : 0));
                JSONObject year1 = object.containsKey("year") ? object.getJSONObject("year") : new JSONObject();
                year1.put("0", y_0 + (year1.containsKey("0") ? year1.getIntValue("0") : 0));
                year1.put("1", y_1 + (year1.containsKey("1") ? year1.getIntValue("1") : 0));
                JSONObject today1 = object.containsKey("today") ? object.getJSONObject("today") : new JSONObject();
                today1.put("0", t_0 + (today1.containsKey("0") ? today1.getIntValue("0") : 0));
                today1.put("1", t_1 + (today1.containsKey("1") ? today1.getIntValue("1") : 0));
                object.put("sorce", 2);
                object.put("today", today1);
                object.put("year", year1);
                object.put("month", mon1);
                res.put("塑料其他", object);

                res.remove(s);
            }
        }


        System.out.println(res);
        //排序
        List<String> ywyNameList = new ArrayList<>();
        ywyNameList.addAll(res.keySet());
        System.out.println(ywyNameList);
        System.out.println(res);
        Collections.sort(ywyNameList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                JSONObject o1Obj = res.getJSONObject(o1);
                JSONObject o2Obj = res.getJSONObject(o2);
                if (!o1Obj.getString("sorce").equals(o2Obj.getString("sorce"))) {
                    return o2Obj.getString("sorce").compareTo(o1Obj.getString("sorce"));
                } else {

                    if ("钢材其他".equals(o1)) {
                        return 1;
                    } else if ("钢材其他".equals(o2)) {
                        return -1;
                    }
                    if ("塑料其他".equals(o1)) {
                        return 1;
                    } else if ("塑料其他".equals(o2)) {
                        return -1;
                    }

                    if ("沙金宝".equals(o1)) {
                        return -1;
                    } else if ("沙金宝".equals(o2)) {
                        return 1;
                    }
                    if ("李宾".equals(o1)) {
                        return -1;
                    } else if ("李宾".equals(o2)) {
                        return 1;
                    }
                    if ("干娜".equals(o1)) {
                        return -1;
                    } else if ("干娜".equals(o2)) {
                        return 1;
                    }
                    if ("陈黎明".equals(o1)) {
                        return -1;
                    } else if ("陈黎明".equals(o2)) {
                        return 1;
                    }


                    JSONObject mon1 = o1Obj.containsKey("month") ? o1Obj.getJSONObject("month") : new JSONObject();
                    JSONObject mon2 = o2Obj.containsKey("month") ? o2Obj.getJSONObject("month") : new JSONObject();
                    int s1 = (mon1.containsKey("1") ? mon1.getIntValue("1") : 0) + (mon1.containsKey("0") ? mon1.getIntValue("0") : 0);
                    int s2 = (mon2.containsKey("1") ? mon2.getIntValue("1") : 0) + (mon2.containsKey("0") ? mon2.getIntValue("0") : 0);

                    return s2 - s1;


                }
            }
        });
        System.out.println(res);
        //绘制excel表格
        return drawBaiFangExce(res, ywyNameList, today);


    }

    private String drawBaiFangExce(JSONObject res, List<String> ywyNameList, Date today) {
        System.out.println(res.toString());
        File directory = new File("src/main/resources");
        String reportPath = null;
        try {
            reportPath = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(today);
        int i1 = instance.get(Calendar.YEAR);
        File f = new File(reportPath + "/file/合肥圆融2020年xx月客户拜访记录总表.xlsx");
        Workbook workbook = new Workbook();
        try {
            workbook.loadFromStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        WorksheetsCollection worksheets = workbook.getWorksheets();
        Worksheet sheet = worksheets.get(0);
        //大标题
        CellRange cellRange = sheet.getCellRange(1, 2);
        cellRange.setText("合肥圆融客户拜访" + new SimpleDateFormat("yyyy年M月").format(today) + "统计");

        //当天拜访标题
        sheet.getCellRange(2, 5).setText(new SimpleDateFormat("M月d日").format(today) + "拜访数据");

        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int mon = c.get(Calendar.MONTH) + 1;
        int ye = c.get(Calendar.YEAR);

        //当月拜访标题
        sheet.getCellRange(2, 8).setText(mon + "月累计拜访数据");
        //当年拜访标题
        sheet.getCellRange(2, 11).setText(ye + "年累计拜访数据");


        int gannaIndex = ywyNameList.indexOf("干娜");


        int startLine = 4;
        for (int i = 0; i < gannaIndex; i++) {
            String s = ywyNameList.get(i);
            JSONObject jsonObject = res.getJSONObject(s);
            JSONObject todays = jsonObject.containsKey("today") ? jsonObject.getJSONObject("today") : new JSONObject();
            JSONObject month = jsonObject.containsKey("month") ? jsonObject.getJSONObject("month") : new JSONObject();
            JSONObject year = jsonObject.containsKey("year") ? jsonObject.getJSONObject("year") : new JSONObject();
            //姓名
            sheet.getCellRange(startLine, 4).setText(s);
            //当天老客户
            sheet.getCellRange(startLine, 5).setNumberValue(todays.containsKey("0") ? todays.getIntValue("0") : 0);
            //当天新客户
            sheet.getCellRange(startLine, 6).setNumberValue(todays.containsKey("1") ? todays.getIntValue("1") : 0);
            //当天合计
            sheet.getCellRange(startLine, 7).setFormula("=SUM(E" + startLine + ":F" + startLine + ")");

            //当月老客户
            sheet.getCellRange(startLine, 8).setNumberValue(month.containsKey("0") ? month.getIntValue("0") : 0);
            //当月新客户
            sheet.getCellRange(startLine, 9).setNumberValue(month.containsKey("1") ? month.getIntValue("1") : 0);
            //当月合计
            sheet.getCellRange(startLine, 10).setFormula("=SUM(H" + startLine + ":I" + startLine + ")");

            //当年老客户
            sheet.getCellRange(startLine, 11).setNumberValue(year.containsKey("0") ? year.getIntValue("0") : 0);
            //当年新客户
            sheet.getCellRange(startLine, 12).setNumberValue(year.containsKey("1") ? year.getIntValue("1") : 0);
            //当年合计
            sheet.getCellRange(startLine, 13).setFormula("=SUM(K" + startLine + ":L" + startLine + ")");


            startLine++;
            if (i != gannaIndex - 1) {
                sheet.insertRow(startLine);
                sheet.copy(sheet.getCellRange(startLine - 1, 1, startLine - 1, 17), sheet.getCellRange(startLine, 1, startLine, 17), true);
            }

        }


        sheet.getCellRange(4, 2, startLine - 1, 2).merge();


        //算出钢材部合计
        for (int i = 5; i <= 13; i++) {
            char cc = (char) ('A' + i - 1);
            sheet.getCellRange(startLine, i).setFormula("=SUM(" + cc + "" + 4 + ":" + cc + "" + (4 + gannaIndex - 1) + ")");
        }

        startLine++;

        //塑料
        for (int i = gannaIndex; i < ywyNameList.size() - 1; i++) {
            String s = ywyNameList.get(i);
            JSONObject jsonObject = res.getJSONObject(s);
            JSONObject todays = jsonObject.containsKey("today") ? jsonObject.getJSONObject("today") : new JSONObject();
            JSONObject month = jsonObject.containsKey("month") ? jsonObject.getJSONObject("month") : new JSONObject();
            JSONObject year = jsonObject.containsKey("year") ? jsonObject.getJSONObject("year") : new JSONObject();
            //姓名
            sheet.getCellRange(startLine, 4).setText(s);
            //当天老客户
            sheet.getCellRange(startLine, 5).setNumberValue(todays.containsKey("0") ? todays.getIntValue("0") : 0);
            //当天新客户
            sheet.getCellRange(startLine, 6).setNumberValue(todays.containsKey("1") ? todays.getIntValue("1") : 0);
            //当天合计
            sheet.getCellRange(startLine, 7).setFormula("=SUM(E" + startLine + ":F" + startLine + ")");

            //当月老客户
            sheet.getCellRange(startLine, 8).setNumberValue(month.containsKey("0") ? month.getIntValue("0") : 0);
            //当月新客户
            sheet.getCellRange(startLine, 9).setNumberValue(month.containsKey("1") ? month.getIntValue("1") : 0);
            //当月合计
            sheet.getCellRange(startLine, 10).setFormula("=SUM(H" + startLine + ":I" + startLine + ")");

            //当年老客户
            sheet.getCellRange(startLine, 11).setNumberValue(year.containsKey("0") ? year.getIntValue("0") : 0);
            //当年新客户
            sheet.getCellRange(startLine, 12).setNumberValue(year.containsKey("1") ? year.getIntValue("1") : 0);
            //当年合计
            sheet.getCellRange(startLine, 13).setFormula("=SUM(K" + startLine + ":L" + startLine + ")");


            startLine++;
            if (i != ywyNameList.size() - 2) {
                sheet.insertRow(startLine);
                sheet.copy(sheet.getCellRange(startLine - 1, 1, startLine - 1, 17), sheet.getCellRange(startLine, 1, startLine, 17), true);
            }

        }


        //算出塑料部合计
        for (int i = 5; i <= 13; i++) {
            char cc = (char) ('A' + i - 1);
            sheet.getCellRange(startLine, i).setFormula("=SUM(" + cc + "" + (gannaIndex + 4 + 1) + ":" + cc + "" + (startLine - 1) + ")");
        }


        sheet.getCellRange(gannaIndex + 4 + 1, 2, startLine - 1, 2).merge();


        startLine++;
        sheet.insertRow(startLine);
        sheet.copy(sheet.getCellRange(startLine - 1, 1, startLine - 1, 17), sheet.getCellRange(startLine, 1, startLine, 17), true);
        sheet.getCellRange(startLine, 2, startLine, 4).merge();


        JSONObject jsonObject = res.getJSONObject("其他");
        JSONObject todays = jsonObject.containsKey("today") ? jsonObject.getJSONObject("today") : new JSONObject();
        JSONObject month = jsonObject.containsKey("month") ? jsonObject.getJSONObject("month") : new JSONObject();
        JSONObject year = jsonObject.containsKey("year") ? jsonObject.getJSONObject("year") : new JSONObject();
        //姓名
        sheet.getCellRange(startLine, 2).setText("其他部门小计");
        //当天老客户
        sheet.getCellRange(startLine, 5).setNumberValue(todays.containsKey("0") ? todays.getIntValue("0") : 0);
        //当天新客户
        sheet.getCellRange(startLine, 6).setNumberValue(todays.containsKey("1") ? todays.getIntValue("1") : 0);
        //当天合计
        sheet.getCellRange(startLine, 7).setFormula("=SUM(E" + startLine + ":F" + startLine + ")");

        //当月老客户
        sheet.getCellRange(startLine, 8).setNumberValue(month.containsKey("0") ? month.getIntValue("0") : 0);
        //当月新客户
        sheet.getCellRange(startLine, 9).setNumberValue(month.containsKey("1") ? month.getIntValue("1") : 0);
        //当月合计
        sheet.getCellRange(startLine, 10).setFormula("=SUM(H" + startLine + ":I" + startLine + ")");

        //当年老客户
        sheet.getCellRange(startLine, 11).setNumberValue(year.containsKey("0") ? year.getIntValue("0") : 0);
        //当年新客户
        sheet.getCellRange(startLine, 12).setNumberValue(year.containsKey("1") ? year.getIntValue("1") : 0);
        //当年合计
        sheet.getCellRange(startLine, 13).setFormula("=SUM(K" + startLine + ":L" + startLine + ")");


        startLine++;

        //算出公司合计
        for (int i = 5; i <= 13; i++) {
            char cc = (char) ('A' + i - 1);
            sheet.getCellRange(startLine, i).setFormula("=" + cc + "" + (gannaIndex + 4) + "+" + cc + "" + (startLine - 1) + "+" + cc + "" + (startLine - 2));
        }


        workbook.calculateAllValue();
        String fileName = "合肥圆融" + instance.get(Calendar.YEAR) + "年" + mon + "月客户拜访记录总表.xlsx" + "VVV" + UUID.randomUUID().toString();
        workbook.saveToFile("E:/springboot/" + fileName);
        return fileName;
    }


    public static void main(String[] args) {

        Workbook workbook = new Workbook();
        try {
            workbook.loadFromStream(new FileInputStream("C:\\Users\\Administrator\\Desktop\\合肥圆融2020年6月客户拜访记录总表(1).xlsx"));
            WorksheetsCollection worksheets = workbook.getWorksheets();
            Worksheet sheet = worksheets.get("数量汇总表");
            CellRange[] rows = sheet.getRows();
            JSONObject res = new JSONObject();
            for (int i = 3; i <= 45; i++) {

                String ywy = sheet.get(i + 1, 4).getText();
                JSONObject obj = res.containsKey("ywy") ? res.getJSONObject(ywy) : new JSONObject();


                if (i <= 21) {
                    obj.put("sorce", 3);

                } else if (i == 22) {
                    continue;
                } else if (i > 22) {
                    obj.put("sorce", 2);
                }

                System.out.println(ywy);
                JSONObject month = new JSONObject();
                JSONObject year = new JSONObject();
                //当月老客户
                month.put("0", Integer.parseInt(sheet.get(i + 1, 8).getNumberText()));
                //当月新客户
                month.put("1", Integer.parseInt(sheet.get(i + 1, 9).getNumberText()));
                //当年新客户
                year.put("0", Integer.parseInt(sheet.get(i + 1, 11).getNumberText()));
                //当月新客户
                year.put("1", Integer.parseInt(sheet.get(i + 1, 12).getNumberText()));

                obj.put("month", month);
                obj.put("year", year);
                res.put(ywy, obj);


            }
            System.out.println(res);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
