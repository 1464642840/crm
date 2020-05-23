package com.conpany.project;


import com.company.project.Application;
import com.company.project.dao.PersonMapper;
import com.company.project.model.Tel;
import com.company.project.service.PersonService;
import com.company.project.service.Plan1Service;
import com.company.project.service.StatisticsService;
import com.company.project.service.TelService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

}



