package com.company.project.utils.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.utils.web.HttpHelper;

import java.util.LinkedHashMap;
import java.util.UUID;

public class CrmUtils {

    static String CRM_DOMAIN = "https://crm.dbys.top";
    static String CRM_USER = "txt:admin_api";
    static String CRM_PASSWORD = "txt:88888888";
    static LinkedHashMap<String, String> HEAD = new LinkedHashMap<>();
    private static JSONObject LOGIN_INFO = null;

    static {
        HEAD.put("Content-Type", "application/zsml; charset=utf-8");
    }


    //Crm登录
    public static void crmLogin() {

        String json = "{session:'', datas:[";
        json += " {id:'user', val:'" + CRM_USER + "'},"; // 用户名
        json += " {id:'password', val:'" + CRM_PASSWORD + "'},"; // 用户名密码
        json += " {id:'serialnum', val:'txt:" + UUID.randomUUID().toString() + "'},"; // 用户串号
        json += " {id:'rndcode', val:''}"; // 随机验证码
        json += "]}";
        LinkedHashMap<String, String> head = new LinkedHashMap<>();
        head.put("Content-Type", "application/zsml; charset=utf-8");
        JSONObject sendPOST = HttpHelper.sendPOST(CRM_DOMAIN + "/SYSA/mobilephone/login.asp", json, head, "utf-8");
        System.out.println(sendPOST);
        LOGIN_INFO = sendPOST;
    }

    /**
     * 指派
     */
    public static void CrmCustomerDesignate(String custId, String ywyId, boolean isShare) {
        if (LOGIN_INFO == null) {
            crmLogin();
        }
        if (!isShare) { // 指派
            String json = "{session:'" + LOGIN_INFO.getJSONObject("header").getString("session")
                    + "',cmdkey:'__sys_dosave', datas:[";
            json += " {id:'ord', val:'" + custId + "'},"; // 数据唯一标识
            json += " {id:'member1', val:'0'},"; //
            json += " {id:'member2', val:'" + ywyId + "'}"; //
            json += "]}";
            JSONObject sendPOST = HttpHelper.sendPOST(
                    CRM_DOMAIN + "/SYSA/mobilephone/systemmanage/order.asp?datatype=tel", json, HEAD, "utf-8");
            System.out.println(sendPOST);
        } else { // 共享
            String json = "{session:'" + LOGIN_INFO.getJSONObject("header").getString("session")
                    + "',cmdkey:'__sys_dosave', datas:[";
            json += " {id:'ord', val:'" + custId + "'},"; // 数据唯一标识
            json += " {id:'member1', val:'0'},"; //
            json += " {id:'member2', val:'" + ywyId + "'},"; //
            json += " {id:'sharecontact', val:'1'},"; // 客户联系人
            json += " {id:'replyshare', val:'1'},"; // 洽谈进展
            json += " {id:'contractshare', val:'0'}"; // 客户合同
            json += "]}";
            JSONObject sendPOST = HttpHelper.sendPOST(CRM_DOMAIN + "/SYSA/mobilephone/salesManage/custom/share.asp",
                    json, HEAD, "utf-8");
            System.out.println(sendPOST);
        }
    }





    /**
     * 获得所以业务员账号信息
     * */
    public static JSONObject getAllCrmAccountInfos() {
        if (LOGIN_INFO == null) {
            crmLogin();
        }
        String json = "{session:'" + LOGIN_INFO.getJSONObject("header").getString("session")
                + "',cmdkey:'refresh', datas:[";
        json += " {id:'pagesize', val:'2000'},"; // 每页记录数
        json += " {id:'pageindex', val:'1'}"; // 数据页标
        json += "]}";
        JSONObject sendPOST = HttpHelper.sendPOST(CRM_DOMAIN + "/SYSA/mobilephone/officemanage/businesscard/list.asp",
                json, HEAD, "utf-8");

        JSONArray jsonArray = sendPOST.getJSONObject("body").getJSONObject("source").getJSONObject("table")
                .getJSONArray("rows");
        JSONObject res = new JSONObject();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray jsonArray2 = jsonArray.getJSONArray(i);
            res.put(jsonArray2.getString(1), jsonArray2.getString(0));
        }
        return res;
    }
}
