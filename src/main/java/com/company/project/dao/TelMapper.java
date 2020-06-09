package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.Tel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TelMapper extends Mapper<Tel> {
    List<Tel> findByMyCondition(Map map);

    void updateCustomerField(@Param("map") HashMap<String, String> map,@Param("ord") Integer ord);

    void updateCustomerField2(@Param("map") HashMap<String, String> map1, @Param("ord") Integer ord);

    List<String> selectExistExtendFields(@Param("ord") Integer ord, @Param("numSet") Set<String> numSet);

    void inserKey(Tel tel);

    List<Tel> findByBaiFangCustList(Map map);

    @Select(" select t.ord,t.name,(select erp.FValue from ERP_CustomValues erp where erp.OrderID=t.ord and erp.FieldsID=27) fenzu,(select erp.FValue from ERP_CustomValues erp where erp.OrderID=t.ord and erp.FieldsID=15) xin from tel t where ord=#{ord}")
    List<HashMap<String,Object>> selectByPrimaryKeyInfo(@Param("ord") Integer company);


}