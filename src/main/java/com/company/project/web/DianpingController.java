package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Dianping;
import com.company.project.service.DianpingService;
import com.company.project.utils.string.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* Created by CodeGenerator on 2020/05/04.
*/
@RestController
@RequestMapping("/zb/dianping")
public class DianpingController {
    @Resource
    private DianpingService dianpingService;

    @PostMapping("/add")
    public Result add(Dianping dianping) {
        if(StrUtils.isNull(dianping.getIntro())){
            return ResultGenerator.genFailResult("评论内容不能为空!");
        }
        dianping.setDate7(new Date());
        dianping.setSort(1);
        dianpingService.saveOneKey(dianping);
        return ResultGenerator.genSuccessResult(dianping);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        dianpingService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Dianping dianping) {
        dianpingService.update(dianping);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Dianping dianping = dianpingService.findById(id);
        return ResultGenerator.genSuccessResult(dianping);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Dianping> list = dianpingService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
