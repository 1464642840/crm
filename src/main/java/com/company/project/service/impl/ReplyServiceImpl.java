package com.company.project.service.impl;

import com.company.project.dao.Plan1Mapper;
import com.company.project.dao.ReplyMapper;
import com.company.project.model.Plan1;
import com.company.project.model.Reply;
import com.company.project.service.ReplyService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2020/04/30.
 */
@Service
@Transactional
public class ReplyServiceImpl extends AbstractService<Reply> implements ReplyService {
    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private Plan1Mapper plan1Mapper;

    @Override
    public void dosSave(Reply reply) {
        Integer plan1 = reply.getPlan1();
        Plan1 byId = plan1Mapper.selectByIds(plan1+"").get(0);
        byId.setIntro2(reply.getIntro());
        byId.setComplete("2");
        plan1Mapper.updateByPrimaryKey(byId);
        replyMapper.insertSelective(reply);

    }
}
