package com.company.project.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
public class Person {
    @Id
    private Integer ord;

    private String name;

    private String sex;

    private String age;

    private String jg;

    private String part1;

    private String job;

    private String phone;

    private String phone2;

    private String fax;

    private String mobile;

    private String email;

    private String msn;

    private String qq;

    private String zip;

    private String address;

    private String photos;

    private String gate;

    private String year1;

    private Integer company;

    private Integer area;

    private String sort;

    private String sort1;

    private String trade;

    private Integer event;

    private Integer chance;

    private Integer plan1;

    private Integer numc1;

    private Integer order1;

    private String xl;

    private String xw;

    private String zy;

    private String yx;

    private String mz;

    private String mobile2;

    private String sg;

    private String tz;

    private String tx;

    private String xm;

    private String xy;

    private String xyname;

    private String yj;

    private String yjsort;

    private String yjname;

    private String yjsize;

    private String hc;

    private String hcsort;

    private String jk;

    private String jb;

    private String jz;

    private String sc;

    private String scsort;

    private String scys;

    private String scpz;

    private String tezheng;

    private Integer person;

    private String gx;

    private Integer cateid;

    private Integer cateid2;

    private Integer cateid3;

    private Date date7;

    private Date date4;

    private Date date5;

    private Integer del;

    private Integer delcate;

    private Date deldate;

    private Date date8;

    private String pym;

    private Integer sort3;

    private String tezhen;

    private String alt;

    @Column(name = "birthdayType")
    private Integer birthdayType;

    @Column(name = "person_excel_drSign")
    private Long person_excel_drSign;

    @Column(name = "person_excel_drUser")
    private Integer person_excel_drUser;

    private Integer role;

    @Column(name = "bDays")
    private Integer bDays;

    @Column(name = "bDaysYear")
    private Integer bDaysYear;

    @Column(name = "weixinAcc")
    private String weixinAcc;

    private String joy;

    private String intro;


}