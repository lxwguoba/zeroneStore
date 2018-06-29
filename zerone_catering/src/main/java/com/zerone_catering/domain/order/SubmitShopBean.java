package com.zerone_catering.domain.order;

import java.io.Serializable;

/**
 * Created by on 2018/4/8 0008 10 21.
 * Author  LiuXingWen
 */

public class SubmitShopBean implements Serializable {


    /**
     * goods_id : 3
     * goods_name : 炸酱面
     * goods_thumb : uploads/catering/20180626020931505.jpg
     * goods_price : 10.05
     * num : 2
     */

    private String goods_id;
    private String goods_name;
    private String goods_thumb;
    private String goods_price;
    private int num;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "SubmitShopBean{" +
                "goods_id='" + goods_id + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", goods_thumb='" + goods_thumb + '\'' +
                ", goods_price='" + goods_price + '\'' +
                ", num=" + num +
                '}';
    }
}
