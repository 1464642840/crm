package com.company.project.service.impl;

import com.company.project.dao.PersonMapper;
import com.company.project.model.Person;
import com.company.project.service.PersonService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by CodeGenerator on 2020/05/14.
 */
@Service
@Transactional
public class PersonServiceImpl extends AbstractService<Person> implements PersonService {
    @Resource
    private PersonMapper personMapper;

    @Override
    public void saveOrupdate(List<Person> persons) {
        List<Person> updatePerson = new ArrayList<>();
        List<Person> savePerson = new ArrayList<>();

        for (Person person : persons) {
            if (person.getOrd() == null) {
                savePerson.add(person);
                continue;
            }
            updatePerson.add(person);
        }
        if (!updatePerson.isEmpty()) {
            personMapper.batchUpdate(updatePerson);
        }
        if (!savePerson.isEmpty()) {
            for (Person person : savePerson) {


                person.setSort(3 + "");
                person.setSort1(21 + "");
                person.setOrder1(2);
                person.setDate7(new Date());
                person.setDel(1);
                person.setBirthdayType(1);
                person.setBDays(-1);
                personMapper.insertSelective(person);
            }

        }
    }

    @Override
    public Person insert(Person person) {

        personMapper.insertKey(person);
        return person;
    }
}
