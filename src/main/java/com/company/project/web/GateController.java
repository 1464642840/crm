package com.company.project.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Gate;
import com.company.project.model.Plan1;
import com.company.project.service.GateService;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by CodeGenerator on 2020/04/24.
*/
@RestController
@RequestMapping("/zb/gate")
public class GateController {
    @Resource
    private GateService gateService;

    @PostMapping("/add")
    public Result add(Gate gate) {
        gateService.save(gate);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        gateService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Gate gate) {
        gateService.update(gate);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Gate gate = gateService.findById(id);
        return ResultGenerator.genSuccessResult(gate);
    }

    @CrossOrigin(origins = "*",maxAge = 3600)
    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer size,@RequestParam(defaultValue="0") Integer bumenId) {
        PageHelper.startPage(page, size);
        Map<String,Object> map = new HashMap<>();
        if(bumenId!=0){
            map.put("bumenId",bumenId);
        }
        List<Gate> list = gateService.findByMyCondition(map);
        PageInfo pageInfo = new PageInfo(list);
        String[] fileds = {"ord","username"};
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Gate.class, fileds);
        String jsonStu = JSONArray.toJSONString(list, filter);
        List parse = (List) JSONArray.parse(jsonStu);
        pageInfo.setList(parse);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/getPosition")
    public Result getPosition(@RequestParam(defaultValue = "") String ywy) {
        if(StrUtils.isNull(ywy)){
            return ResultGenerator.genFailResult("姓名不能为空");
        }

        List<Map<Object,Object>> position =  gateService.getPositionByName(ywy);
        if(CollectionUtils.isEmpty(position)){
            return ResultGenerator.genFailResult("无法找到业务员职位信息");
        }
        return ResultGenerator.genSuccessResult(position.get(0));
    }
}
