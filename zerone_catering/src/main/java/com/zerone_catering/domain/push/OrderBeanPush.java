package com.zerone_catering.domain.push;

import java.io.Serializable;

/**
 * Created by on 2018/6/20 0020 16 45.
 * Author  LiuXingWen
 */

public class OrderBeanPush implements Serializable {
    private Integer onum;
    private String oname;

    public OrderBeanPush(Integer onum, String oname) {
        this.onum = onum;
        this.oname = oname;
    }

    public Integer getOnum() {
        return onum;
    }

    public void setOnum(Integer onum) {
        this.onum = onum;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }
}
