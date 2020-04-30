package com.company.project.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Gate {
    @Id
    private Integer ord;

    private String username;

    private String pw;

    private String name;

    private String title;

    private Integer cateid;

    private Integer sorce;

    private Integer sorce2;

    private String see1;

    private String cor;

    private String del1;

    private String share;

    private String order1;

    private String agree1;

    private String top1;

    private String con1;

    private String hk1;

    private String pro1;

    private String sh1;

    private String cg1;

    private String rk1;

    private String ck1;

    private String fh1;

    private String dy1;

    private String dc1;

    private Integer del;

    private BigDecimal numy;

    private BigDecimal numm;

    private String ygid;

    private String cardid;

    private String sex;

    private String jg;

    private String mz;

    private Date date1;

    private Date date2;

    private Date date3;

    private String xl;

    private String zy;

    private String xx;

    private String phone1;

    private String phone2;

    private String mobile;

    private String tc;

    private String ah;

    private Integer alt;

    private Date datealt;

    private Date date7;

    private String fax;

    private String email;

    private String address;

    private Integer num1;

    private String mac;

    private Integer jjgz;

    @Column(name = "time_login")
    private Date timeLogin;

    @Column(name = "on_line")
    private Integer onLine;

    @Column(name = "num_1")
    private Integer num_1;

    @Column(name = "num_2")
    private Integer num_2;

    @Column(name = "num_3")
    private Integer num_3;

    @Column(name = "num_4")
    private Integer num_4;

    @Column(name = "num_5")
    private Integer num_5;

    @Column(name = "num_6")
    private Integer num6;

    @Column(name = "num_7")
    private Integer num7;

    private Integer addcate;

    private Integer tj1;

    private Integer tj2;

    private Integer tj3;

    private Integer tj4;

    @Column(name = "num_ly")
    private Integer numLy;

    @Column(name = "Serial")
    private String serial;

    private Integer qbtc;

    @Column(name = "Channel")
    private Integer channel;

    @Column(name = "num_code")
    private String numCode;

    private Integer num2;

    private Integer num3;

    @Column(name = "callModel")
    private Integer callmodel;

    @Column(name = "callPreNum")
    private String callprenum;

    private Integer job;

    @Column(name = "num_week")
    private Integer numWeek;

    @Column(name = "num_month")
    private Integer numMonth;

    @Column(name = "num_year")
    private Integer numYear;

    @Column(name = "num1_xm")
    private Integer num1Xm;

    @Column(name = "num2_xm")
    private Integer num2Xm;

    @Column(name = "num3_xm")
    private Integer num3Xm;

    @Column(name = "mobile_kq")
    private Integer mobileKq;

    private Integer jmgou;

    @Column(name = "salaryClass")
    private Integer salaryclass;

    @Column(name = "num_gj_1")
    private BigDecimal numGj1;

    @Column(name = "num_gj_2")
    private BigDecimal numGj2;

    @Column(name = "GPS_Open")
    private Integer gpsOpen;

    @Column(name = "workPosition")
    private Integer workposition;

    @Column(name = "isMobileLoginOn")
    private Integer ismobileloginon;

    private Integer orgsid;

    private Integer partadmin;

    private Integer pricesorce;

    @Column(name = "import")
    private Integer imports;

    @Column(name = "MobVisitToken")
    private String mobvisittoken;

    private String weixin;

    private String photourl;

    private String intro;

    @Column(name = "sessionData")
    private String sessiondata;

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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return pw
     */
    public String getPw() {
        return pw;
    }

    /**
     * @param pw
     */
    public void setPw(String pw) {
        this.pw = pw;
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
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * @return sorce
     */
    public Integer getSorce() {
        return sorce;
    }

    /**
     * @param sorce
     */
    public void setSorce(Integer sorce) {
        this.sorce = sorce;
    }

    /**
     * @return sorce2
     */
    public Integer getSorce2() {
        return sorce2;
    }

    /**
     * @param sorce2
     */
    public void setSorce2(Integer sorce2) {
        this.sorce2 = sorce2;
    }

    /**
     * @return see1
     */
    public String getSee1() {
        return see1;
    }

    /**
     * @param see1
     */
    public void setSee1(String see1) {
        this.see1 = see1;
    }

    /**
     * @return cor
     */
    public String getCor() {
        return cor;
    }

    /**
     * @param cor
     */
    public void setCor(String cor) {
        this.cor = cor;
    }

    /**
     * @return del1
     */
    public String getDel1() {
        return del1;
    }

    /**
     * @param del1
     */
    public void setDel1(String del1) {
        this.del1 = del1;
    }

    /**
     * @return share
     */
    public String getShare() {
        return share;
    }

    /**
     * @param share
     */
    public void setShare(String share) {
        this.share = share;
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
     * @return agree1
     */
    public String getAgree1() {
        return agree1;
    }

    /**
     * @param agree1
     */
    public void setAgree1(String agree1) {
        this.agree1 = agree1;
    }

    /**
     * @return top1
     */
    public String getTop1() {
        return top1;
    }

    /**
     * @param top1
     */
    public void setTop1(String top1) {
        this.top1 = top1;
    }

    /**
     * @return con1
     */
    public String getCon1() {
        return con1;
    }

    /**
     * @param con1
     */
    public void setCon1(String con1) {
        this.con1 = con1;
    }

    /**
     * @return hk1
     */
    public String getHk1() {
        return hk1;
    }

    /**
     * @param hk1
     */
    public void setHk1(String hk1) {
        this.hk1 = hk1;
    }

    /**
     * @return pro1
     */
    public String getPro1() {
        return pro1;
    }

    /**
     * @param pro1
     */
    public void setPro1(String pro1) {
        this.pro1 = pro1;
    }

    /**
     * @return sh1
     */
    public String getSh1() {
        return sh1;
    }

    /**
     * @param sh1
     */
    public void setSh1(String sh1) {
        this.sh1 = sh1;
    }

    /**
     * @return cg1
     */
    public String getCg1() {
        return cg1;
    }

    /**
     * @param cg1
     */
    public void setCg1(String cg1) {
        this.cg1 = cg1;
    }

    /**
     * @return rk1
     */
    public String getRk1() {
        return rk1;
    }

    /**
     * @param rk1
     */
    public void setRk1(String rk1) {
        this.rk1 = rk1;
    }

    /**
     * @return ck1
     */
    public String getCk1() {
        return ck1;
    }

    /**
     * @param ck1
     */
    public void setCk1(String ck1) {
        this.ck1 = ck1;
    }

    /**
     * @return fh1
     */
    public String getFh1() {
        return fh1;
    }

    /**
     * @param fh1
     */
    public void setFh1(String fh1) {
        this.fh1 = fh1;
    }

    /**
     * @return dy1
     */
    public String getDy1() {
        return dy1;
    }

    /**
     * @param dy1
     */
    public void setDy1(String dy1) {
        this.dy1 = dy1;
    }

    /**
     * @return dc1
     */
    public String getDc1() {
        return dc1;
    }

    /**
     * @param dc1
     */
    public void setDc1(String dc1) {
        this.dc1 = dc1;
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

    /**
     * @return numy
     */
    public BigDecimal getNumy() {
        return numy;
    }

    /**
     * @param numy
     */
    public void setNumy(BigDecimal numy) {
        this.numy = numy;
    }

    /**
     * @return numm
     */
    public BigDecimal getNumm() {
        return numm;
    }

    /**
     * @param numm
     */
    public void setNumm(BigDecimal numm) {
        this.numm = numm;
    }

    /**
     * @return ygid
     */
    public String getYgid() {
        return ygid;
    }

    /**
     * @param ygid
     */
    public void setYgid(String ygid) {
        this.ygid = ygid;
    }

    /**
     * @return cardid
     */
    public String getCardid() {
        return cardid;
    }

    /**
     * @param cardid
     */
    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    /**
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return jg
     */
    public String getJg() {
        return jg;
    }

    /**
     * @param jg
     */
    public void setJg(String jg) {
        this.jg = jg;
    }

    /**
     * @return mz
     */
    public String getMz() {
        return mz;
    }

    /**
     * @param mz
     */
    public void setMz(String mz) {
        this.mz = mz;
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
     * @return date2
     */
    public Date getDate2() {
        return date2;
    }

    /**
     * @param date2
     */
    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    /**
     * @return date3
     */
    public Date getDate3() {
        return date3;
    }

    /**
     * @param date3
     */
    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    /**
     * @return xl
     */
    public String getXl() {
        return xl;
    }

    /**
     * @param xl
     */
    public void setXl(String xl) {
        this.xl = xl;
    }

    /**
     * @return zy
     */
    public String getZy() {
        return zy;
    }

    /**
     * @param zy
     */
    public void setZy(String zy) {
        this.zy = zy;
    }

    /**
     * @return xx
     */
    public String getXx() {
        return xx;
    }

    /**
     * @param xx
     */
    public void setXx(String xx) {
        this.xx = xx;
    }

    /**
     * @return phone1
     */
    public String getPhone1() {
        return phone1;
    }

    /**
     * @param phone1
     */
    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    /**
     * @return phone2
     */
    public String getPhone2() {
        return phone2;
    }

    /**
     * @param phone2
     */
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return tc
     */
    public String getTc() {
        return tc;
    }

    /**
     * @param tc
     */
    public void setTc(String tc) {
        this.tc = tc;
    }

    /**
     * @return ah
     */
    public String getAh() {
        return ah;
    }

    /**
     * @param ah
     */
    public void setAh(String ah) {
        this.ah = ah;
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
     * @return datealt
     */
    public Date getDatealt() {
        return datealt;
    }

    /**
     * @param datealt
     */
    public void setDatealt(Date datealt) {
        this.datealt = datealt;
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
     * @return fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @return num1
     */
    public Integer getNum1() {
        return num1;
    }

    /**
     * @param num1
     */
    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    /**
     * @return mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return jjgz
     */
    public Integer getJjgz() {
        return jjgz;
    }

    /**
     * @param jjgz
     */
    public void setJjgz(Integer jjgz) {
        this.jjgz = jjgz;
    }

    /**
     * @return time_login
     */
    public Date getTimeLogin() {
        return timeLogin;
    }

    /**
     * @param timeLogin
     */
    public void setTimeLogin(Date timeLogin) {
        this.timeLogin = timeLogin;
    }

    /**
     * @return on_line
     */
    public Integer getOnLine() {
        return onLine;
    }

    /**
     * @param onLine
     */
    public void setOnLine(Integer onLine) {
        this.onLine = onLine;
    }



    /**
     * @return addcate
     */
    public Integer getAddcate() {
        return addcate;
    }

    /**
     * @param addcate
     */
    public void setAddcate(Integer addcate) {
        this.addcate = addcate;
    }

    /**
     * @return tj1
     */
    public Integer getTj1() {
        return tj1;
    }

    /**
     * @param tj1
     */
    public void setTj1(Integer tj1) {
        this.tj1 = tj1;
    }

    /**
     * @return tj2
     */
    public Integer getTj2() {
        return tj2;
    }

    /**
     * @param tj2
     */
    public void setTj2(Integer tj2) {
        this.tj2 = tj2;
    }

    /**
     * @return tj3
     */
    public Integer getTj3() {
        return tj3;
    }

    /**
     * @param tj3
     */
    public void setTj3(Integer tj3) {
        this.tj3 = tj3;
    }

    /**
     * @return tj4
     */
    public Integer getTj4() {
        return tj4;
    }

    /**
     * @param tj4
     */
    public void setTj4(Integer tj4) {
        this.tj4 = tj4;
    }

    /**
     * @return num_ly
     */
    public Integer getNumLy() {
        return numLy;
    }

    /**
     * @param numLy
     */
    public void setNumLy(Integer numLy) {
        this.numLy = numLy;
    }

    /**
     * @return Serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return qbtc
     */
    public Integer getQbtc() {
        return qbtc;
    }

    /**
     * @param qbtc
     */
    public void setQbtc(Integer qbtc) {
        this.qbtc = qbtc;
    }

    /**
     * @return Channel
     */
    public Integer getChannel() {
        return channel;
    }

    /**
     * @param channel
     */
    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    /**
     * @return num_code
     */
    public String getNumCode() {
        return numCode;
    }

    /**
     * @param numCode
     */
    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    /**
     * @return num2
     */
    public Integer getNum2() {
        return num2;
    }

    /**
     * @param num2
     */
    public void setNum2(Integer num2) {
        this.num2 = num2;
    }

    /**
     * @return num3
     */
    public Integer getNum3() {
        return num3;
    }

    /**
     * @param num3
     */
    public void setNum3(Integer num3) {
        this.num3 = num3;
    }

    /**
     * @return callModel
     */
    public Integer getCallmodel() {
        return callmodel;
    }

    /**
     * @param callmodel
     */
    public void setCallmodel(Integer callmodel) {
        this.callmodel = callmodel;
    }

    /**
     * @return callPreNum
     */
    public String getCallprenum() {
        return callprenum;
    }

    /**
     * @param callprenum
     */
    public void setCallprenum(String callprenum) {
        this.callprenum = callprenum;
    }

    /**
     * @return job
     */
    public Integer getJob() {
        return job;
    }

    /**
     * @param job
     */
    public void setJob(Integer job) {
        this.job = job;
    }

    /**
     * @return num_week
     */
    public Integer getNumWeek() {
        return numWeek;
    }

    /**
     * @param numWeek
     */
    public void setNumWeek(Integer numWeek) {
        this.numWeek = numWeek;
    }

    /**
     * @return num_month
     */
    public Integer getNumMonth() {
        return numMonth;
    }

    /**
     * @param numMonth
     */
    public void setNumMonth(Integer numMonth) {
        this.numMonth = numMonth;
    }

    /**
     * @return num_year
     */
    public Integer getNumYear() {
        return numYear;
    }

    /**
     * @param numYear
     */
    public void setNumYear(Integer numYear) {
        this.numYear = numYear;
    }

    /**
     * @return num1_xm
     */
    public Integer getNum1Xm() {
        return num1Xm;
    }

    /**
     * @param num1Xm
     */
    public void setNum1Xm(Integer num1Xm) {
        this.num1Xm = num1Xm;
    }

    /**
     * @return num2_xm
     */
    public Integer getNum2Xm() {
        return num2Xm;
    }

    /**
     * @param num2Xm
     */
    public void setNum2Xm(Integer num2Xm) {
        this.num2Xm = num2Xm;
    }

    /**
     * @return num3_xm
     */
    public Integer getNum3Xm() {
        return num3Xm;
    }

    /**
     * @param num3Xm
     */
    public void setNum3Xm(Integer num3Xm) {
        this.num3Xm = num3Xm;
    }

    /**
     * @return mobile_kq
     */
    public Integer getMobileKq() {
        return mobileKq;
    }

    /**
     * @param mobileKq
     */
    public void setMobileKq(Integer mobileKq) {
        this.mobileKq = mobileKq;
    }

    /**
     * @return jmgou
     */
    public Integer getJmgou() {
        return jmgou;
    }

    /**
     * @param jmgou
     */
    public void setJmgou(Integer jmgou) {
        this.jmgou = jmgou;
    }

    /**
     * @return salaryClass
     */
    public Integer getSalaryclass() {
        return salaryclass;
    }

    /**
     * @param salaryclass
     */
    public void setSalaryclass(Integer salaryclass) {
        this.salaryclass = salaryclass;
    }

    /**
     * @return num_gj_1
     */
    public BigDecimal getNumGj1() {
        return numGj1;
    }

    /**
     * @param numGj1
     */
    public void setNumGj1(BigDecimal numGj1) {
        this.numGj1 = numGj1;
    }

    /**
     * @return num_gj_2
     */
    public BigDecimal getNumGj2() {
        return numGj2;
    }

    /**
     * @param numGj2
     */
    public void setNumGj2(BigDecimal numGj2) {
        this.numGj2 = numGj2;
    }

    /**
     * @return GPS_Open
     */
    public Integer getGpsOpen() {
        return gpsOpen;
    }

    /**
     * @param gpsOpen
     */
    public void setGpsOpen(Integer gpsOpen) {
        this.gpsOpen = gpsOpen;
    }

    /**
     * @return workPosition
     */
    public Integer getWorkposition() {
        return workposition;
    }

    /**
     * @param workposition
     */
    public void setWorkposition(Integer workposition) {
        this.workposition = workposition;
    }

    /**
     * @return isMobileLoginOn
     */
    public Integer getIsmobileloginon() {
        return ismobileloginon;
    }

    /**
     * @param ismobileloginon
     */
    public void setIsmobileloginon(Integer ismobileloginon) {
        this.ismobileloginon = ismobileloginon;
    }

    /**
     * @return orgsid
     */
    public Integer getOrgsid() {
        return orgsid;
    }

    /**
     * @param orgsid
     */
    public void setOrgsid(Integer orgsid) {
        this.orgsid = orgsid;
    }

    /**
     * @return partadmin
     */
    public Integer getPartadmin() {
        return partadmin;
    }

    /**
     * @param partadmin
     */
    public void setPartadmin(Integer partadmin) {
        this.partadmin = partadmin;
    }

    /**
     * @return pricesorce
     */
    public Integer getPricesorce() {
        return pricesorce;
    }

    /**
     * @param pricesorce
     */
    public void setPricesorce(Integer pricesorce) {
        this.pricesorce = pricesorce;
    }

    /**
     * @return import
     */
    public Integer getImports() {
        return imports;
    }

    /**
     * @param imports
     */
    public void setImport(Integer imports) {
        this.imports = imports;
    }

    /**
     * @return MobVisitToken
     */
    public String getMobvisittoken() {
        return mobvisittoken;
    }

    /**
     * @param mobvisittoken
     */
    public void setMobvisittoken(String mobvisittoken) {
        this.mobvisittoken = mobvisittoken;
    }

    /**
     * @return weixin
     */
    public String getWeixin() {
        return weixin;
    }

    /**
     * @param weixin
     */
    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    /**
     * @return photourl
     */
    public String getPhotourl() {
        return photourl;
    }

    /**
     * @param photourl
     */
    public void setPhotourl(String photourl) {
        this.photourl = photourl;
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
     * @return sessionData
     */
    public String getSessiondata() {
        return sessiondata;
    }

    /**
     * @param sessiondata
     */
    public void setSessiondata(String sessiondata) {
        this.sessiondata = sessiondata;
    }
}