package com.zerone_catering.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/7/2 0002 16 00.
 * Author  LiuXingWen
 */

public class PayOrderDetails implements Serializable {

    /**
     * status : 1
     * msg : 订单详情查询成功
     * data : {"orderdata":{"id":12,"ordersn":"XC20180702035702_14_100012","order_price":"349233.20","remarks":"特辣","user_id":4,"nickname":"武影","status":"1","operator_id":14,"organization_id":14,"realname":"顾客手机下单","created_at":"1530518222","payment_company":null,"paytype":"-1","discount_price":"342248.54","payment_price":"342248.54","discount":"9.80"},"ordergoods":[{"goods_id":1,"title":"1","thumb":"http://develop2.01nnt.com/public/thumb.png","total":3,"price":"1.00"},{"goods_id":4,"title":"劳力士","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"156125.00"},{"goods_id":5,"title":"商品一","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"5465.00"},{"goods_id":9,"title":"纯牛奶","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"5.00"},{"goods_id":1,"title":"1","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"1.00"},{"goods_id":4,"title":"劳力士","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"156125.00"},{"goods_id":1,"title":"1","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"1.00"},{"goods_id":4,"title":"劳力士","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"156125.00"},{"goods_id":5,"title":"商品一","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"5465.00"},{"goods_id":9,"title":"纯牛奶","thumb":"http://develop2.01nnt.com/public/thumb.png","total":1,"price":"5.00"},{"goods_id":2,"title":"2","thumb":"http://develop2.01nnt.com/public/thumb.png","total":3,"price":"2.00"},{"goods_id":6,"title":"商品二","thumb":"http://develop2.01nnt.com/public/thumb.png","total":4,"price":"6500.00"},{"goods_id":3,"title":"炸酱面","thumb":"http://develop2.01nnt.com/uploads/catering/20180626020919862.jpg","total":3,"price":"10.05"},{"goods_id":3,"title":"炸酱面","thumb":"http://develop2.01nnt.com/uploads/catering/20180626020919862.jpg","total":1,"price":"10.05"}]}
     */

    private String status;
    private String msg;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * orderdata : {"id":12,"ordersn":"XC20180702035702_14_100012","order_price":"349233.20","remarks":"特辣","user_id":4,"nickname":"武影","status":"1","operator_id":14,"organization_id":14,"realname":"顾客手机下单","created_at":"1530518222","payment_company":null,"paytype":"-1","discount_price":"342248.54","payment_price":"342248.54","discount":"9.80"}
         * ordergoods : [{"goods_id":1,"title":"1","thumb":"http://develop2.01nnt.com/public/thumb.png","total":3,"price":"1.00"},{"goods_id":4,"title":"劳力士","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"156125.00"},{"goods_id":5,"title":"商品一","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"5465.00"},{"goods_id":9,"title":"纯牛奶","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"5.00"},{"goods_id":1,"title":"1","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"1.00"},{"goods_id":4,"title":"劳力士","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"156125.00"},{"goods_id":1,"title":"1","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"1.00"},{"goods_id":4,"title":"劳力士","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"156125.00"},{"goods_id":5,"title":"商品一","thumb":"http://develop2.01nnt.com/public/thumb.png","total":2,"price":"5465.00"},{"goods_id":9,"title":"纯牛奶","thumb":"http://develop2.01nnt.com/public/thumb.png","total":1,"price":"5.00"},{"goods_id":2,"title":"2","thumb":"http://develop2.01nnt.com/public/thumb.png","total":3,"price":"2.00"},{"goods_id":6,"title":"商品二","thumb":"http://develop2.01nnt.com/public/thumb.png","total":4,"price":"6500.00"},{"goods_id":3,"title":"炸酱面","thumb":"http://develop2.01nnt.com/uploads/catering/20180626020919862.jpg","total":3,"price":"10.05"},{"goods_id":3,"title":"炸酱面","thumb":"http://develop2.01nnt.com/uploads/catering/20180626020919862.jpg","total":1,"price":"10.05"}]
         */

        private OrderdataBean orderdata;
        private List<OrdergoodsBean> ordergoods;

        public OrderdataBean getOrderdata() {
            return orderdata;
        }

        public void setOrderdata(OrderdataBean orderdata) {
            this.orderdata = orderdata;
        }

        public List<OrdergoodsBean> getOrdergoods() {
            return ordergoods;
        }

        public void setOrdergoods(List<OrdergoodsBean> ordergoods) {
            this.ordergoods = ordergoods;
        }

        public static class OrderdataBean implements Serializable {
            /**
             * id : 12
             * ordersn : XC20180702035702_14_100012
             * order_price : 349233.20
             * remarks : 特辣
             * user_id : 4
             * nickname : 武影
             * status : 1
             * operator_id : 14
             * organization_id : 14
             * realname : 顾客手机下单
             * created_at : 1530518222
             * payment_company : null
             * paytype : -1
             * discount_price : 342248.54
             * payment_price : 342248.54
             * discount : 9.80
             */

            private int id;
            private String ordersn;
            private String order_price;
            private String remarks;
            private int user_id;
            private String nickname;
            private String status;
            private int operator_id;
            private int organization_id;
            private String realname;
            private String created_at;
            private Object payment_company;
            private String paytype;
            private String discount_price;
            private String payment_price;
            private String discount;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrdersn() {
                return ordersn;
            }

            public void setOrdersn(String ordersn) {
                this.ordersn = ordersn;
            }

            public String getOrder_price() {
                return order_price;
            }

            public void setOrder_price(String order_price) {
                this.order_price = order_price;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getOperator_id() {
                return operator_id;
            }

            public void setOperator_id(int operator_id) {
                this.operator_id = operator_id;
            }

            public int getOrganization_id() {
                return organization_id;
            }

            public void setOrganization_id(int organization_id) {
                this.organization_id = organization_id;
            }

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public Object getPayment_company() {
                return payment_company;
            }

            public void setPayment_company(Object payment_company) {
                this.payment_company = payment_company;
            }

            public String getPaytype() {
                return paytype;
            }

            public void setPaytype(String paytype) {
                this.paytype = paytype;
            }

            public String getDiscount_price() {
                return discount_price;
            }

            public void setDiscount_price(String discount_price) {
                this.discount_price = discount_price;
            }

            public String getPayment_price() {
                return payment_price;
            }

            public void setPayment_price(String payment_price) {
                this.payment_price = payment_price;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }
        }

        public static class OrdergoodsBean implements Serializable {
            /**
             * goods_id : 1
             * title : 1
             * thumb : http://develop2.01nnt.com/public/thumb.png
             * total : 3
             * price : 1.00
             */

            private int goods_id;
            private String title;
            private String thumb;
            private int total;
            private String price;

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
}
