package com.company.project.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Tel;
import com.company.project.service.TelService;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CodeGenerator on 2020/04/21.
 */
@RestController
@RequestMapping("/tel")
public class TelController {
    @Resource
    private TelService telService;

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
    public Result update(Tel tel) {
        telService.update(tel);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Tel tel = telService.findById(id);
        return ResultGenerator.genSuccessResult(tel);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Tel> list = telService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/custList")
    public Result custList(@RequestParam(defaultValue = "0") Double lng,@RequestParam(defaultValue = "0") Integer lat,@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "name") String order, @RequestParam(defaultValue = "0") Integer size, @RequestParam(defaultValue = "") String ywy) {

        // List<Tel> list = telService.findAll();
        if (StrUtils.isNull(ywy)) {
            return ResultGenerator.genFailResult("ywy不能为空");
        }
        Map<String,Object> map =  new HashMap<>();
        map.put("ywy",ywy);
        map.put("order",order);
        if("distance".equals(order)){
            if(lng*lat==0){
                return ResultGenerator.genFailResult("经纬度不能为空");
            }
            map.put("lng",lng);
            map.put("lat",lat);
        }
        PageHelper.startPage(page, size);
        List<Tel> list = telService.findByMyCondition(map);
        PageInfo pageInfo = new PageInfo(list);

        String[] fileds = {"ord", "khid", "name", "address","person_name","mobile"};
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Tel.class, fileds);
        String jsonStu = JSONArray.toJSONString(list, filter);
        List parse = (List) JSONArray.parse(jsonStu);
        pageInfo.setList(parse);


        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
