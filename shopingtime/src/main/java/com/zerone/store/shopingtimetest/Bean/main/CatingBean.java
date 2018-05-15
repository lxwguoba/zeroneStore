package com.zerone.store.shopingtimetest.Bean.main;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/5/10 0010 10 25.
 * Author  LiuXingWen
 */

public class CatingBean implements Serializable {


    /**
     * id : 5
     * name : 水果奶茶
     * displayorder : 0
     */

    private int id;
    private String name;
    private int displayorder;
    private Map<String, List<ProductBean>> map;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(int displayorder) {
        this.displayorder = displayorder;
    }

    public Map<String, List<ProductBean>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<ProductBean>> map) {
        this.map = map;
    }


    @Override
    public String toString() {
        return "CatingBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayorder=" + displayorder +
                ", map=" + map +
                '}';
    }
}
