package com.zerone_catering.domain.payorderlistbean;

import java.io.Serializable;

/**
 * Created by on 2018/6/28 0028 17 01.
 * Author  LiuXingWen
 * cashier：是收银
 */

public class OrderCashierListBean implements Serializable {


    /**
     * id : 53
     * ordersn : XC20180628034152_14_100019
     * order_price : 807986.00
     * status : 0
     * created_at : 1530171712
     * type : 1为已查看，0为未查看
     */
    private int id;

    private String ordersn;

    private String order_price;

    private String status;

    private String created_at;

    private int type;
    private boolean ochecklean;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOchecklean() {
        return ochecklean;
    }

    public void setOchecklean(boolean ochecklean) {
        this.ochecklean = ochecklean;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
