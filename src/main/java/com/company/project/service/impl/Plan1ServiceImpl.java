package com.company.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.company.project.dao.Plan1Mapper;
import com.company.project.dao.ReplyMapper;
import com.company.project.dao.TelMapper;
import com.company.project.model.Plan1;
import com.company.project.model.Reply;
import com.company.project.service.Plan1Service;
import com.company.project.core.AbstractService;
import com.company.project.utils.erp.StatisticsUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
    private TelMapper telMapper;

    @Override
    public List<Plan1> findByMyCondition(Map<String, Object> map) {
        return plan1Mapper.findByMyCondition(map);
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
        plan1.setDate7(new Date(nowDate));
        plan1.setIsxunhuan(0);
        plan1.setStarttime1(instance.get(Calendar.HOUR_OF_DAY) - 1 + "");
        plan1.setStarttime2(instance.get(Calendar.MINUTE) + "");
        plan1.setTime1(instance.get(Calendar.HOUR_OF_DAY) + 1 + "");
        plan1.setTime2(instance.get(Calendar.MINUTE) + "");
        plan1.setComplete(1 + "");
        plan1.setCateid3(3);
        plan1.setComplete("2");
        plan1.setIntro2(plan1.getIntro());
        plan1.setOption1(0);

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
        String companyName = telMapper.selectByPrimaryKey(plan1.getCompany()).getName();
        try {
            if (StatisticsUtils.isNewCustomer(companyName, date)) {
                reply.setIsNew(1);
            } else {
                reply.setIsNew(0);
            }
        } catch (Exception e) {

        }
        replyMapper.insertSelective(reply);

        return plan1;
    }


    @Override
    public Plan1 upDatePlan1OneKey(Plan1 plan1, Long nowDate, String name, String name2, Integer replyId) {
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
        plan1.setIntro2(plan1.getIntro());
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

}
