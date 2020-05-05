package com.company.project.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Plan1;
import com.company.project.model.Tel;
import com.company.project.service.Plan1Service;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by CodeGenerator on 2020/04/24.
 */
@RestController
@RequestMapping("/zb/plan1")
public class Plan1Controller {
    @Resource
    private Plan1Service plan1Service;

    @PostMapping("/add")
    public Result add(Plan1 plan1,@RequestParam(defaultValue = "0") Long nowDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(nowDate);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);

        plan1.setGate(4);
        plan1.setSort1(1);

        plan1.setDate1(sdf.parse(sdf.format(date)));
        plan1.setDate8(new Date());
        plan1.setDate7(new Date());
        plan1.setIsxunhuan(0);
        plan1.setStarttime1(instance.get(Calendar.HOUR_OF_DAY)-1+"");
        plan1.setStarttime2(instance.get(Calendar.MINUTE)+"");
        plan1.setTime1(instance.get(Calendar.HOUR_OF_DAY)+1+"");
        plan1.setTime2(instance.get(Calendar.MINUTE)+"");
        plan1.setComplete(1+"");
        plan1.setCateid3(3);
        plan1.setIntro2("");
        plan1.setOption1(0);
        plan1.setDate4(sdf.parse(sdf.format(date)));
        plan1.setStartdate1(sdf.parse(sdf.format(date)));
        plan1.setOrder1(plan1.getCateid()+"");

        Plan1 plan11 = plan1Service.saveOneKey(plan1);
        return ResultGenerator.genSuccessResult(plan11.getOrd());
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        plan1Service.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Plan1 plan1) {
        plan1Service.update(plan1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Plan1 plan1 = plan1Service.findById(id);
        return ResultGenerator.genSuccessResult(plan1);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "") String bumen, @RequestParam(defaultValue = "0") Long createDate1, @RequestParam(defaultValue = "0") Long createDate2, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, @RequestParam(defaultValue = "") String ywy) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> map = new HashMap<>();
        if (!StrUtils.isNull(ywy)) {
            map.put("ywy", ywy);
        }
        if (!StrUtils.isNull(keyword)) {
            map.put("keyword", keyword);
        }
        try {
            if (createDate1 != 0) {
                map.put("createDate1", sdf.format(new Date(createDate1))); //开始创建时间
            }
            if (createDate2 != 0) {
                map.put("createDate2", sdf.format(new Date(createDate2))); //结束创建时间
            }
        } catch (Exception e) {
            return ResultGenerator.genFailResult("日期格式有误");
        }
        if (!StrUtils.isNull(bumen)) {
            map.put("bumen", bumen+"部");
        }


        PageHelper.startPage(page, size);
        List<Plan1> list = plan1Service.findByMyCondition(map);
        PageInfo pageInfo = new PageInfo(list);


        String[] fileds = {"ord","username","companyName","dianpingCount","introObj","tags","replyId","others","date7"};
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Plan1.class, fileds);
        String jsonStu = JSONArray.toJSONString(list, filter);
        String s = JSON.toJSONString(jsonStu, SerializerFeature.WriteDateUseDateFormat);
        List parse = (List) JSONArray.parse(s);

        pageInfo.setList(parse);
        return ResultGenerator.genSuccessResult(pageInfo);
    }



}
