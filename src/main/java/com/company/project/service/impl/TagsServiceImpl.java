package com.company.project.service.impl;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.Plan1Mapper;
import com.company.project.dao.TagsMapper;
import com.company.project.model.Plan1;
import com.company.project.model.Tags;
import com.company.project.service.TagsService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Created by CodeGenerator on 2020/04/26.
 */
@Service
@Transactional
public class TagsServiceImpl extends AbstractService<Tags> implements TagsService {
    @Resource
    private TagsMapper tagsMapper;
    @Resource
    private Plan1Mapper plan1Mapper;

    @Override
    public Result tags(Tags tags) {
        Condition condition = new Condition(Tags.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("person", tags.getPerson());
        criteria.andEqualTo("plan1", tags.getPlan1());

        List<Tags> tags1 = (List<Tags>) tagsMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(tags1)) {
            tags.setTime(new Date());
            tags.setId(null);
            tagsMapper.insertSelective(tags);
            //更新点赞数
            Plan1 plan1 = plan1Mapper.selectByPrimaryKey(tags.getPlan1());
            plan1.setTags(plan1.getTags() + 1);
            plan1Mapper.updateByPrimaryKeySelective(plan1);

            return ResultGenerator.genSuccessResult(1);
        } else {
            Tags tags2 = tags1.get(0);
            int i = tagsMapper.deleteByPrimaryKey(tags2.getId());
            //更新点赞数
            Plan1 plan1 = plan1Mapper.selectByPrimaryKey(tags.getPlan1());
            plan1.setTags(plan1.getTags() - 1);
            plan1Mapper.updateByPrimaryKeySelective(plan1);
            if (i > 0) {
                return ResultGenerator.genSuccessResult(-1);
            }
        }
        return ResultGenerator.genFailResult("失败了");
    }
}
