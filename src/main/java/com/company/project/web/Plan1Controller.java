package com.company.project.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Person;
import com.company.project.model.Plan1;
import com.company.project.model.Tags;
import com.company.project.model.Tel;
import com.company.project.service.PersonService;
import com.company.project.service.Plan1Service;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
    @Autowired
    @Resource PersonService personService;


    @PostMapping("/add")
    public Result add(Plan1 plan1, @RequestParam(defaultValue = "0") Long nowDate,@RequestParam(defaultValue = "") String name,@RequestParam(defaultValue = "") String name2) throws ParseException {



        Plan1 plan11 = plan1Service.savePlan1OneKey(plan1,nowDate,name,name2);
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
        if(plan1.getPerson()!=null){
            Person byId = personService.findById(plan1.getPerson());
            if(byId!=null){
                plan1.setPersonObj(byId);
            }

        }
        return ResultGenerator.genSuccessResult(plan1);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(required = false) Integer company, @RequestParam(defaultValue = "") String bumen, @RequestParam(defaultValue = "0") Long createDate1, @RequestParam(defaultValue = "0") Long createDate2, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, @RequestParam(defaultValue = "") String ywy) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> map = new HashMap<>();
        if (!StrUtils.isNull(ywy)) {
            map.put("ywy", ywy);
        }
        if (!StrUtils.isNull(keyword)) {
            map.put("keyword", keyword);
        }
        try {
            //开始创建时间
            if (createDate1 != 0) {
                map.put("createDate1", sdf.format(new Date(createDate1)));
            }
            //结束创建时间
            if (createDate2 != 0) {
                map.put("createDate2", sdf.format(new Date(createDate2+3600*24*1000)));
            }
        } catch (Exception e) {
            return ResultGenerator.genFailResult("日期格式有误");
        }
        if (!StrUtils.isNull(bumen)) {
            map.put("bumen", bumen + "部");
        }
        if (company != null) {
            map.put("company", company);
        }


        //对点赞列表利用set进行去重
        PageHelper.startPage(page, size);
        List<Plan1> list = plan1Service.findByMyCondition(map);
        for (int i = 0; i < list.size(); i++) {
            Plan1 plan1 = list.get(i);
            if (!CollectionUtils.isEmpty(plan1.getTagList())) {
                Set<Tags> t = new HashSet<>();
                t.addAll(plan1.getTagList());
                List<Tags> tlist = new ArrayList<>();
                tlist.addAll(t);
                plan1.setTagList(tlist);
            }

        }
        PageInfo pageInfo = new PageInfo(list);


        String[] fileds = {"ord", "username", "companyName", "dianpingCount", "introObj", "tags", "replyId", "others", "date7", "selfTag", "dianpingList", "tagList","company"};
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Plan1.class, fileds);
        String jsonStu = JSONArray.toJSONString(list, filter);
        List parse = (List) JSONArray.parse(jsonStu);

        pageInfo.setList(parse);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 业务员拜访统计
     */
    @PostMapping("/statistics")
    public Result statistics(@RequestParam Integer ywyId) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (ywyId == null) {
            return ResultGenerator.genFailResult("ywyId不能为空");
        }
        /*
         * 用于存储mybaties的查询参数
         * */
        HashMap<String, Object> map = new HashMap<String, Object>(7);
        map.put("ywyId", ywyId);
        //获得本年的第一天
        Date year = sdf.parse(new SimpleDateFormat("yyyy").format(new Date()) + "-01-01");
        //获取本月的第一天
        Date month = sdf.parse(new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01");
        //获取本周的第一天
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date week = cal.getTime();

        //将日期存入map集合中

        map.put("year", year);
        map.put("month", month);
        map.put("week", week);
        //从数据表plan1查询数据
        JSONObject obj = plan1Service.statistics(map);
        return ResultGenerator.genSuccessResult(obj);
    }


}
