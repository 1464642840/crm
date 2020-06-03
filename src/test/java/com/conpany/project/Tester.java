package com.conpany.project;


import com.company.project.Application;
import com.company.project.core.MyThread;
import com.company.project.dao.PersonMapper;
import com.company.project.model.Tel;
import com.company.project.service.PersonService;
import com.company.project.service.Plan1Service;
import com.company.project.service.StatisticsService;
import com.company.project.service.TelService;
import com.company.project.utils.position.GaodeUtils;
import com.company.project.utils.string.StrUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        List<Tel> all = telService.findAll();
        ThreadPoolExecutor instance = MyThread.getInstance();
        for (Tel tel : all) {
            String address = tel.getAddress();
            if (StrUtils.isNull(address)) {
                continue;
            }
            instance.execute(new Runnable() {
                @Override
                public void run() {
                    String geocoderLatitude = GaodeUtils.getGeocoderLatitude(address);
                    String[] split = geocoderLatitude.split(",");
                    tel.setLng(new BigDecimal(split[0]));
                    tel.setLat(new BigDecimal(split[1]));
                    telService.updateSelective(tel);
                }
            });

        }

        try {
            instance.awaitTermination(200, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public  void testExport(){
        try {
            statisticsService.getYwyVisitTodayStatistics();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}



