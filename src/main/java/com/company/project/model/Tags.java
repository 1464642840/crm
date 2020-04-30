package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

public class Tags {
    @Id
    private Integer id;

    private Integer plan1;

    private String person;

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
}