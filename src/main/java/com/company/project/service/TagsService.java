package com.company.project.service;
import com.company.project.core.Result;
import com.company.project.model.Tags;
import com.company.project.core.Service;


/**
 * Created by CodeGenerator on 2020/04/26.
 */
public interface TagsService extends Service<Tags> {

    Result tags(Tags tags);
}
