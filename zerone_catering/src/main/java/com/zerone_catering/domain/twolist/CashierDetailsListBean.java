package com.zerone_catering.domain.twolist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/6/29 0029 15 53.
 * Author  LiuXingWen
 */

public class CashierDetailsListBean implements Serializable {

    /**
     * ordersn : XC20180628034152_14_100019
     * goodsdata : [{"id":104,"goods_id":9,"title":"纯牛奶","thumb":"http://develop2.01nnt.com/public/thumb.png","total":4,"price":"5.00"}]
     */

    private String ordersn;
    private List<GoodsdataBean> goodsdata;

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public List<GoodsdataBean> getGoodsdata() {
        return goodsdata;
    }

    public void setGoodsdata(List<GoodsdataBean> goodsdata) {
        this.goodsdata = goodsdata;
    }

    public static class GoodsdataBean {
        /**
         * id : 104
         * goods_id : 9
         * title : 纯牛奶
         * thumb : http://develop2.01nnt.com/public/thumb.png
         * total : 4
         * price : 5.00
         */

        private int id;
        private int goods_id;
        private String title;
        private String thumb;
        private int total;
        private String price;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
}
