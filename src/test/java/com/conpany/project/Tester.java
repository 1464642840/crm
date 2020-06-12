package com.conpany.project;


import com.alibaba.fastjson.JSONObject;
import com.company.project.Application;
import com.company.project.Constants;
import com.company.project.core.MyThread;
import com.company.project.dao.GateMapper;
import com.company.project.dao.Plan1Mapper;
import com.company.project.dao.ReplyMapper;
import com.company.project.model.Plan1;
import com.company.project.model.Reply;
import com.company.project.model.Tel;
import com.company.project.service.*;
import com.company.project.utils.position.GaodeUtils;
import com.company.project.utils.string.StrUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单元测试继承该类即可
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)//这里的Application是springboot的启动类名
@WebAppConfiguration
public class Tester {


    @Autowired
    private TelService telService;
    @Autowired
    private Plan1Service plan1Service;
    @Autowired
    private PersonService personService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private Plan1Mapper plan1Mapper;
    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private GateMapper gateMapper;

    @Autowired
    StatisticsService statisticsService;

    @Test
    public void tes222() throws ParseException, InterruptedException, IOException {

        SysnTest sysnTest = new SysnTest();
        sysnTest.setTelService(telService);
        sysnTest.setPlan1Service(plan1Service);
        sysnTest.setPersonService(personService);

        sysnTest.test2();
    }


    @Test
    public void testStatictics() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date Date1 = sdf.parse("2019-03-6");
            Date Date2 = sdf.parse("2021-05-6");
            statisticsService.getSaleManDataStatistics("塑料", Date1, Date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void fullPosition() {
       // List<Tel> all = telService.findAll();
        List<Tel> all = telService.findByIds("3694");
        ThreadPoolExecutor instance = MyThread.getInstance();
        for (Tel tel : all) {
            System.out.println(tel.getOrd());
            String address = StrUtils.clearAllSymbol(tel.getAddress());
            if (StrUtils.isNull(address)) {
                continue;
            }
            if(instance.getQueue().size()>1000){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            instance.execute(new Runnable() {
                @Override
                public void run() {
                   try {

                       String geocoderLatitude = GaodeUtils.getGeocoderLatitude(address);
                       String[] split = geocoderLatitude.split(",");
                       tel.setLng(new BigDecimal(split[0]));
                       tel.setLat(new BigDecimal(split[1]));
                       telService.updateSelective(tel);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                }
            });

        }



        try {
            instance.awaitTermination(2000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testExport() {
        try {
            statisticsService.getYwyVisitTodayStatistics("2020-06-04");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testInertr() throws ParseException {


        JSONObject baiFangTongji = Constants.baiFangTongji;
        Set<String> strings = baiFangTongji.keySet();
        for (String string : strings) {

            JSONObject jsonObject = baiFangTongji.getJSONObject(string);
            Integer sorce = jsonObject.getInteger("sorce");
            JSONObject year = jsonObject.getJSONObject("year");
            int olds = year.getIntValue("0");
            int news = year.getIntValue("1");
            int ord = 0;
            try {
                List<Map<Object, Object>> positionByName = gateMapper.getPositionByName(string);
                ord = Integer.parseInt(positionByName.get(0).get("ord").toString());
            } catch (Exception e) {
                continue;

            }

            List<HashMap<String, Object>> data = replyMapper.getByTest(string);

            for (HashMap<String, Object> datum : data) {
                if ("1".equals(datum.get("is_new").toString())) {
                    news -= Integer.parseInt(datum.get("counts").toString());
                }
                if ("0".equals(datum.get("is_new").toString())) {
                    olds -= Integer.parseInt(datum.get("counts").toString());
                }

            }

            if (olds < 0) {
                olds = 0;
                news -= olds;
            }
            if (news < 0) {
                news = 0;
                olds -= news;
            }


            insertTO(ord, news, olds);
        }


    }

    @Test
    public void addPersonBaifangCount() throws ParseException {
        insertTO(82,1,2);
    }

    private void insertTO(Integer gateId, int new_, int old_) throws ParseException {

        for (int i = 0; i < new_; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Plan1 plan1 = new Plan1();
            plan1.setDate7(sdf.parse("2020-05-31"));
            plan1.setCateid(gateId);
            plan1.setOrder1(gateId + "");
            plan1.setIntro2("");
            plan1.setIntro("");
            int j = plan1Mapper.insertUseGeneratedKeys(plan1);

            Reply reply = new Reply();

            reply.setPlan1(plan1.getOrd());
            reply.setCateid(gateId);
            reply.setDel(1);
            reply.setIsNew(1);
            replyService.saveSelective(reply);
        }
        for (int i = 0; i < old_; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Plan1 plan1 = new Plan1();
            plan1.setDate7(sdf.parse("2020-05-31"));
            plan1.setCateid(gateId);
            plan1.setOrder1(gateId + "");
            plan1.setIntro2("");
            plan1.setIntro("");
            int j = plan1Mapper.insertUseGeneratedKeys(plan1);

            Reply reply = new Reply();

            reply.setPlan1(j);
            reply.setCateid(gateId);
            reply.setDel(1);
            reply.setIsNew(0);
            replyService.saveSelective(reply);
        }
    }





}



