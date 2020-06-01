package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.dao.ReplyMapper;
import com.company.project.model.Reply;
import com.company.project.service.StatisticsService;
import com.company.project.utils.date.DateUtils;
import com.company.project.utils.erp.ErpDataUtils;
import com.company.project.utils.erp.StatisticsUtils;
import com.company.project.utils.string.NumberUtils;
import com.company.project.utils.string.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public JSONObject getSaleManDataStatistics(String type, Date startDate, Date endDate) throws ParseException {

        //1,根据日期范围获得日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        //客户拜访记录
        List<Map<Object, Object>> visitList = new ArrayList<>();

        JSONArray dateByParam = new JSONArray();
        if ("塑料".equals(type)) {
            String[] fields = {"FSalerId.FName", "FPriceUnitQty"};
            String condition = "FDate>='" + sdf.format(startDate) + "' and FDate<= '" + sdf.format(endDate) + "' and FCustId.FName not in('第八元素环境技术有限公司','期初资源调整','合肥圆融供应链管理有限公司','HONGKONG WINYOND CO.,LIMILED') and FPriceUnitQty>0.001  and   FSaleDeptId.Fname in('电商部-深圳中心','电商部-业务中心')";

            String order = "FDate desc";
            dateByParam = ErpDataUtils.getDateByParam("SAL_SaleOrder", order, fields, condition);

            visitList = replyMapper.selectGroupByYwy(startDate, endDate, "2");

        } else if ("钢材".equals(type)) {
            String[] fields = {"FSalerId.FName", "FPriceUnitQty"};
            String condition = "FDate>='" + sdf.format(startDate) + "' and FDate<= '" + sdf.format(endDate) + "' and FCustId.FName not in('第八元素环境技术有限公司','期初资源调整','合肥圆融供应链管理有限公司','HONGKONG WINYOND CO.,LIMILED') and FPriceUnitQty>0.001  and   FSaleDeptId.Fname like'钢材%'";

            String order = "FDate desc";
            dateByParam = ErpDataUtils.getDateByParam("SAL_SaleOrder", order, fields, condition);
            visitList = replyMapper.selectGroupByYwy(startDate, endDate, "3");
        }
        //1.得到每位销售员的销售数据
        JSONObject res = new JSONObject();

        final String SALECOUNKEY = "saleCount";
        for (int i = 0; i < dateByParam.size(); i++) {
            JSONArray jsonArray = dateByParam.getJSONArray(i);
            String ywy = jsonArray.getString(0);
            Double sl = jsonArray.getDouble(1)/1000;
            JSONObject ywyObj = res.containsKey(ywy) ? res.getJSONObject(ywy) : new JSONObject();
            ywyObj.put(SALECOUNKEY, NumberUtils.add(sl, ywyObj.containsKey(SALECOUNKEY) ? ywyObj.getDouble(SALECOUNKEY) : 0));
            res.put(ywy, ywyObj);
        }
        //2.得到每个业务员的拜访量

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        //用户成交状态
        JSONObject slCustomerStatusDate = StatisticsUtils.getCUSTOMERSTATUS();


        for (Map<Object, Object> o : visitList) {

            String ywy = o.get("ywy").toString();
            String isNew = o.get("is_new").toString();
            Integer times = Integer.parseInt(o.get("times").toString());


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
        if ("塑料".equals(type)) {
            String[] fields = {"FSalerId.FName", "FCustId.FName"};
            String condition = "FDate>='" + sdf.format(startDate) + "' and FDate<= '" + sdf.format(endDate) + "' and FCustId.FName not in('第八元素环境技术有限公司','个人','合肥融达环境技术有限公司','期初资源调整','合肥圆融供应链管理有限公司','HONGKONG WINYOND CO.,LIMILED') and FPriceUnitQty>0.001  and   FSaleDeptId.Fname in('电商部-深圳中心','电商部-业务中心')";

            String order = "FDate desc";
            dateByParam = ErpDataUtils.getDateByParam("SAL_SaleOrder", order, fields, condition);

            visitList = replyMapper.selectVisitCountGroupByYwy(startDate, endDate, "2");

        } else if ("钢材".equals(type)) {
            String[] fields = {"FSalerId.FName", "FCustId.FName"};
            String condition = "FDate>='" + sdf.format(startDate) + "' and FDate<= '" + sdf.format(endDate) + "' and FCustId.FName not in('第八元素环境技术有限公司','个人','合肥融达环境技术有限公司','期初资源调整','合肥圆融供应链管理有限公司','HONGKONG WINYOND CO.,LIMILED') and FPriceUnitQty>0.001  and   FSaleDeptId.Fname like'钢材%'";

            String order = "FDate desc";
            dateByParam = ErpDataUtils.getDateByParam("SAL_SaleOrder", order, fields, condition);
            visitList = replyMapper.selectVisitCountGroupByYwy(startDate, endDate, "3");
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

        //全部的销售数据
        JSONObject customerstatus = StatisticsUtils.getCUSTOMERSTATUS();

        //拿到业务员的销售数据,统计客户个数
        for (int i = 0; i < dateByParam.size(); i++) {
            JSONArray jsonArray = dateByParam.getJSONArray(i);
            String ywy = jsonArray.getString(0);
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

    public static void main(String[] args) {
        //  JSONObject slCustomerStatusDate = StatisticsUtils.getSlCustomerStatusDate();
        //  System.out.println(slCustomerStatusDate);
    }
}
