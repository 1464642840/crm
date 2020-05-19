package com.conpany.project;


import com.company.project.Application;
import com.company.project.dao.PersonMapper;
import com.company.project.model.Tel;
import com.company.project.service.PersonService;
import com.company.project.service.Plan1Service;
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
import java.util.List;

/**
 * 单元测试继承该类即可
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)//这里的Application是springboot的启动类名
@WebAppConfiguration
public  class Tester {




    @Autowired
    private  TelService  telService;
    @Autowired
    private Plan1Service plan1Service;
    @Autowired
    private PersonService personService;

    @Test
    public  void tes222() throws ParseException, InterruptedException, IOException {

     SysnTest sysnTest = new SysnTest();
     sysnTest.setTelService(telService);
     sysnTest.setPlan1Service(plan1Service);
     sysnTest.setPersonService(personService);

     sysnTest.test2();
    }
}



