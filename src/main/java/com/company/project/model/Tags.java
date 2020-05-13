package com.company.project.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import javax.persistence.*;

public class Tags {
    @Id
    private Integer id;

    private Integer plan1;

    private String person;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return plan1
     */
    public Integer getPlan1() {
        return plan1;
    }

    /**
     * @param plan1
     */
    public void setPlan1(Integer plan1) {
        this.plan1 = plan1;
    }

    /**
     * @return person
     */
    public String getPerson() {
        return person;
    }

    /**
     * @param person
     */
    public void setPerson(String person) {
        this.person = person;
    }

    /**
     * @return time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(Date time) {
        this.time = time;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tags tags = (Tags) o;

        if (!person.equals(tags.person)) return false;
        return time.equals(tags.time);
    }

    @Override
    public int hashCode() {
        int result = person.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }
}