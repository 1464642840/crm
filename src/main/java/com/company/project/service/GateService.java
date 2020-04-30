package com.company.project.service;
import com.company.project.model.Gate;
import com.company.project.core.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/04/24.
 */
public interface GateService extends Service<Gate> {

    List<Map<Object,Object>> getPositionByName(String ywy);
}
