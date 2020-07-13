package com.company.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.company.project.core.ServiceException;
import com.company.project.dao.ErpCustomvaluesMapper;
import com.company.project.dao.TelMapper;
import com.company.project.model.ErpCustomvalues;
import com.company.project.model.Gate;
import com.company.project.model.Tel;
import com.company.project.service.GateService;
import com.company.project.service.TelService;
import com.company.project.core.AbstractService;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Created by CodeGenerator on 2020/04/21.
 */
@Service
public class TelServiceImpl extends AbstractService<Tel> implements TelService {
    @Resource
    private TelMapper telMapper;
    @Resource
    private ErpCustomvaluesMapper erpCustomvaluesMapper;
    @Resource
    private GateService gateService;

    final String[] MANANGERPOTION = {"总经理", "风控", "部门长", "综合主管"};

    @Override
    public List<Tel> findByMyCondition(Map map) {
        Gate by = gateService.findBy("username", map.get("ywy"));
        if (by == null) {
            throw new ServiceException("找不到账号信息");
        }
        map.put("username", by.getUsername());
        map.put("cateid", by.getOrd().toString());
        map.put("bumenId", by.getSorce().toString());
        //客户列表
        if (Integer.parseInt(map.get("size").toString()) == 15) {
            //用户对象


            String position = map.get("position").toString();


            if (map.containsKey("position")) {
                if ("部门长".equals(position) || "综合主管".equals(position)) {
                    //1.获得部门长所在的部门id

                    Integer sorce = by.getSorce();
                    map.put("sorce", sorce);
                    map.remove("ywy");
                } else if ("总经理".equals(position) || "风控".equals(position)) {
                    String bumen = map.containsKey("部门") ? map.get("bumen").toString() : "";
                    map.remove("ywy");
                    map.remove("bumenId");
                }
            }

            PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString()));
            List<Tel> byMyCondition = telMapper.findByMyCondition(map);

            //判断当前是不是管理级别,是就把公司对应的业务员和被共享的人查询来
            if (Arrays.asList(MANANGERPOTION).contains(position)) {
                Set<String> telCateidList = new HashSet<>();
                for (Tel tel : byMyCondition) {
                    String share = tel.getShare()==null?"":tel.getShare().trim();
                    String cateid = tel.getCateid()==null?"":tel.getCateid().toString().trim();
                    if (!StrUtils.isNull(cateid)) {
                        telCateidList.add(cateid);
                    }
                    if (!StrUtils.isNull(share)) {
                        for (String s : share.split(",")) {
                            try {
                                telCateidList.add(s);
                            } catch (Exception e) {
                                continue;
                            }

                        }

                    }
                }
                //2.去数据库拿到这些cateid对应的人员
                if(telCateidList.size()>0) {
                    List<Gate> byIds = gateService.findByIds(StrUtils.join(telCateidList.toArray(), ","));
                    //将人员放进map中,key为id,值为姓名
                    JSONObject obj = new JSONObject();
                    for (Gate byId : byIds) {
                        obj.put(byId.getOrd().toString(), byId.getUsername());
                    }

                    //3.将查询的用户列表添加进业务员列表中
                    for (Tel tel : byMyCondition) {
                        if ("供应商".equals(tel.getBusinessType())) {
                            continue;
                        }
                        Set<String> ywyList = new LinkedHashSet<>();
                        String share = tel.getShare() == null ? "" : tel.getShare().trim();
                        String cateid = tel.getCateid() == null ? "" : tel.getCateid().toString().trim();
                        if (obj.containsKey(cateid)) {
                            ywyList.add(obj.getString(cateid));
                        }
                        if (!StrUtils.isNull(share)) {
                            for (String s : share.split(",")) {
                                if (obj.containsKey(s)) {
                                    ywyList.add(obj.getString(s));
                                }

                            }
                        }

                        tel.setYwyList(StrUtils.join(ywyList.toArray(), " "));

                    }
                }


            }


            return byMyCondition;
            //拜访时搜索客户列表
        } else {
            Integer sorce = by.getSorce();
            map.put("sorce", sorce);
            if(Arrays.asList(MANANGERPOTION).contains(map.get("position"))){
                map.remove("sorce");
            }
            PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString()));
            return telMapper.findByBaiFangCustList(map);
        }
    }

    @Override
    public void updateCustInfo(Tel tel, Map<String, String[]> parameterMap) {

        
        //获取类的所有字段
        Class<Tel> telClass = Tel.class;
        Field[] declaredFields = telClass.getDeclaredFields();
        List<String> telFileNames = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            telFileNames.add(declaredField.getName());

        }

        HashMap<String, String> map = new HashMap<>();
        HashMap<String, String> map1 = new HashMap<>();
        Set<String> numSet = new HashSet<>();
        Iterator iterator = parameterMap.keySet().iterator();
        int baseParamCount = 0;
        while (iterator.hasNext()) {

            String key = (String) iterator.next();
            if (!key.startsWith("@")) {
                if (!"ord".equals(key)&&telFileNames.contains(key)) {
                    baseParamCount++;
                }
            } else if (key.contains("_")) {
                numSet.add(key.split("_")[1]);
                if (key.startsWith("@meju")) {
                    if (!StrUtils.isNull(parameterMap.get(key)[0])) {
                        map.put(key.split("_")[1], parameterMap.get(key)[0]);
                    }
                } else {
                    map1.put(key.split("_")[1], parameterMap.get(key)[0]);
                }

            }
        }


        //更新客户的基本信息


        //修改基本信息
        if (baseParamCount != 0) {
            telMapper.updateByPrimaryKeySelective(tel);
        }


        //判断扩展字段在数据库有没有记录
        List<String> exist = new ArrayList<>();
        if (!CollectionUtils.isEmpty(numSet)) {
            exist = telMapper.selectExistExtendFields(tel.getOrd(), numSet);
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
        if (!CollectionUtils.isEmpty(erpCustomvaluesList)) {
            erpCustomvaluesMapper.insertList(erpCustomvaluesList);
        }


        //修改枚举类型
        if (!map.isEmpty()) {
            telMapper.updateCustomerField(map, tel.getOrd());
        }
        //修改普通扩展字段
        if (!map1.isEmpty()) {
            telMapper.updateCustomerField2(map1, tel.getOrd());
        }
    }

    @Override
    public Tel insertKey(Tel tel) {
        telMapper.inserKey(tel);
        return tel;
    }


}
