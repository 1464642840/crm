package com.company.project.service;
import com.company.project.model.Tel;
import com.company.project.core.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/04/21.
 */
public interface TelService extends Service<Tel> {

    List<Tel> findByMyCondition(Map map);
}
