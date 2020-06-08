package com.company.project.utils.erp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.model.Reply;

import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticsUtils {


    private static JSONObject CUSTOMERSTATUS = null;
    private static JSONObject CAIGOUSTATUS = null;
    private static Date LAST_DATE = null;

    public static JSONObject getCUSTOMERSTATUS() {
        if (CUSTOMERSTATUS == null) {
            LAST_DATE = new Date();
            return CUSTOMERSTATUS = getSlCustomerStatusDate(new Date());

        } else {
            if (LAST_DATE.getTime() + 3600 * 1000 > new Date().getTime()) {
                LAST_DATE = new Date();
                return CUSTOMERSTATUS = getSlCustomerStatusDate(new Date());
            } else {
                return CUSTOMERSTATUS;
            }
        }
    }


    public static JSONObject getCAIGOUSTATUS() {
        if (CAIGOUSTATUS == null) {
            LAST_DATE = new Date();
            return CAIGOUSTATUS = getCaigouStatusDate(new Date());

        } else {
            if (LAST_DATE.getTime() + 3600 * 1000 > new Date().getTime()) {
                LAST_DATE = new Date();
                return CAIGOUSTATUS = getCaigouStatusDate(new Date());
            } else {
                return CAIGOUSTATUS;
            }
        }
    }


    /**
     * 判断是否为新客户
     */
    public static boolean isNewCustomer(String custName, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取成交客户数据
        getCUSTOMERSTATUS();
        if (CUSTOMERSTATUS.containsKey(custName)) {
            JSONObject jsonObject = CUSTOMERSTATUS.getJSONObject(custName);
            JSONArray dateList = jsonObject.getJSONArray("date");
            String string = dateList.getString(dateList.size() - 1);
            if (sdf.format(date).compareTo(string) > 0) {
                return false;
            } else {
                return true;
            }

        } else {
            //无成交记录
            return true;
        }

    }


    /**
     * 判断是否为新供应商
     */
    public static boolean isNewGys(String custName, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取成交供应商数据
        getCAIGOUSTATUS();
        if (CAIGOUSTATUS.containsKey(custName)) {
            JSONObject jsonObject = CAIGOUSTATUS.getJSONObject(custName);
            JSONArray dateList = jsonObject.getJSONArray("date");
            String string = dateList.getString(dateList.size() - 1);
            if (sdf.format(date).compareTo(string) > 0) {
                return false;
            } else {
                return true;
            }

        } else {
            //无成交记录
            return true;
        }

    }


    /**
     * 获得客户状态统计的数据
     *
     * @return
     */
    public static JSONObject getSlCustomerStatusDate(Date date1) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdFormat.format(date1);
        String[] fields = {"FCustId.FName", "FSalerId.FName", "FDate"};
        String condition = "FCustId.FName not in ('第八元素环境技术有限公司','合肥圆融供应链管理有限公司','个人') and FDocumentStatus='C' and FDate>='2019-04-01' and FDate<='"
                + format + "'";
        JSONArray dateByParam = ErpDataUtils.getDateByParam("SAL_SaleOrder", "", fields, condition);
        final JSONObject customerObj = new JSONObject();
        for (int i = 0; i < dateByParam.size(); i++) {
            JSONArray jsonArray = dateByParam.getJSONArray(i);
            String custName = jsonArray.getString(0);
            String saleManName = jsonArray.getString(1);
            String date = jsonArray.getString(2).substring(0, 10);
            if (customerObj.containsKey(custName)) {
                JSONObject jsonObject = customerObj.getJSONObject(custName);

                Set<String> dateSet = new TreeSet<>(new Comparator<String>() {

                    @Override
                    public int compare(String o1, String o2) {
                        return -o1.compareTo(o2);
                    }
                });
                dateSet.addAll(jsonObject.getJSONArray("date").toJavaList(String.class));


                if (!dateSet.contains(date)) {
                    dateSet.add(date);
                }
                jsonObject.put("date", dateSet);
                customerObj.put(custName, jsonObject);

            } else {
                JSONObject jsonObject = new JSONObject();
                JSONArray ywyArr = new JSONArray();
                Set<String> dateArr = new TreeSet<>(new Comparator<String>() {

                    @Override
                    public int compare(String o1, String o2) {
                        return -o1.compareTo(o2);
                    }
                });
                ywyArr.add(saleManName);
                dateArr.add(date);
                jsonObject.put("xsy", ywyArr);
                jsonObject.put("date", dateArr);
                customerObj.put(custName, jsonObject);
            }
        }
        Set keySet = customerObj.keySet();
        ArrayList<String> arrayList = new ArrayList<>(keySet);
        Collections.sort(arrayList, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return -customerObj.getJSONObject(o1).getString("date")
                        .compareTo(customerObj.getJSONObject(o2).getString("date"));
            }
        });
        JSONObject res = new JSONObject();
        for (String string : arrayList) {
            res.put(string, customerObj.getJSONObject(string));
        }
        return res;
    }


    /**
     * 获得采购供应商状态统计的数据
     *
     * @return
     */
    public static JSONObject getCaigouStatusDate(Date date1) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdFormat.format(date1);
        String[] fields = {"FSupplierId.FName", "FPurchaserId.FName", "FDate"};
        String condition = "FDocumentStatus='C' and FDate>='2019-04-01' and FDate<='"
                + format + "'";
        JSONArray dateByParam = ErpDataUtils.getDateByParam("PUR_PurchaseOrder", "", fields, condition);
        final JSONObject customerObj = new JSONObject();
        for (int i = 0; i < dateByParam.size(); i++) {
            JSONArray jsonArray = dateByParam.getJSONArray(i);
            String custName = jsonArray.getString(0);
            String saleManName = jsonArray.getString(1);
            String date = jsonArray.getString(2).substring(0, 10);
            if (customerObj.containsKey(custName)) {
                JSONObject jsonObject = customerObj.getJSONObject(custName);

                Set<String> dateSet = new TreeSet<>(new Comparator<String>() {

                    @Override
                    public int compare(String o1, String o2) {
                        return -o1.compareTo(o2);
                    }
                });
                dateSet.addAll(jsonObject.getJSONArray("date").toJavaList(String.class));


                if (!dateSet.contains(date)) {
                    dateSet.add(date);
                }
                jsonObject.put("date", dateSet);
                customerObj.put(custName, jsonObject);

            } else {
                JSONObject jsonObject = new JSONObject();
                JSONArray ywyArr = new JSONArray();
                Set<String> dateArr = new TreeSet<>(new Comparator<String>() {

                    @Override
                    public int compare(String o1, String o2) {
                        return -o1.compareTo(o2);
                    }
                });
                ywyArr.add(saleManName);
                dateArr.add(date);
                jsonObject.put("xsy", ywyArr);
                jsonObject.put("date", dateArr);
                customerObj.put(custName, jsonObject);
            }
        }
        Set keySet = customerObj.keySet();
        ArrayList<String> arrayList = new ArrayList<>(keySet);
        Collections.sort(arrayList, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return -customerObj.getJSONObject(o1).getString("date")
                        .compareTo(customerObj.getJSONObject(o2).getString("date"));
            }
        });
        JSONObject res = new JSONObject();
        for (String string : arrayList) {
            res.put(string, customerObj.getJSONObject(string));
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(getCaigouStatusDate(new Date()));
    }


}
