package com.company.project.service;
import com.company.project.model.Reply;
import com.company.project.core.Service;


/**
 * Created by CodeGenerator on 2020/04/30.
 */
public interface ReplyService extends Service<Reply> {

    void dosSave(Reply reply);
}
