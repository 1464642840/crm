package com.company.project.utils.erp;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.utils.string.StrUtils;

import java.util.Date;

public class ErpDataUtils {

	public static JSONArray getDateByParam(String tableName, String order, String[] fields, String condition) {
		String select = "[{\"FormId\": \"" + tableName + "\",\"FieldKeys\": \"" + StrUtils.join(fields, ",")
				+ "\",\"FilterString\":\"" + condition
				+ "\",\"OrderString\": \""+order+"\",\"TopRowCount\": 0,\"StartRow\": 0,\"Limit\": 0}]";

		ERPHttpClient client = ERPHttpClient.getInstance();

		String g = client.selectBitchForms(select);
		try {
			JSONArray fromObject = JSONArray.parseArray(g);
			return fromObject;
		} catch (Exception e) {
			System.out.println("错误数据:" + g);
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static JSONObject getJSONObjectByAttribute(String formId, String key, String value, ERPHttpClient client) {
		String queryParam = "{\"FormId\": \"\", \"data\": {\"CreateOrgId\": \"0\",\"Number\": \"\", \"Id\": \"\"}}";
		JSONObject param = JSONObject.parseObject(queryParam);
		JSONObject data = param.getJSONObject("data");
		data.put(key, value);
		param.put("FormId", formId);
		param.put("data", data);
//		System.out.println(data);
		String message = client.selectForms(param.toString());
		JSONObject result = JSONObject.parseObject(message);
		return result;
	}
	







}
