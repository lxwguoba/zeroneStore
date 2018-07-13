package com.zerone_catering.domain.tablefinal.cashiertable;

import java.io.Serializable;

/**
 * Created by on 2018/6/28 0028 15 10.
 * Author  LiuXingWen
 */

public class TableListInfoCashierFinal implements Serializable {
    /**
     * id : 5
     * table_name : 桌子一号
     * room_id : 1
     * table_sort : 0
     * order_num : 2
     * order_unpaid : 1
     *num : 桌子的新的订单
     */
    private int id;
    private String table_name;
    private int room_id;
    private int table_sort;
    private int order_num;
    private int order_unpaid;

    private int num;


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getTable_sort() {
        return table_sort;
    }

    public void setTable_sort(int table_sort) {
        this.table_sort = table_sort;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public int getOrder_unpaid() {
        return order_unpaid;
    }

    public void setOrder_unpaid(int order_unpaid) {
        this.order_unpaid = order_unpaid;
    }

    @Override
    public String toString() {
        return "TableListInfoCashierFinal{" +
                "id=" + id +
                ", table_name='" + table_name + '\'' +
                ", room_id=" + room_id +
                ", table_sort=" + table_sort +
                ", order_num=" + order_num +
                ", order_unpaid=" + order_unpaid +
                '}';
    }
}
