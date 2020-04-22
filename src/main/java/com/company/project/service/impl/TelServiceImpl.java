package com.company.project.service.impl;

import com.company.project.dao.TelMapper;
import com.company.project.model.Tel;
import com.company.project.service.TelService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/04/21.
 */
@Service
@Transactional
public class TelServiceImpl extends AbstractService<Tel> implements TelService {
    @Resource
    private TelMapper telMapper;

    @Override
    public List<Tel> findByMyCondition(Map map) {
       return  telMapper.findByMyCondition(map);
    }
}
