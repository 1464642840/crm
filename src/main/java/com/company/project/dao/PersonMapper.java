package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.Person;

import java.util.List;

public interface PersonMapper extends Mapper<Person> {
    void batchUpdate(List<Person> updatePerson);

    void insertKey(Person person);
}