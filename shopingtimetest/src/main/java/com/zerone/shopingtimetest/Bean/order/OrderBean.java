package com.zerone.shopingtimetest.Bean.order;

import java.io.Serializable;

/**
 * Created by on 2018/4/2 0002 18 36.
 * Author  LiuXingWen
 * 订单实体类
 */

public class OrderBean implements Serializable {
    //订单ID
    private String id;
    //订单编号
    private String ordersn;
    //订单价格
    private String order_price;
    //订单状态
    private String status;
    //created_at
    private String created_at;

    //折扣价
    private String discount_price;
    //实收
    private String payment_price;


    public OrderBean() {
    }


    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getPayment_price() {
        return payment_price;
    }

    public void setPayment_price(String payment_price) {
        this.payment_price = payment_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
