package com.company.project.service.impl;

import com.company.project.dao.GateMapper;
import com.company.project.model.Gate;
import com.company.project.model.Tags;
import com.company.project.service.GateService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/04/24.
 */
@Service
@Transactional
public class GateServiceImpl extends AbstractService<Gate> implements GateService {
    @Resource
    private GateMapper gateMapper;

    @Override
    public List<Map<Object,Object>> getPositionByName(String ywy) {
        return gateMapper.getPositionByName(ywy);
    }

    @Override
    public List<Gate> findByMyCondition(Map<String, Object> map) {
        Condition condition = new Condition(Gate.class);
        Example.Criteria criteria = condition.createCriteria();
        if(map.containsKey("bumenId")) {
            criteria.andEqualTo("sorce",map.get("bumenId") );
            criteria.andEqualTo("del",1 );
        }
        List<Gate> gates = gateMapper.selectByCondition(condition);
        return gates;


    }
}
