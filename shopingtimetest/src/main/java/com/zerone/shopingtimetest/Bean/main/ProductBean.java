package com.zerone.shopingtimetest.Bean.main;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/5/10 0010 10 31.
 * Author  LiuXingWen
 */

public class ProductBean implements Serializable {

    /**
     * id : 15
     * name : 西瓜奶茶
     * category_id : 4
     * details : 西瓜奶茶
     * price : 20.00
     * stock : 553
     * category_name : 奶茶01
     * thumb : [{"thumb":"uploads/simple/20180504030852200.jpg"}]
     */

    private int id;
    private String name;
    private int category_id;
    //所属分类的位置
    private int category_pos;
    private String details;
    private String price;
    private int stock;
    private String category_name;
    private List<ThumbBean> thumb;
    //商品数量
    private int productCount;


    public int getCategory_pos() {
        return category_pos;
    }

    public void setCategory_pos(int category_pos) {
        this.category_pos = category_pos;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<ThumbBean> getThumb() {
        return thumb;
    }

    public void setThumb(List<ThumbBean> thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "ProductBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category_id=" + category_id +
                ", category_pos=" + category_pos +
                ", details='" + details + '\'' +
                ", price='" + price + '\'' +
                ", stock=" + stock +
                ", category_name='" + category_name + '\'' +
                ", thumb=" + thumb +
                ", productCount=" + productCount +
                '}';
    }

    public static class ThumbBean {
        /**
         * thumb : uploads/simple/20180504030852200.jpg
         */

        private String thumb;

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
    }
}
