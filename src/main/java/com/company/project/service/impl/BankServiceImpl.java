package com.company.project.service.impl;

import com.company.project.dao.BankMapper;
import com.company.project.model.Bank;
import com.company.project.service.BankService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2020/04/21.
 */
@Service
@Transactional
public class BankServiceImpl extends AbstractService<Bank> implements BankService {
    @Resource
    private BankMapper bankMapper;

}
