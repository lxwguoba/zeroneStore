package com.zerone_catering.domain;

import java.io.Serializable;

/**
 * Created by on 2018/6/14 0014 16 07.
 * Author  LiuXingWen
 */
public class TalbeBean implements Serializable {
    private String table_name;
    private String table_id;
    private String onum;
    private String onopaynumber;
    private String cid;

    public TalbeBean() {
    }

    /**
     * @param table_name
     * @param table_id
     * @param onum         订单数
     * @param onopaynumber 未付款订单数
     */
    public TalbeBean(String cid, String table_name, String table_id, String onum, String onopaynumber) {
        this.cid = cid;
        this.table_name = table_name;
        this.table_id = table_id;
        this.onum = onum;
        this.onopaynumber = onopaynumber;
    }

    public String getOnum() {
        return onum;
    }

    public void setOnum(String onum) {
        this.onum = onum;
    }

    public String getOnopaynumber() {
        return onopaynumber;
    }

    public void setOnopaynumber(String onopaynumber) {
        this.onopaynumber = onopaynumber;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }
}
