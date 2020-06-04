package com.company.project.model;

import javax.persistence.*;

@Table(name = "ERP_CustomValues")
public class ErpCustomvalues {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FieldsID")
    private Integer fieldsid;

    @Column(name = "OrderID")
    private Integer orderid;

    @Column(name = "FValue")
    private String fvalue;

    @Column(name = "caigouQClist")
    private Integer caigouqclist;

    /**
     * @return ID
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
     * @return FieldsID
     */
    public Integer getFieldsid() {
        return fieldsid;
    }

    /**
     * @param fieldsid
     */
    public void setFieldsid(Integer fieldsid) {
        this.fieldsid = fieldsid;
    }

    /**
     * @return OrderID
     */
    public Integer getOrderid() {
        return orderid;
    }

    /**
     * @param orderid
     */
    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    /**
     * @return FValue
     */
    public String getFvalue() {
        return fvalue;
    }

    /**
     * @param fvalue
     */
    public void setFvalue(String fvalue) {
        this.fvalue = fvalue;
    }

    /**
     * @return caigouQClist
     */
    public Integer getCaigouqclist() {
        return caigouqclist;
    }

    /**
     * @param caigouqclist
     */
    public void setCaigouqclist(Integer caigouqclist) {
        this.caigouqclist = caigouqclist;
    }
}