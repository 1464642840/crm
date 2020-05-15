package com.company.project.service.impl;

import com.company.project.dao.ErpCustomvaluesMapper;
import com.company.project.dao.TelMapper;
import com.company.project.model.ErpCustomvalues;
import com.company.project.model.Tel;
import com.company.project.service.TelService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by CodeGenerator on 2020/04/21.
 */
@Service
@Transactional
public class TelServiceImpl extends AbstractService<Tel> implements TelService {
    @Resource
    private TelMapper telMapper;
    @Resource
    private ErpCustomvaluesMapper erpCustomvaluesMapper;

    @Override
    public List<Tel> findByMyCondition(Map map) {
        return telMapper.findByMyCondition(map);
    }

    @Override
    public void updateCustInfo(Tel tel, HashMap<String, String> map, HashMap<String, String> map1, int baseParamCount, Set<String> numSet) {
        //更新客户的基本信息


        //修改基本信息
        if (baseParamCount != 0) {
            telMapper.updateByPrimaryKeySelective(tel);
        }


        //判断扩展字段在数据库有没有记录
        List<String> exist = new ArrayList<>();
        if(!CollectionUtils.isEmpty(numSet)){
            telMapper.selectExistExtendFields(tel.getOrd(), numSet);
        }


        //修改枚举类型
        if (!map.isEmpty()) {
            telMapper.updateCustomerField(map, tel.getOrd());
        }
        //修改普通扩展字段
        if (!map1.isEmpty()) {
            telMapper.updateCustomerField2(map1, tel.getOrd());
        }

        //新增扩展字段
        List<ErpCustomvalues> erpCustomvaluesList = new ArrayList<>();
        numSet.removeAll(exist);

        for (String s : numSet) {
            ErpCustomvalues e = new ErpCustomvalues();
            e.setFieldsid(Integer.parseInt(s));
            e.setOrderid(tel.getOrd());
            e.setFvalue(map.containsKey(s) ? map.get(s) : map1.get(s));
            if (e.getFvalue() == null) {
                e.setFvalue("");
            }
            erpCustomvaluesList.add(e);
        }
        if(!CollectionUtils.isEmpty(erpCustomvaluesList)) {
            erpCustomvaluesMapper.insertList(erpCustomvaluesList);
        }
    }
}
