package com.company.project.service.impl;

import com.company.project.dao.Plan1Mapper;
import com.company.project.model.Plan1;
import com.company.project.service.Plan1Service;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/04/24.
 */
@Service
@Transactional
public class Plan1ServiceImpl extends AbstractService<Plan1> implements Plan1Service {
    @Resource
    private Plan1Mapper plan1Mapper;

    @Override
    public List<Plan1> findByMyCondition(Map<String, Object> map) {
        return plan1Mapper.findByMyCondition(map);
    }

    @Override
    public String tags(String ord) {
        Plan1 plan1 = plan1Mapper.selectByPrimaryKey(ord);
        if(plan1==null) {
            return "";
        }
        return null;
    }
}
