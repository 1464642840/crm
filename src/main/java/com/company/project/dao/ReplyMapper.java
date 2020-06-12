package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.Reply;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ReplyMapper extends Mapper<Reply> {


    List<Map<Object, Object>> selectGroupByYwy(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("type") String type);


    List<Map<Object, Object>> selectVisitCountGroupByYwy(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("type") String type);

    @Select("select  name,is_new,COUNT(name) counts from reply where name=#{ywy} and date7<='2020-06-03 23:59:59' and date7>'2020-01-01'  group by name,is_new")
    List<HashMap<String, Object>> getByTest(@Param("ywy") String string);

    List<String> getYwyList(String type);
}