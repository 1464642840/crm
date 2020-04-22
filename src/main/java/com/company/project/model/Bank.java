package com.company.project.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer bank;

    private BigDecimal money1;

    private BigDecimal money2;

    private Integer sort;

    private String intro;

    private Integer gl;

    private Integer gl2;

    private Integer cateid;

    private Date date1;

    private Date date7;

    private Integer del;

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
     * @return bank
     */
    public Integer getBank() {
        return bank;
    }

    /**
     * @param bank
     */
    public void setBank(Integer bank) {
        this.bank = bank;
    }

    /**
     * @return money1
     */
    public BigDecimal getMoney1() {
        return money1;
    }

    /**
     * @param money1
     */
    public void setMoney1(BigDecimal money1) {
        this.money1 = money1;
    }

    /**
     * @return money2
     */
    public BigDecimal getMoney2() {
        return money2;
    }

    /**
     * @param money2
     */
    public void setMoney2(BigDecimal money2) {
        this.money2 = money2;
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
     * @return gl
     */
    public Integer getGl() {
        return gl;
    }

    /**
     * @param gl
     */
    public void setGl(Integer gl) {
        this.gl = gl;
    }

    /**
     * @return gl2
     */
    public Integer getGl2() {
        return gl2;
    }

    /**
     * @param gl2
     */
    public void setGl2(Integer gl2) {
        this.gl2 = gl2;
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
     * @return date1
     */
    public Date getDate1() {
        return date1;
    }

    /**
     * @param date1
     */
    public void setDate1(Date date1) {
        this.date1 = date1;
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
     * @return del
     */
    public Integer getDel() {
        return del;
    }

    /**
     * @param del
     */
    public void setDel(Integer del) {
        this.del = del;
    }
}