package com.company.project.service;
import com.company.project.model.Plan1;
import com.company.project.core.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/04/24.
 */
public interface Plan1Service extends Service<Plan1> {

    List<Plan1> findByMyCondition(Map<String, Object> map);

    String tags(String ord);
}
