package com.zerone_catering.domain.order;

import java.io.Serializable;

/**
 * Created by on 2018/4/3 0003 10 59.
 * Author  LiuXingWen
 * 这个是订单详情中的商品列表展示数据
 */

public class CheckCancelGoodsBean implements Serializable {

    //商品ID
    private String goods_id;
    //商品标题
    private String goods_title;
    //商品图片
    private String goods_thumb;
    //商品描述
    private String goods_details;
    //总数
    private String goods_total;
    //价格
    private String goods_price;
    //这个商品是否选中删除
    private Boolean goods_ckeck;
    //商品是否移除了 1以为移除 0为没有移除
    private int goodsGone;

    public CheckCancelGoodsBean() {

    }

    public int getGoodsGone() {
        return goodsGone;
    }

    public void setGoodsGone(int goodsGone) {
        this.goodsGone = goodsGone;
    }

    public Boolean getGoods_ckeck() {
        return goods_ckeck;
    }

    public void setGoods_ckeck(Boolean goods_ckeck) {
        this.goods_ckeck = goods_ckeck;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public String getGoods_details() {
        return goods_details;
    }

    public void setGoods_details(String goods_details) {
        this.goods_details = goods_details;
    }

    public String getGoods_total() {
        return goods_total;
    }

    public void setGoods_total(String goods_total) {
        this.goods_total = goods_total;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    @Override
    public String toString() {
        return "CheckCancelGoodsBean{" +
                "goods_id='" + goods_id + '\'' +
                ", goods_title='" + goods_title + '\'' +
                ", goods_thumb='" + goods_thumb + '\'' +
                ", goods_details='" + goods_details + '\'' +
                ", goods_total='" + goods_total + '\'' +
                ", goods_price='" + goods_price + '\'' +
                ", goods_ckeck=" + goods_ckeck +
                '}';
    }
}
