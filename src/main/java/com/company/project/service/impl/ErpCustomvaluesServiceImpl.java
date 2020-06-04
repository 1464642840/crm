package com.company.project.service.impl;

import com.company.project.dao.ErpCustomvaluesMapper;
import com.company.project.model.ErpCustomvalues;
import com.company.project.service.ErpCustomvaluesService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2020/05/13.
 */
@Service
@Transactional
public class ErpCustomvaluesServiceImpl extends AbstractService<ErpCustomvalues> implements ErpCustomvaluesService {
    @Resource
    private ErpCustomvaluesMapper erpCustomvaluesMapper;

}
