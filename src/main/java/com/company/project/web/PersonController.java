package com.company.project.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Person;
import com.company.project.model.Tel;
import com.company.project.service.PersonService;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2020/05/14.
 */
@RestController
@RequestMapping("/zb/person")
public class PersonController {
    @Resource
    private PersonService personService;

    @PostMapping("/add")
    public Result add(Person person) {
        personService.save(person);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        personService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/update")
    public Result update(@RequestBody List<Person> persons) {
        personService.saveOrupdate(persons);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/updateOne")
    public Result update(@RequestParam(defaultValue = "") Integer ord, @RequestParam(defaultValue = "") String company, @RequestParam(defaultValue = "") String person_name, @RequestParam(defaultValue = "") String phone, @RequestParam(defaultValue = "") String mobile, @RequestParam(defaultValue = "") String qq, @RequestParam(defaultValue = "") String email, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        Person p = new Person();
        if (!StrUtils.isNull(ord + "")) {
            p.setOrd(ord);
        }
        if (!StrUtils.isNull(company)) {
            p.setCompany(Integer.parseInt(company));
        }
        if (!StrUtils.isNull(person_name)) {
            p.setName(person_name);
        }
        if (!StrUtils.isNull(phone)) {
            p.setPhone(phone);
        }
        if (!StrUtils.isNull(mobile)) {
            p.setMobile(mobile);
        }
        if (!StrUtils.isNull(qq)) {
            p.setQq(qq);
        }
        if (!StrUtils.isNull(email)) {
            p.setEmail(email);
        }
        personService.updateSelective(p);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Person person = personService.findById(id);
        return ResultGenerator.genSuccessResult(person);
    }


    @PostMapping("/getlist")
    public Result getlist(@RequestParam(defaultValue = "") Integer ord, @RequestParam(defaultValue = "") Integer ywyId) {

        Condition c = new Condition(Person.class);
        Example.Criteria criteria = c.createCriteria();
        criteria.andEqualTo("company", ord);
        criteria.andEqualTo("cateid", ywyId);


        PageHelper.startPage(1, 100);
        List<Person> list = personService.findByCondition(c);
        PageInfo pageInfo = new PageInfo(list);

        String[] fileds = {"sex", "name", "address", "job", "mobile", "msn", "phone", "qq","email","ord"};
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Person.class, fileds);
        String jsonStu = JSONArray.toJSONString(list, filter);
        List parse = (List) JSONArray.parse(jsonStu);
        pageInfo.setList(parse);
        return ResultGenerator.genSuccessResult(pageInfo);


    }


    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "") String company, @RequestParam(defaultValue = "") String person_name, @RequestParam(defaultValue = "") String phone, @RequestParam(defaultValue = "") String mobile, @RequestParam(defaultValue = "") String qq, @RequestParam(defaultValue = "") String email, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {

        PageHelper.startPage(page, size);
        Condition c = new Condition(Person.class);
        Example.Criteria criteria = c.createCriteria();
        if (!StrUtils.isNull(person_name)) {
            criteria.andEqualTo("name", person_name);
        }
        if (!StrUtils.isNull(phone)) {
            criteria.andEqualTo("phone", phone);
        }
        if (!StrUtils.isNull(mobile)) {
            criteria.andEqualTo("mobile", mobile);
        }
        if (!StrUtils.isNull(company)) {
            criteria.andEqualTo("company", company);
        }
        List<Person> list = personService.findByCondition(c);

        if (list != null && list.size() >= 1) {
            return ResultGenerator.genSuccessResult(list.get(0).getOrd());
        }
        return ResultGenerator.genFailResult("");


    }
}
