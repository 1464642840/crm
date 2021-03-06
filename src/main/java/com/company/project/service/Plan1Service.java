package com.company.project.service;
import com.alibaba.fastjson.JSONObject;
import com.company.project.model.Plan1;
import com.company.project.core.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/04/24.
 */
public interface Plan1Service extends Service<Plan1> {

    List<Plan1> findByMyCondition(Map<String, Object> map);

    String tags(String ord);

    JSONObject statistics(HashMap<String, Object> map);

    Plan1 savePlan1OneKey(Plan1 plan1, Long nowDate, String name, String name2);

    Plan1 upDatePlan1OneKey(Plan1 plan1, Long nowDate, String name, String name2, Integer replyId);


    String doExport(Date sd, Date ed, String ywyIds, String custIds, String type);

    String doExport2(Date sd, Date ed, String custIds, String type, String bumen);

    List<Plan1> getAllRecord(Integer id);
}
