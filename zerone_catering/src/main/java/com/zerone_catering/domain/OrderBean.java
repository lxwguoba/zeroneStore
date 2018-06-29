package com.zerone_catering.domain;

/**
 * Created by on 2018/6/14 0014 17 35.
 * Author  LiuXingWen
 */

public class OrderBean {
    private boolean ochecklean;
    private String osn;
    private String otime;
    private String omoney;


    /**
     * @param ochecklean 订单是否被选中
     * @param osn        订单编号
     * @param otime      下单时间
     * @param omoney     订单总额
     */
    public OrderBean(boolean ochecklean, String osn, String otime, String omoney) {
        this.ochecklean = ochecklean;
        this.osn = osn;
        this.otime = otime;
        this.omoney = omoney;
    }

    public OrderBean() {
    }

    public String getOtime() {
        return otime;
    }

    public void setOtime(String otime) {
        this.otime = otime;
    }

    public String getOmoney() {
        return omoney;
    }

    public void setOmoney(String omoney) {
        this.omoney = omoney;
    }

    public boolean isOchecklean() {
        return ochecklean;
    }

    public void setOchecklean(boolean ochecklean) {
        this.ochecklean = ochecklean;
    }

    public String getOsn() {
        return osn;
    }

    public void setOsn(String osn) {
        this.osn = osn;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "ochecklean=" + ochecklean +
                ", osn='" + osn + '\'' +
                '}';
    }
}
