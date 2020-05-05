package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Plan1;
import com.company.project.model.Reply;
import com.company.project.service.Plan1Service;
import com.company.project.service.ReplyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by CodeGenerator on 2020/04/30.
 */
@RestController
@RequestMapping("/zb/reply")
public class ReplyController {
    @Resource
    private ReplyService replyService;
    @Resource
    private Plan1Service plan1Service;

    @PostMapping("/add")
    public Result add(Reply reply, @RequestParam(defaultValue = "0") Long nowDate) {
        Calendar c = Calendar.getInstance();

        if (nowDate == null) {
            return ResultGenerator.genFailResult("日期不能为空");
        }
        Date date = new Date(nowDate);
        c.setTime(date);
        reply.setDate7(date);
        reply.setSort1(1);
        reply.setTime1(c.get(Calendar.HOUR_OF_DAY));
        replyService.dosSave(reply);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        replyService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Reply reply) {
        replyService.update(reply);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Reply reply = replyService.findById(id);
        return ResultGenerator.genSuccessResult(reply);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Reply> list = replyService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
