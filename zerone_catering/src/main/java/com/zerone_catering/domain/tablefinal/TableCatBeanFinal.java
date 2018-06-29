package com.zerone_catering.domain.tablefinal;

import java.util.List;

/**
 * Created by on 2018/6/27 0027 16 01.
 * Author  LiuXingWen
 */

public class TableCatBeanFinal {
    /**
     * id : 1
     * room_name : 大厅1
     * room_sort : 1
     */
    private int id;
    private String room_name;
    private int room_sort;
    private List<TableInfoBeanFianl> tableList;

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

    public List<TableInfoBeanFianl> getTableList() {
        return tableList;
    }

    public void setTableList(List<TableInfoBeanFianl> tableList) {
        this.tableList = tableList;
    }
}
