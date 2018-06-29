package com.zerone_catering.domain.tablefinal.cashiertable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/6/28 0028 15 03.
 * Author  LiuXingWen
 */

public class TableCatCashierFinal implements Serializable {
    /**
     * id : 2
     * room_name : 雅座
     * room_sort : 0
     */
    private int id;
    private String room_name;
    private int room_sort;
    /**
     * 分类包含了多少张桌子的map集合
     */
    private Map<String, List<TableListInfoCashierFinal>> map;


    public Map<String, List<TableListInfoCashierFinal>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<TableListInfoCashierFinal>> map) {
        this.map = map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getRoom_sort() {
        return room_sort;
    }

    public void setRoom_sort(int room_sort) {
        this.room_sort = room_sort;
    }

    @Override
    public String toString() {
        return "TableCatCashierFinal{" +
                "id=" + id +
                ", room_name='" + room_name + '\'' +
                ", room_sort=" + room_sort +
                ", map=" + map +
                '}';
    }
}
