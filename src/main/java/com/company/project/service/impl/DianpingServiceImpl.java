package com.company.project.service.impl;

import com.company.project.dao.DianpingMapper;
import com.company.project.model.Dianping;
import com.company.project.service.DianpingService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2020/05/04.
 */
@Service
@Transactional
public class DianpingServiceImpl extends AbstractService<Dianping> implements DianpingService {
    @Resource
    private DianpingMapper dianpingMapper;

}
