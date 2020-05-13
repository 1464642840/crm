package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.Plan1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Plan1Mapper extends Mapper<Plan1> {
    List<Plan1> findByMyCondition(Map<String, Object> map);

    HashMap<String, Object> statistics(HashMap<String, Object> map);
}