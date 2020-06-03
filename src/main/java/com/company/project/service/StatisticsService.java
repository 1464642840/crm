package com.company.project.service;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.util.Date;

public interface StatisticsService {

    JSONObject getSaleManDataStatistics(String type, Date sD, Date eD) throws ParseException;

    JSONObject getcustomerVisitStatistics(String type, Date sD, Date eD);

    String getYwyVisitTodayStatistics() throws ParseException;
}
