package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.Gate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface GateMapper extends Mapper<Gate> {
    @Select("SELECT DISTINCT s.sort1 position,g.ord,g1.sort1 bumen,g1.ord bumenId FROM gate g LEFT JOIN sortonehy s ON g.workPosition=s.ord left join gate1 g1 on g1.ord=g.sorce WHERE g.username=#{ywy}")
    List<Map<Object,Object>> getPositionByName(@Param("ywy") String ywy);

    List<Map<Object, Object>> selectSlGandGcywy();
}