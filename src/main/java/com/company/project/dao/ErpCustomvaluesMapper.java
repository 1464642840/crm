package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.ErpCustomvalues;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ErpCustomvaluesMapper extends Mapper<ErpCustomvalues> {
    @Update("update ERP_CustomValues set FValue=#{xin} where OrderID=#{company} and FieldsID=#{i}")
    void upDateFileds(@Param("i") int i, @Param("company") Integer company, @Param("xin") String xin);
}