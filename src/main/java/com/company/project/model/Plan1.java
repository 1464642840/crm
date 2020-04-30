package com.company.project.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import com.alibaba.fastjson.JSON;
import tk.mybatis.mapper.annotation.*;

public class Plan1 {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer ord;

    private Integer gate;

    private String complete;

    private Integer sort1;

    private Date date1;

    private String time1;

    private String time2;

    private String order1;

    private Integer sort98;

    private Integer cateid;

    private Integer cateid2;

    private Integer cateid3;

    private Integer company;

    private Integer person;

    private Integer option1;

    private Integer chance;

    private Integer lcb;

    private Integer contract;

    private String pay;

    private Date date7;

    private Date date4;

    private Date date8;

    private Date startdate1;

    private String starttime1;

    private String starttime2;

    @Column(name = "isXunhuan")
    private Integer isxunhuan;

    private Integer alt;

    private String address;

    private BigDecimal lng;

    private BigDecimal lat;

    private String intro;

    private String intro2;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
     * @return gate
     */
    public Integer getGate() {
        return gate;
    }

    /**
     * @param gate
     */
    public void setGate(Integer gate) {
        this.gate = gate;
    }

    /**
     * @return complete
     */
    public String getComplete() {
        return complete;
    }

    /**
     * @param complete
     */
    public void setComplete(String complete) {
        this.complete = complete;
    }

    /**
     * @return sort1
     */
    public Integer getSort1() {
        return sort1;
    }

    /**
     * @param sort1
     */
    public void setSort1(Integer sort1) {
        this.sort1 = sort1;
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
     * @return time1
     */
    public String getTime1() {
        return time1;
    }

    /**
     * @param time1
     */
    public void setTime1(String time1) {
        this.time1 = time1;
    }

    /**
     * @return time2
     */
    public String getTime2() {
        return time2;
    }

    /**
     * @param time2
     */
    public void setTime2(String time2) {
        this.time2 = time2;
    }

    /**
     * @return order1
     */
    public String getOrder1() {
        return order1;
    }

    /**
     * @param order1
     */
    public void setOrder1(String order1) {
        this.order1 = order1;
    }

    /**
     * @return sort98
     */
    public Integer getSort98() {
        return sort98;
    }

    /**
     * @param sort98
     */
    public void setSort98(Integer sort98) {
        this.sort98 = sort98;
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
     * @return cateid2
     */
    public Integer getCateid2() {
        return cateid2;
    }

    /**
     * @param cateid2
     */
    public void setCateid2(Integer cateid2) {
        this.cateid2 = cateid2;
    }

    /**
     * @return cateid3
     */
    public Integer getCateid3() {
        return cateid3;
    }

    /**
     * @param cateid3
     */
    public void setCateid3(Integer cateid3) {
        this.cateid3 = cateid3;
    }

    /**
     * @return company
     */
    public Integer getCompany() {
        return company;
    }

    /**
     * @param company
     */
    public void setCompany(Integer company) {
        this.company = company;
    }

    /**
     * @return person
     */
    public Integer getPerson() {
        return person;
    }

    /**
     * @param person
     */
    public void setPerson(Integer person) {
        this.person = person;
    }

    /**
     * @return option1
     */
    public Integer getOption1() {
        return option1;
    }

    /**
     * @param option1
     */
    public void setOption1(Integer option1) {
        this.option1 = option1;
    }

    /**
     * @return chance
     */
    public Integer getChance() {
        return chance;
    }

    /**
     * @param chance
     */
    public void setChance(Integer chance) {
        this.chance = chance;
    }

    /**
     * @return lcb
     */
    public Integer getLcb() {
        return lcb;
    }

    /**
     * @param lcb
     */
    public void setLcb(Integer lcb) {
        this.lcb = lcb;
    }

    /**
     * @return contract
     */
    public Integer getContract() {
        return contract;
    }

    /**
     * @param contract
     */
    public void setContract(Integer contract) {
        this.contract = contract;
    }

    /**
     * @return pay
     */
    public String getPay() {
        return pay;
    }

    /**
     * @param pay
     */
    public void setPay(String pay) {
        this.pay = pay;
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
     * @return date4
     */
    public Date getDate4() {
        return date4;
    }

    /**
     * @param date4
     */
    public void setDate4(Date date4) {
        this.date4 = date4;
    }

    /**
     * @return date8
     */
    public Date getDate8() {
        return date8;
    }

    /**
     * @param date8
     */
    public void setDate8(Date date8) {
        this.date8 = date8;
    }

    /**
     * @return startdate1
     */
    public Date getStartdate1() {
        return startdate1;
    }

    /**
     * @param startdate1
     */
    public void setStartdate1(Date startdate1) {
        this.startdate1 = startdate1;
    }

    /**
     * @return starttime1
     */
    public String getStarttime1() {
        return starttime1;
    }

    /**
     * @param starttime1
     */
    public void setStarttime1(String starttime1) {
        this.starttime1 = starttime1;
    }

    /**
     * @return starttime2
     */
    public String getStarttime2() {
        return starttime2;
    }

    /**
     * @param starttime2
     */
    public void setStarttime2(String starttime2) {
        this.starttime2 = starttime2;
    }

    /**
     * @return isXunhuan
     */
    public Integer getIsxunhuan() {
        return isxunhuan;
    }

    /**
     * @param isxunhuan
     */
    public void setIsxunhuan(Integer isxunhuan) {
        this.isxunhuan = isxunhuan;
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

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return lng
     */
    public BigDecimal getLng() {
        return lng;
    }

    /**
     * @param lng
     */
    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    /**
     * @return lat
     */
    public BigDecimal getLat() {
        return lat;
    }

    /**
     * @param lat
     */
    public void setLat(BigDecimal lat) {
        this.lat = lat;
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
        this.introObj = JSON.parseObject(intro);
        this.intro = intro;
    }

    /**
     * @return intro2
     */
    public String getIntro2() {
        return intro2;
    }

    /**
     * @param intro2
     */
    public void setIntro2(String intro2) {
        this.intro2 = intro2;
    }


    @Transient
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Transient
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Transient
    private int dianpingCount;

    public int getDianpingCount() {
        return dianpingCount;
    }

    public void setDianpingCount(int dianpingCount) {
        this.dianpingCount = dianpingCount;
    }

    private int tags = 0;

    public int getTags() {
        return tags;
    }

    public void setTags(int tags) {
        this.tags = tags;
    }

    @Transient
    private JSON introObj;

    public JSON getIntroObj() {
        return introObj;
    }

    public void setIntroObj(JSON introObj) {
        this.introObj = introObj;
    }
}