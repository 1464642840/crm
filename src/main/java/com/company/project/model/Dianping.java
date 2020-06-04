package com.company.project.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import javax.persistence.*;

public class Dianping {
    @Id
    private Integer id;

    private Integer ord;

    private String intro;

    private String name;

    private Integer sort;

    private Integer cateid;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date date7;

    private Integer alt;

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
     * @return ord
     */
    public Integer getOrd() {
        return ord;
    }

    /**
     * @param ord
     */
    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    /**
     * @return intro
     */
    public String getIntro() {
        return intro;
    }

    /**
     * @param intro
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return cateid
     */
    public Integer getCateid() {
        return cateid;
    }

    /**
     * @param cateid
     */
    public void setCateid(Integer cateid) {
        this.cateid = cateid;
    }

    /**
     * @return date7
     */
    public Date getDate7() {
        return date7;
    }

    /**
     * @param date7
     */
    public void setDate7(Date date7) {
        this.date7 = date7;
    }

    /**
     * @return alt
     */
    public Integer getAlt() {
        return alt;
    }

    /**
     * @param alt
     */
    public void setAlt(Integer alt) {
        this.alt = alt;
    }
}