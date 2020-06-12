package com.company.project.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.dao.ErpCustomvaluesMapper;
import com.company.project.model.ErpCustomvalues;
import com.company.project.model.Tel;
import com.company.project.service.GateService;
import com.company.project.service.TelService;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by CodeGenerator on 2020/04/21.
 */
@RestController
@RequestMapping("/zb/tel")
public class TelController {
    @Resource
    private TelService telService;
    @Resource
    private GateService gateService;

    @Resource
    ErpCustomvaluesMapper erpCustomvaluesMapper;

    @PostMapping("/add")
    public Result add(Tel tel) {
        telService.save(tel);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        telService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Tel tel, HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();


        try {
            telService.updateCustInfo(tel, parameterMap);
        } catch (
                ServiceException e) {
            return ResultGenerator.genFailResult(e.getMessage());
        }


        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Tel tel = telService.findById(id);
        return ResultGenerator.genSuccessResult(tel);
    }

    @PostMapping("/judgeRepeat")
    public Result detail(@RequestParam String custName) {
        if (StrUtils.isNull(custName)) {
            return ResultGenerator.genSuccessResult(true);
        }
        Condition condition = new Condition(Tel.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("del", 1);
        criteria.andEqualTo("name", custName);
        List<Tel> byCondition = telService.findByCondition(condition);
        if (CollectionUtils.isEmpty(byCondition)) {
            return ResultGenerator.genSuccessResult(true);
        }
        return ResultGenerator.genSuccessResult(false);
    }


    @PostMapping("/getType")
    public Result getType(Integer ord) {
        ErpCustomvalues record = new ErpCustomvalues();
        record.setFieldsid(27);
        record.setOrderid(ord);
        ErpCustomvalues erpCustomvalues = erpCustomvaluesMapper.selectOne(record);
        return ResultGenerator.genSuccessResult(erpCustomvalues.getFvalue());
    }



    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Tel> list = telService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @CrossOrigin(origins = "*",maxAge = 3600)
    @PostMapping("/custList")
    public Result custList(@RequestParam(defaultValue = "") String position, @RequestParam(defaultValue = "0") Long visitDate1, @RequestParam(defaultValue = "0") Long visitDate2, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "") String custName, @RequestParam(defaultValue = "0") Long createDate1, @RequestParam(required = false) String businessType, @RequestParam(defaultValue = "0") Long createDate2, @RequestParam(defaultValue = "0") Double lng, @RequestParam(defaultValue = "0") Double lat, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "name") String order, @RequestParam(defaultValue = "5") Integer size, @RequestParam(defaultValue = "") String ywy) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // List<Tel> list = telService.findAll();
        if (StrUtils.isNull(ywy)) {
            return ResultGenerator.genFailResult("ywy不能为空");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ywy", ywy); //业务员
        map.put("order", order); //排序方式

        try {
            if (createDate1 != 0) {
                map.put("createDate1", sdf.format(new Date(createDate1))); //开始创建时间
            }
            if (createDate2 != 0) {
                map.put("createDate2", sdf.format(new Date(createDate2 + 3600 * 24 * 1000))); //结束创建时间
            }
            if (visitDate1 != 0) {
                map.put("visitDate1", sdf.format(new Date(visitDate1))); //开始拜访时间
            }
            if (visitDate2 != 0) {
                map.put("visitDate2", sdf.format(new Date(visitDate2 + 3600 * 24 * 1000))); //结束创建时间
            }
        } catch (Exception e) {
            return ResultGenerator.genFailResult("日期格式有误");
        }

        if (!StrUtils.isNull(businessType)) {
            map.put("businessType", businessType); //企业类型
        }
        if (!StrUtils.isNull(position)) {
            map.put("position", position); //职位
        } else {
            List<Map<Object, Object>> positionByName = gateService.getPositionByName(ywy);
            try {
                position = positionByName.get(0).get("position").toString();
            } catch (Exception e) {
                position = "普通员工";
            }
            map.put("position", position); //职位
        }
        map.put("size", size);
        map.put("page", page);
        if (!StrUtils.isNull(keyword)) {
            map.put("keyword", keyword); //关键字
        }
        if (!StrUtils.isNull(custName)) {
            map.put("custName", custName); //关键字
        }
        if ("distance".equals(order)) {
            if (lng * lat == 0) {
                return ResultGenerator.genFailResult("经纬度不能为空");
            }
            map.put("lng", lng);
            map.put("lat", lat);
        }

        List<Tel> list = new ArrayList<>();
        try {
            list=telService.findByMyCondition(map);
        } catch (Exception e) {
            return ResultGenerator.genFailResult(e.getMessage());
        }
        PageInfo pageInfo = new PageInfo(list);

        String[] fileds = {"ord", "khid", "name", "address", "person_name", "mobile", "businessType", "isHisCustomer"};
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Tel.class, fileds);
        String jsonStu = JSONArray.toJSONString(list, filter);
        List parse = (List) JSONArray.parse(jsonStu);
        pageInfo.setList(parse);


        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
