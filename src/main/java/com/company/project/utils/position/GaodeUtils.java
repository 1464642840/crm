package com.company.project.utils.position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import com.company.project.utils.string.StrUtils;
import com.company.project.utils.web.SSLClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.*;


public class GaodeUtils {
	private static final String KEY = "7abec3abdaeb9c290e088a9feed44e32";// key 根据高德地图申请 认证 给的key 该key 不对任何ip 限制
																			// 0.0.0.0/00

	/**
	 * 返回输入地址的经纬度坐标 key lng(经度),lat(纬度)
	 */
	public static String getGeocoderLatitude(String address) {
		HttpResponse res = null;
		HttpGet get = null;
		CloseableHttpClient httpclient = null;
		try {
			// 将地址转换成utf-8的16进制
			String address_ = URLEncoder.encode(address, "UTF-8");
			httpclient = SSLClient.getHttpsClient();
			// get=new
			// HttpGet("https://restapi.amap.com/v3/geocode/geo?address="+address+"&output=json&key="+KEY);
			get = new HttpGet("https://restapi.amap.com/v3/place/text?key=" + KEY + "&keywords=" + address_);
			res = httpclient.execute(get);
			HttpEntity entity = res.getEntity();
			String str = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			if (!StrUtils.isNull(str)) {
				JSONObject jsonObject = JSONObject.parseObject(str);
				JSONArray jsonArray = jsonObject.getJSONArray("pois");
				if (jsonArray.size() <= 0) {
					jsonArray.add(jsonObject.getJSONObject("sug_address"));
				}
				if (jsonArray.size() > 0) {
					if (jsonArray.getJSONObject(0).containsKey("location")) {
						String location = jsonArray.getJSONObject(0).get("location").toString();
						return location;
					} else {
						JSONObject jsonObject2 = jsonObject.getJSONObject("suggestion").getJSONArray("cities")
								.getJSONObject(0);
						String cityCode = jsonObject2.getString("citycode");
						get = new HttpGet("https://restapi.amap.com/v3/place/text?key=" + KEY + "&keywords=" + address_
								+ "&city=" + cityCode);
						res = httpclient.execute(get);
						entity = res.getEntity();
						str = EntityUtils.toString(entity);
						EntityUtils.consume(entity);
						if (!StrUtils.isNull(str)) {
							jsonObject = JSONObject.parseObject(str);
							jsonArray = jsonObject.getJSONArray("pois");
							if (jsonArray.size() <= 0) {
								jsonArray.add(jsonObject.getJSONObject("sug_address"));
							}
							if (jsonArray.size() > 0) {
								if (jsonArray.getJSONObject(0).containsKey("location")) {
									String location = jsonArray.getJSONObject(0).get("location").toString();
									return location;
								}
							}
						}
					}
				}
				return null;
			}
		} catch (Exception e) {
			System.out.println("地址有误:" + address);
			e.printStackTrace();
		}
		return null;
	}

	// 根据两个点的经纬度计算两点之间的距离
	public static String getDistanceByPoints(String origins, String destination) {
		HttpResponse res = null;

		return null;
	}
	
	
	
	
	
	
	
	/**
	 * 返回输入地址返回地址信息
	 */
	public static JSONObject getInfoByAddress(String address) {
		HttpResponse res = null;
		HttpGet get = null;
		CloseableHttpClient httpclient = null;
		try {
			// 将地址转换成utf-8的16进制
			String address_ = URLEncoder.encode(address, "UTF-8");
			httpclient = SSLClient.getHttpsClient();
			// get=new
			// HttpGet("https://restapi.amap.com/v3/geocode/geo?address="+address+"&output=json&key="+KEY);
			get = new HttpGet("https://restapi.amap.com/v3/place/text?key=" + KEY + "&keywords=" + address_);
			res = httpclient.execute(get);
			HttpEntity entity = res.getEntity();
			String str = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			if (!StrUtils.isNull(str)) {
				JSONObject jsonObject = JSONObject.parseObject(str);
				JSONArray jsonArray = jsonObject.getJSONArray("pois");
				if (jsonArray.size() <= 0) {
					jsonArray.add(jsonObject.getJSONObject("sug_address"));
				}
				if (jsonArray.size() > 0) {
					if (jsonArray.getJSONObject(0).containsKey("location")) {
						return jsonArray.getJSONObject(0);
					} else {
						JSONObject jsonObject2 = jsonObject.getJSONObject("suggestion").getJSONArray("cities")
								.getJSONObject(0);
						String cityCode = jsonObject2.getString("citycode");
						get = new HttpGet("https://restapi.amap.com/v3/place/text?key=" + KEY + "&keywords=" + address_
								+ "&city=" + cityCode);
						res = httpclient.execute(get);
						entity = res.getEntity();
						str = EntityUtils.toString(entity);
						EntityUtils.consume(entity);
						if (!StrUtils.isNull(str)) {
							jsonObject = JSONObject.parseObject(str);
							jsonArray = jsonObject.getJSONArray("pois");
							if (jsonArray.size() <= 0) {
								jsonArray.add(jsonObject.getJSONObject("sug_address"));
							}
							if (jsonArray.size() > 0) {
								if (jsonArray.getJSONObject(0).containsKey("location")) {
									return   jsonArray.getJSONObject(0);
									
								}
							}
						}
					}
				}
				return null;
			}
		} catch (Exception e) {
			System.out.println("地址有误:" + address);
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	

	/*
	 * Java http 请求
	 */
	public static String loadJson(String url) {
		try {
			// 下面那条URL请求返回结果无中文，可不转换编码格式
			CloseableHttpClient httpsClient = SSLClient.getHttpsClient();
			HttpGet get = new HttpGet(url);
			// 设置Headers
			CloseableHttpResponse execute = httpsClient.execute(get);
			HttpEntity entity = execute.getEntity();
			String str = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			return str;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 高德地图WebAPI : 行驶距离测量
	 * type:0,直线距离,1.驾车导航距离
	 */
	public static String distance(String origins, String destination,int type) {
		
		String url = "http://restapi.amap.com/v3/distance?" + "origins=" + origins + "&destination=" + destination
				+ "&type=" + type + "&output=json" + "&key=" + KEY;
		JSONObject jsonobject = JSONObject.parseObject(loadJson(url));
		// System.out.println(jsonobject.toString());
		/*
		 * JSONArray resultsArray = jsonobject.getJSONArray("results"); JSONObject
		 * distanceObject = resultsArray.getJSONObject(0); String distance =
		 * distanceObject.getString("distance");
		 */
		return jsonobject.toString();
	}

	/**
	 * 经纬度转中文地址
	 */
	public static String getAddressByLocation(String location) {
		try {
			String url = "https://restapi.amap.com/v3/geocode/regeo?location=" + location + "&key=" + KEY
					+ "&radius=1000&extensions=all";
			JSONObject jsonobject = JSONObject.parseObject(loadJson(url));
			return jsonobject.getJSONObject("regeocode").getString("formatted_address");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	public static void main(String[] args) {

		JSONObject infoByAddress = getInfoByAddress("中山市火炬开发区云步路1号");
		System.out.println(infoByAddress);
		
	}

}
