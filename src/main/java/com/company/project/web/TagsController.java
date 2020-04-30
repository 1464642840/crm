package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Tags;
import com.company.project.service.TagsService;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2020/04/26.
 */
@RestController
@RequestMapping("/zb/tags")
public class TagsController {
    @Resource
    private TagsService tagsService;

    @PostMapping("/add")
    public Result add(Tags tags) {
        if (StrUtils.isNull(tags.getPerson())) {
            return ResultGenerator.genFailResult("业务员姓名不能为空");
        }
        if (tags.getPlan1() == null) {
            return ResultGenerator.genFailResult("参数plan1不能为空");
        }
        Result res = tagsService.tags(tags);
        return res;
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        tagsService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Tags tags) {
        tagsService.update(tags);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Tags tags = tagsService.findById(id);
        return ResultGenerator.genSuccessResult(tags);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Tags> list = tagsService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
