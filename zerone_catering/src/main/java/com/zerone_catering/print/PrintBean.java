package com.zerone_catering.print;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/1/31 0031 17 42.
 * Author  LiuXingWen
 * 打印封装实体类
 */

public class PrintBean implements Serializable {
    //订单编号
    private String ordersn;
    //下单时间
    private String createTime;
    //订单总金额
    private String pmoney;
    //订单状态
    private String orderTuype;

    //实付金额
    private String payment_price;
    //折扣
    private String discount;
    //备注
    private String remark;

    private String roomAndTable;
    private List<PrintItem> list;

    public String getRoomAndTable() {
        return roomAndTable;
    }

    public void setRoomAndTable(String roomAndTable) {
        this.roomAndTable = roomAndTable;
    }

    public String getPayment_price() {
        return payment_price;
    }

    public void setPayment_price(String payment_price) {
        this.payment_price = payment_price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPmoney() {
        return pmoney;
    }

    public void setPmoney(String pmoney) {
        this.pmoney = pmoney;
    }

    public String getOrderTuype() {
        return orderTuype;
    }

    public void setOrderTuype(String orderTuype) {
        this.orderTuype = orderTuype;
    }

    public List<PrintItem> getList() {
        return list;
    }

    public void setList(List<PrintItem> list) {
        this.list = list;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
