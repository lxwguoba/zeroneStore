package com.store.zerone.zeronestore.demo1;

import java.io.Serializable;

/**
 * Created by on 2018/3/16 0016 09 50.
 * Author  LiuXingWen
 */

public class TestGoods implements Serializable {
     private String   goodsName;

     private  String   goodsBarCode;

     private  String   goodsMoney;

     private   String  goodsNumber;

    public TestGoods() {
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsBarCode() {
        return goodsBarCode;
    }

    public void setGoodsBarCode(String goodsBarCode) {
        this.goodsBarCode = goodsBarCode;
    }

    public String getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(String goodsMoney) {
        this.goodsMoney = goodsMoney;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }



    @Override
    public String toString() {
        return "TestGoods{" +
                "goodsName='" + goodsName + '\'' +
                ", goodsBarCode='" + goodsBarCode + '\'' +
                ", goodsMoney='" + goodsMoney + '\'' +
                ", goodsNumber='" + goodsNumber + '\'' +
                '}';
    }


}
