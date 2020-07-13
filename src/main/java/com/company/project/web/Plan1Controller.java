package com.company.project.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.model.*;
import com.company.project.service.*;
import com.company.project.utils.date.DateUtils;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
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

    @Resource
    PersonService personService;

    @Autowired
    private GateService gateService;

    @Autowired
    private DianpingService dianpingService;


    @PostMapping("/add")
    public Result add(Plan1 plan1, @RequestParam(defaultValue = "0") Long nowDate, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String name2) throws ParseException {


        Plan1 plan11 = plan1Service.savePlan1OneKey(plan1, nowDate, name, name2);
        return ResultGenerator.genSuccessResult(plan11.getOrd());
    }


    @PostMapping("/updatePlan")
    public Result updatePlan(Plan1 plan1, @RequestParam(defaultValue = "0") Long nowDate, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String name2, @RequestParam Integer replyId) throws ParseException {


        try {
            Plan1 plan11 = plan1Service.upDatePlan1OneKey(plan1, nowDate, name, name2, replyId);
            return ResultGenerator.genSuccessResult(plan11.getOrd());
        } catch (ServiceException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult(e.getMessage());
        }

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

    @Autowired
    private TagsService tagsService;

    @PostMapping("/details")
    public Result details(@RequestParam Integer id, @RequestParam(required = false) String person) {


        List<Plan1> list = plan1Service.getAllRecord(id);


        for (int i = 0; i < list.size(); i++) {
            Plan1 plan1 = list.get(i);

            //查询这个人是否给这条记录点赞
            plan1.setSelfTag(0);
            if (!StrUtils.isNull(person)) {
                Condition c = new Condition(Tags.class);
                Example.Criteria criteria = c.createCriteria();
                criteria.andEqualTo("person", person);
                criteria.andEqualTo("plan1", plan1.getOrd());

                List<Tags> byCondition = tagsService.findByCondition(c);
                if (!CollectionUtils.isEmpty(byCondition)) {
                    plan1.setSelfTag(1);
                }
            }

            //查询联系人的信息和用户名
            plan1.setUsername(gateService.findById(plan1.getCateid()).getUsername());
            if (plan1.getPerson() != null) {
                Person byId = personService.findById(plan1.getPerson());
                if (byId != null) {
                    plan1.setPersonObj(byId);
                }


            }
            list.set(i, plan1);

        }
        return ResultGenerator.genSuccessResult(list);
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Plan1 plan1 = plan1Service.findById(id);
        if (plan1.getPerson() != null) {
            Person byId = personService.findById(plan1.getPerson());
            if (byId != null) {
                plan1.setPersonObj(byId);
            }

        }
        return ResultGenerator.genSuccessResult(plan1);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(required = false) String position, @RequestParam(required = false) Integer company, @RequestParam(defaultValue = "") String bumen, @RequestParam(defaultValue = "0") Long createDate1, @RequestParam(defaultValue = "0") Long createDate2, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer size, @RequestParam(defaultValue = "") String ywy) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> map = new HashMap<>();
        if (!StrUtils.isNull(ywy)) {
            map.put("ywy", ywy);
        }


        if (!StrUtils.isNull(position)) {
            map.put("position", position);
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
                map.put("createDate2", sdf.format(new Date(createDate2 + 3600 * 24 * 1000)));
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
        map.put("page", page);
        map.put("size", size);


        //对点赞列表利用set进行去重

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


        String[] fileds = {"ord", "username", "companyName", "dianpingCount", "introObj", "tags", "replyId", "others", "date7", "selfTag", "dianpingList", "tagList", "company", "isPeitong"};
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
    public Result statistics(@RequestParam Integer ywyId, @RequestParam String position) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        /*
         * 用于存储mybaties的查询参数
         * */
        HashMap<String, Object> map = new HashMap<String, Object>(7);
        if (ywyId != null) {
            map.put("ywyId", ywyId);
        }
        map.put("position", position);
        //获得本年的第一天
        Date year = sdf.parse(new SimpleDateFormat("yyyy").format(new Date()) + "-01-01");
        //获取本月的第一天
        Date month = sdf.parse(new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01");
        //获取本周的第一天
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date week = sdf.parse(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));

        //获取当前
        Date day = sdf.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        //获取当前季度
        Date quarter = sdf.parse(new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.getCurrQuarter()));
        //将日期存入map集合中

        map.put("year", year);
        map.put("month", month);
        map.put("week", week);
        map.put("day", day);
        map.put("quarter", quarter);
        //从数据表plan1查询数据
        JSONObject obj = plan1Service.statistics(map);
        return ResultGenerator.genSuccessResult(obj);
    }

    /**
     * 业务员拜访统计导出
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/export")
    public Result statistics(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, @RequestParam(required = false) String ywyIds, @RequestParam(required = false) String custIds, @RequestParam String type, @RequestParam String bumen) throws ParseException {
        String url = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
        Date sd = null;
        Date ed = null;
        if (!StrUtils.isNull(endDate)) {
            ed = simpleDateFormat.parse(endDate);
            ed = new Date(ed.getTime() + 3600 * 24 * 1000);
        }
        if (!StrUtils.isNull(startDate)) {
            sd = simpleDateFormat.parse(startDate);
        }

        try {
            if (!StrUtils.isNull(ywyIds)) {
                url = plan1Service.doExport(sd, ed, ywyIds, type, bumen);
            } else if (!StrUtils.isNull(custIds)) {
                url = plan1Service.doExport2(sd, ed, custIds, type, bumen);
            }
        } catch (ServiceException e) {
            return ResultGenerator.genFailResult(e.getMessage());
        }
        return ResultGenerator.genSuccessResult(url);
    }


    /**
     * 文件下载接口
     */
    @RequestMapping("/download.do")
    public void download(HttpServletResponse response, HttpServletRequest request) throws IOException {

        String root = request.getServletContext().getRealPath("/");
        String fileName = request.getParameter("fileName");

        File file = new File("E:/springboot/" + fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        fileName = fileName.substring(0, fileName.indexOf("VVV"));
        byte[] bytes = IOUtils.toByteArray(fileInputStream);
        response.reset();// 如果有换行，对于文本文件没有什么问题，但是对于其它格
        // 式，比如AutoCAD、Word、Excel等文件下载下来的文件中就会多出一些换行符//0x0d和0x0a，这样可能导致某些格式的文件无法打开，有些也可以正常打开。同//时response.reset()这种方式也能清空缓冲区,
        // 防止页面中的空行等输出到下载内容里去
        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");// firefox浏览器
        else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)
            fileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
        else {
            fileName = URLEncoder.encode(fileName, "UTF-8");// 其他浏览器
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        response.setHeader("Connection", "close");
        IOUtils.write(bytes, response.getOutputStream());
        fileInputStream.close();
        file.delete();
    }


}
