package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.Tel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface TelMapper extends Mapper<Tel> {
    List<Tel> findByMyCondition(Map map);
}