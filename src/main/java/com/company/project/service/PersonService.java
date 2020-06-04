package com.company.project.service;
import com.company.project.model.Person;
import com.company.project.core.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2020/05/14.
 */
public interface PersonService extends Service<Person> {

    void saveOrupdate(List<Person> persons);

    Person insert(Person person);
}
