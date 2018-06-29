package com.zerone_catering.domain.tablefinal;

/**
 * Created by on 2018/6/27 0027 16 05.
 * Author  LiuXingWen
 */

public class TableInfoBeanFianl {


    /**
     * id : 5
     * table_name : 桌子一号
     * room_id : 1
     * table_sort : 0
     * room_name : 大厅1
     */

    private int id;
    private String table_name;
    private int room_id;
    private int table_sort;
    private String room_name;

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

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    @Override
    public String toString() {
        return "TableInfoBeanFianl{" +
                "id=" + id +
                ", table_name='" + table_name + '\'' +
                ", room_id=" + room_id +
                ", table_sort=" + table_sort +
                ", room_name='" + room_name + '\'' +
                '}';
    }
}
