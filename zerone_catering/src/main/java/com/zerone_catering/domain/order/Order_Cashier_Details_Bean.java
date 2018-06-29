package com.zerone_catering.domain.order;

import java.io.Serializable;

/**
 * Created by on 2018/6/29 0029 09 15.
 * Author  LiuXingWen
 * 这个是收银里的订单详情
 */

public class Order_Cashier_Details_Bean implements Serializable {


    /**
     * goods_id : 1
     * title : 1
     * thumb : http://develop2.01nnt.com/public/thumb.png
     * total : 4
     * price : 1.00
     */

    private int goods_id;
    private String title;
    private String thumb;
    private int total;
    private String price;
    //这个商品是否选中删除
    private Boolean goods_ckeck;

    public Boolean getGoods_ckeck() {
        return goods_ckeck;
    }

    public void setGoods_ckeck(Boolean goods_ckeck) {
        this.goods_ckeck = goods_ckeck;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
