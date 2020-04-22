package com.company.project.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Tel {
    @Id
    private Integer ord;

    private String sort;

    private String name;

    private String khid;

    private Integer area;

    private Integer trade;

    private Integer sort1;

    private Integer sort2;

    private Integer ly;

    private Integer jz;

    private Integer person;

    private String phone;

    private String fax;

    private String email;

    private String faren;

    private BigDecimal zijin;

    private Integer pernum1;

    private Integer pernum2;

    private String url;

    private String zip;

    private String address;

    private String gate;

    private String x;

    private String h;

    private String f;

    private BigDecimal num1;

    private BigDecimal num2;

    private String share;

    private Integer order1;

    private Integer cateadd;

    private Integer cateorder1;

    private Integer cateid;

    private Integer cateid2;

    private Integer cateid3;

    private Integer cateid4;

    private Integer cateidgq;

    private Date date2;

    private Date date1;

    private Date datepro;

    private Date dategq;

    private Integer profect1;

    private Integer del;

    private Integer delcate;

    private Date deldate;

    private Date date8;

    private Date datealt;

    @Column(name = "bank_1")
    private String bank1;

    @Column(name = "bank_2")
    private String bank2;

    @Column(name = "bank_7")
    private String bank7;

    @Column(name = "bank_3")
    private String bank3;

    @Column(name = "bank_4")
    private String bank4;

    @Column(name = "bank_5")
    private String bank5;

    @Column(name = "bank_6")
    private String bank6;

    @Column(name = "bank2_1")
    private String bank21;

    @Column(name = "bank2_2")
    private String bank22;

    @Column(name = "bank2_7")
    private String bank27;

    @Column(name = "bank2_3")
    private String bank23;

    @Column(name = "bank2_4")
    private String bank24;

    @Column(name = "bank2_5")
    private String bank25;

    @Column(name = "bank2_6")
    private String bank26;

    private Integer fkdays;

    private Integer fkdate;

    private BigDecimal jf;

    private BigDecimal jf2;

    private Integer company;

    private String pym;

    private Integer sort3;

    private Date datelast;

    private Integer sortfq;

    private String zdy1;

    private String zdy2;

    private String zdy3;

    private String zdy4;

    private Integer zdy5;

    private Integer zdy6;

    @Column(name = "hk_xz")
    private BigDecimal hkXz;

    private BigDecimal money1;

    private Integer hmd;

    private Integer sharecontact;

    @Column(name = "replyShare")
    private Integer replyshare;

    @Column(name = "ModifyStamp")
    private String modifystamp;

    @Column(name = "tel_excel_drSign")
    private Long telExcelDrsign;

    @Column(name = "tel_excel_drUser")
    private Integer telExcelDruser;

    private Integer sp;

    @Column(name = "cateid_sp")
    private Integer cateidSp;

    @Column(name = "status_sp")
    private Integer statusSp;

    @Column(name = "date_sp")
    private Date dateSp;

    @Column(name = "intro_sp_cateid")
    private Integer introSpCateid;

    private Integer credit;

    @Column(name = "isNeedQuali")
    private Integer isneedquali;

    private Integer qualifications;

    @Column(name = "sp_qualifications")
    private Integer spQualifications;

    @Column(name = "cateid_sp_qualifications")
    private Integer cateidSpQualifications;

    @Column(name = "status_sp_qualifications")
    private Integer statusSpQualifications;

    private BigDecimal lat;

    private BigDecimal lng;

    private String person_name;
    private String mobile;

    private Integer hascoord;

    private String product;

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String intro;

    private String c2;

    private String c3;

    private String c4;


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
     * @return sort
     */
    public String getSort() {
        return sort;
    }

    /**
     * @param sort
     */
    public void setSort(String sort) {
        this.sort = sort;
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
     * @return khid
     */
    public String getKhid() {
        return khid;
    }

    /**
     * @param khid
     */
    public void setKhid(String khid) {
        this.khid = khid;
    }

    /**
     * @return area
     */
    public Integer getArea() {
        return area;
    }

    /**
     * @param area
     */
    public void setArea(Integer area) {
        this.area = area;
    }

    /**
     * @return trade
     */
    public Integer getTrade() {
        return trade;
    }

    /**
     * @param trade
     */
    public void setTrade(Integer trade) {
        this.trade = trade;
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
     * @return sort2
     */
    public Integer getSort2() {
        return sort2;
    }

    /**
     * @param sort2
     */
    public void setSort2(Integer sort2) {
        this.sort2 = sort2;
    }

    /**
     * @return ly
     */
    public Integer getLy() {
        return ly;
    }

    /**
     * @param ly
     */
    public void setLy(Integer ly) {
        this.ly = ly;
    }

    /**
     * @return jz
     */
    public Integer getJz() {
        return jz;
    }

    /**
     * @param jz
     */
    public void setJz(Integer jz) {
        this.jz = jz;
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
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
     * @return faren
     */
    public String getFaren() {
        return faren;
    }

    /**
     * @param faren
     */
    public void setFaren(String faren) {
        this.faren = faren;
    }

    /**
     * @return zijin
     */
    public BigDecimal getZijin() {
        return zijin;
    }

    /**
     * @param zijin
     */
    public void setZijin(BigDecimal zijin) {
        this.zijin = zijin;
    }

    /**
     * @return pernum1
     */
    public Integer getPernum1() {
        return pernum1;
    }

    /**
     * @param pernum1
     */
    public void setPernum1(Integer pernum1) {
        this.pernum1 = pernum1;
    }

    /**
     * @return pernum2
     */
    public Integer getPernum2() {
        return pernum2;
    }

    /**
     * @param pernum2
     */
    public void setPernum2(Integer pernum2) {
        this.pernum2 = pernum2;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip
     */
    public void setZip(String zip) {
        this.zip = zip;
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
     * @return gate
     */
    public String getGate() {
        return gate;
    }

    /**
     * @param gate
     */
    public void setGate(String gate) {
        this.gate = gate;
    }

    /**
     * @return x
     */
    public String getX() {
        return x;
    }

    /**
     * @param x
     */
    public void setX(String x) {
        this.x = x;
    }

    /**
     * @return h
     */
    public String getH() {
        return h;
    }

    /**
     * @param h
     */
    public void setH(String h) {
        this.h = h;
    }

    /**
     * @return f
     */
    public String getF() {
        return f;
    }

    /**
     * @param f
     */
    public void setF(String f) {
        this.f = f;
    }

    /**
     * @return num1
     */
    public BigDecimal getNum1() {
        return num1;
    }

    /**
     * @param num1
     */
    public void setNum1(BigDecimal num1) {
        this.num1 = num1;
    }

    /**
     * @return num2
     */
    public BigDecimal getNum2() {
        return num2;
    }

    /**
     * @param num2
     */
    public void setNum2(BigDecimal num2) {
        this.num2 = num2;
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
    public Integer getOrder1() {
        return order1;
    }

    /**
     * @param order1
     */
    public void setOrder1(Integer order1) {
        this.order1 = order1;
    }

    /**
     * @return cateadd
     */
    public Integer getCateadd() {
        return cateadd;
    }

    /**
     * @param cateadd
     */
    public void setCateadd(Integer cateadd) {
        this.cateadd = cateadd;
    }

    /**
     * @return cateorder1
     */
    public Integer getCateorder1() {
        return cateorder1;
    }

    /**
     * @param cateorder1
     */
    public void setCateorder1(Integer cateorder1) {
        this.cateorder1 = cateorder1;
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
     * @return cateid4
     */
    public Integer getCateid4() {
        return cateid4;
    }

    /**
     * @param cateid4
     */
    public void setCateid4(Integer cateid4) {
        this.cateid4 = cateid4;
    }

    /**
     * @return cateidgq
     */
    public Integer getCateidgq() {
        return cateidgq;
    }

    /**
     * @param cateidgq
     */
    public void setCateidgq(Integer cateidgq) {
        this.cateidgq = cateidgq;
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
     * @return datepro
     */
    public Date getDatepro() {
        return datepro;
    }

    /**
     * @param datepro
     */
    public void setDatepro(Date datepro) {
        this.datepro = datepro;
    }

    /**
     * @return dategq
     */
    public Date getDategq() {
        return dategq;
    }

    /**
     * @param dategq
     */
    public void setDategq(Date dategq) {
        this.dategq = dategq;
    }

    /**
     * @return profect1
     */
    public Integer getProfect1() {
        return profect1;
    }

    /**
     * @param profect1
     */
    public void setProfect1(Integer profect1) {
        this.profect1 = profect1;
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
     * @return delcate
     */
    public Integer getDelcate() {
        return delcate;
    }

    /**
     * @param delcate
     */
    public void setDelcate(Integer delcate) {
        this.delcate = delcate;
    }

    /**
     * @return deldate
     */
    public Date getDeldate() {
        return deldate;
    }

    /**
     * @param deldate
     */
    public void setDeldate(Date deldate) {
        this.deldate = deldate;
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
     * @return bank_1
     */
    public String getBank1() {
        return bank1;
    }

    /**
     * @param bank1
     */
    public void setBank1(String bank1) {
        this.bank1 = bank1;
    }

    /**
     * @return bank_2
     */
    public String getBank2() {
        return bank2;
    }

    /**
     * @param bank2
     */
    public void setBank2(String bank2) {
        this.bank2 = bank2;
    }

    /**
     * @return bank_7
     */
    public String getBank7() {
        return bank7;
    }

    /**
     * @param bank7
     */
    public void setBank7(String bank7) {
        this.bank7 = bank7;
    }

    /**
     * @return bank_3
     */
    public String getBank3() {
        return bank3;
    }

    /**
     * @param bank3
     */
    public void setBank3(String bank3) {
        this.bank3 = bank3;
    }

    /**
     * @return bank_4
     */
    public String getBank4() {
        return bank4;
    }

    /**
     * @param bank4
     */
    public void setBank4(String bank4) {
        this.bank4 = bank4;
    }

    /**
     * @return bank_5
     */
    public String getBank5() {
        return bank5;
    }

    /**
     * @param bank5
     */
    public void setBank5(String bank5) {
        this.bank5 = bank5;
    }

    /**
     * @return bank_6
     */
    public String getBank6() {
        return bank6;
    }

    /**
     * @param bank6
     */
    public void setBank6(String bank6) {
        this.bank6 = bank6;
    }

    /**
     * @return bank2_1
     */
    public String getBank21() {
        return bank21;
    }

    /**
     * @param bank21
     */
    public void setBank21(String bank21) {
        this.bank21 = bank21;
    }

    /**
     * @return bank2_2
     */
    public String getBank22() {
        return bank22;
    }

    /**
     * @param bank22
     */
    public void setBank22(String bank22) {
        this.bank22 = bank22;
    }

    /**
     * @return bank2_7
     */
    public String getBank27() {
        return bank27;
    }

    /**
     * @param bank27
     */
    public void setBank27(String bank27) {
        this.bank27 = bank27;
    }

    /**
     * @return bank2_3
     */
    public String getBank23() {
        return bank23;
    }

    /**
     * @param bank23
     */
    public void setBank23(String bank23) {
        this.bank23 = bank23;
    }

    /**
     * @return bank2_4
     */
    public String getBank24() {
        return bank24;
    }

    /**
     * @param bank24
     */
    public void setBank24(String bank24) {
        this.bank24 = bank24;
    }

    /**
     * @return bank2_5
     */
    public String getBank25() {
        return bank25;
    }

    /**
     * @param bank25
     */
    public void setBank25(String bank25) {
        this.bank25 = bank25;
    }

    /**
     * @return bank2_6
     */
    public String getBank26() {
        return bank26;
    }

    /**
     * @param bank26
     */
    public void setBank26(String bank26) {
        this.bank26 = bank26;
    }

    /**
     * @return fkdays
     */
    public Integer getFkdays() {
        return fkdays;
    }

    /**
     * @param fkdays
     */
    public void setFkdays(Integer fkdays) {
        this.fkdays = fkdays;
    }

    /**
     * @return fkdate
     */
    public Integer getFkdate() {
        return fkdate;
    }

    /**
     * @param fkdate
     */
    public void setFkdate(Integer fkdate) {
        this.fkdate = fkdate;
    }

    /**
     * @return jf
     */
    public BigDecimal getJf() {
        return jf;
    }

    /**
     * @param jf
     */
    public void setJf(BigDecimal jf) {
        this.jf = jf;
    }

    /**
     * @return jf2
     */
    public BigDecimal getJf2() {
        return jf2;
    }

    /**
     * @param jf2
     */
    public void setJf2(BigDecimal jf2) {
        this.jf2 = jf2;
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
     * @return pym
     */
    public String getPym() {
        return pym;
    }

    /**
     * @param pym
     */
    public void setPym(String pym) {
        this.pym = pym;
    }

    /**
     * @return sort3
     */
    public Integer getSort3() {
        return sort3;
    }

    /**
     * @param sort3
     */
    public void setSort3(Integer sort3) {
        this.sort3 = sort3;
    }

    /**
     * @return datelast
     */
    public Date getDatelast() {
        return datelast;
    }

    /**
     * @param datelast
     */
    public void setDatelast(Date datelast) {
        this.datelast = datelast;
    }

    /**
     * @return sortfq
     */
    public Integer getSortfq() {
        return sortfq;
    }

    /**
     * @param sortfq
     */
    public void setSortfq(Integer sortfq) {
        this.sortfq = sortfq;
    }

    /**
     * @return zdy1
     */
    public String getZdy1() {
        return zdy1;
    }

    /**
     * @param zdy1
     */
    public void setZdy1(String zdy1) {
        this.zdy1 = zdy1;
    }

    /**
     * @return zdy2
     */
    public String getZdy2() {
        return zdy2;
    }

    /**
     * @param zdy2
     */
    public void setZdy2(String zdy2) {
        this.zdy2 = zdy2;
    }

    /**
     * @return zdy3
     */
    public String getZdy3() {
        return zdy3;
    }

    /**
     * @param zdy3
     */
    public void setZdy3(String zdy3) {
        this.zdy3 = zdy3;
    }

    /**
     * @return zdy4
     */
    public String getZdy4() {
        return zdy4;
    }

    /**
     * @param zdy4
     */
    public void setZdy4(String zdy4) {
        this.zdy4 = zdy4;
    }

    /**
     * @return zdy5
     */
    public Integer getZdy5() {
        return zdy5;
    }

    /**
     * @param zdy5
     */
    public void setZdy5(Integer zdy5) {
        this.zdy5 = zdy5;
    }

    /**
     * @return zdy6
     */
    public Integer getZdy6() {
        return zdy6;
    }

    /**
     * @param zdy6
     */
    public void setZdy6(Integer zdy6) {
        this.zdy6 = zdy6;
    }

    /**
     * @return hk_xz
     */
    public BigDecimal getHkXz() {
        return hkXz;
    }

    /**
     * @param hkXz
     */
    public void setHkXz(BigDecimal hkXz) {
        this.hkXz = hkXz;
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
     * @return hmd
     */
    public Integer getHmd() {
        return hmd;
    }

    /**
     * @param hmd
     */
    public void setHmd(Integer hmd) {
        this.hmd = hmd;
    }

    /**
     * @return sharecontact
     */
    public Integer getSharecontact() {
        return sharecontact;
    }

    /**
     * @param sharecontact
     */
    public void setSharecontact(Integer sharecontact) {
        this.sharecontact = sharecontact;
    }

    /**
     * @return replyShare
     */
    public Integer getReplyshare() {
        return replyshare;
    }

    /**
     * @param replyshare
     */
    public void setReplyshare(Integer replyshare) {
        this.replyshare = replyshare;
    }

    /**
     * @return ModifyStamp
     */
    public String getModifystamp() {
        return modifystamp;
    }

    /**
     * @param modifystamp
     */
    public void setModifystamp(String modifystamp) {
        this.modifystamp = modifystamp;
    }

    /**
     * @return tel_excel_drSign
     */
    public Long getTelExcelDrsign() {
        return telExcelDrsign;
    }

    /**
     * @param telExcelDrsign
     */
    public void setTelExcelDrsign(Long telExcelDrsign) {
        this.telExcelDrsign = telExcelDrsign;
    }

    /**
     * @return tel_excel_drUser
     */
    public Integer getTelExcelDruser() {
        return telExcelDruser;
    }

    /**
     * @param telExcelDruser
     */
    public void setTelExcelDruser(Integer telExcelDruser) {
        this.telExcelDruser = telExcelDruser;
    }

    /**
     * @return sp
     */
    public Integer getSp() {
        return sp;
    }

    /**
     * @param sp
     */
    public void setSp(Integer sp) {
        this.sp = sp;
    }

    /**
     * @return cateid_sp
     */
    public Integer getCateidSp() {
        return cateidSp;
    }

    /**
     * @param cateidSp
     */
    public void setCateidSp(Integer cateidSp) {
        this.cateidSp = cateidSp;
    }

    /**
     * @return status_sp
     */
    public Integer getStatusSp() {
        return statusSp;
    }

    /**
     * @param statusSp
     */
    public void setStatusSp(Integer statusSp) {
        this.statusSp = statusSp;
    }

    /**
     * @return date_sp
     */
    public Date getDateSp() {
        return dateSp;
    }

    /**
     * @param dateSp
     */
    public void setDateSp(Date dateSp) {
        this.dateSp = dateSp;
    }

    /**
     * @return intro_sp_cateid
     */
    public Integer getIntroSpCateid() {
        return introSpCateid;
    }

    /**
     * @param introSpCateid
     */
    public void setIntroSpCateid(Integer introSpCateid) {
        this.introSpCateid = introSpCateid;
    }

    /**
     * @return credit
     */
    public Integer getCredit() {
        return credit;
    }

    /**
     * @param credit
     */
    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    /**
     * @return isNeedQuali
     */
    public Integer getIsneedquali() {
        return isneedquali;
    }

    /**
     * @param isneedquali
     */
    public void setIsneedquali(Integer isneedquali) {
        this.isneedquali = isneedquali;
    }

    /**
     * @return qualifications
     */
    public Integer getQualifications() {
        return qualifications;
    }

    /**
     * @param qualifications
     */
    public void setQualifications(Integer qualifications) {
        this.qualifications = qualifications;
    }

    /**
     * @return sp_qualifications
     */
    public Integer getSpQualifications() {
        return spQualifications;
    }

    /**
     * @param spQualifications
     */
    public void setSpQualifications(Integer spQualifications) {
        this.spQualifications = spQualifications;
    }

    /**
     * @return cateid_sp_qualifications
     */
    public Integer getCateidSpQualifications() {
        return cateidSpQualifications;
    }

    /**
     * @param cateidSpQualifications
     */
    public void setCateidSpQualifications(Integer cateidSpQualifications) {
        this.cateidSpQualifications = cateidSpQualifications;
    }

    /**
     * @return status_sp_qualifications
     */
    public Integer getStatusSpQualifications() {
        return statusSpQualifications;
    }

    /**
     * @param statusSpQualifications
     */
    public void setStatusSpQualifications(Integer statusSpQualifications) {
        this.statusSpQualifications = statusSpQualifications;
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
     * @return hascoord
     */
    public Integer getHascoord() {
        return hascoord;
    }

    /**
     * @param hascoord
     */
    public void setHascoord(Integer hascoord) {
        this.hascoord = hascoord;
    }

    /**
     * @return product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product
     */
    public void setProduct(String product) {
        this.product = product;
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
     * @return c2
     */
    public String getC2() {
        return c2;
    }

    /**
     * @param c2
     */
    public void setC2(String c2) {
        this.c2 = c2;
    }

    /**
     * @return c3
     */
    public String getC3() {
        return c3;
    }

    /**
     * @param c3
     */
    public void setC3(String c3) {
        this.c3 = c3;
    }

    /**
     * @return c4
     */
    public String getC4() {
        return c4;
    }

    /**
     * @param c4
     */
    public void setC4(String c4) {
        this.c4 = c4;
    }
}