package com.zerone_catering.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/6/30 0030 14 34.
 * Author  LiuXingWen
 */

public class ForThePaymentBean implements Serializable {
    /**
     * status : 1
     * msg : 订单详情查询成功
     * data : {"orderdata":{"id":18,"ordersn":"XC20180630022442_14_100010","order_price":"11.05","remarks":"这世界我来了 爱是你我","user_id":0,"nickname":"现场店员开单","status":"0","operator_id":14,"organization_id":14,"realname":"果粒橙","created_at":"1530339882","payment_company":null,"paytype":null,"discount_price":"9.83","payment_price":null,"discount":"8.90"},"ordergoods":[{"goods_id":3,"title":"炸酱面","thumb":"http://develop2.01nnt.com/uploads/catering/20180626020919862.jpg","total":1,"price":"10.05"},{"goods_id":1,"title":"1","thumb":"http://develop2.01nnt.com/public/thumb.png","total":1,"price":"1.00"}]}
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

    @Override
    public String toString() {
        return "ForThePaymentBean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean implements Serializable {
        /**
         * orderdata : {"id":18,"ordersn":"XC20180630022442_14_100010","order_price":"11.05","remarks":"这世界我来了 爱是你我","user_id":0,"nickname":"现场店员开单","status":"0","operator_id":14,"organization_id":14,"realname":"果粒橙","created_at":"1530339882","payment_company":null,"paytype":null,"discount_price":"9.83","payment_price":null,"discount":"8.90"}
         * ordergoods : [{"goods_id":3,"title":"炸酱面","thumb":"http://develop2.01nnt.com/uploads/catering/20180626020919862.jpg","total":1,"price":"10.05"},{"goods_id":1,"title":"1","thumb":"http://develop2.01nnt.com/public/thumb.png","total":1,"price":"1.00"}]
         */


        private OrderdataBean orderdata;
        private List<OrdergoodsBean> ordergoods;

        @Override
        public String toString() {
            return "DataBean{" +
                    "orderdata=" + orderdata +
                    ", ordergoods=" + ordergoods +
                    '}';
        }

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

        public static class OrderdataBean {
            /**
             * id : 18
             * ordersn : XC20180630022442_14_100010
             * order_price : 11.05
             * remarks : 这世界我来了 爱是你我
             * user_id : 0
             * nickname : 现场店员开单
             * status : 0
             * operator_id : 14
             * organization_id : 14
             * realname : 果粒橙
             * created_at : 1530339882
             * payment_company : null
             * paytype : null
             * discount_price : 9.83
             * payment_price : null
             * discount : 8.90
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
            private Object paytype;
            private String discount_price;
            private Object payment_price;
            private String discount;
            private String table_name;
            private String room_name;

            @Override
            public String toString() {
                return "OrderdataBean{" +
                        "id=" + id +
                        ", ordersn='" + ordersn + '\'' +
                        ", order_price='" + order_price + '\'' +
                        ", remarks='" + remarks + '\'' +
                        ", user_id=" + user_id +
                        ", nickname='" + nickname + '\'' +
                        ", status='" + status + '\'' +
                        ", operator_id=" + operator_id +
                        ", organization_id=" + organization_id +
                        ", realname='" + realname + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", payment_company=" + payment_company +
                        ", paytype=" + paytype +
                        ", discount_price='" + discount_price + '\'' +
                        ", payment_price=" + payment_price +
                        ", discount='" + discount + '\'' +
                        '}';
            }

            public String getTable_name() {
                return table_name;
            }

            public void setTable_name(String table_name) {
                this.table_name = table_name;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

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

            public Object getPaytype() {
                return paytype;
            }

            public void setPaytype(Object paytype) {
                this.paytype = paytype;
            }

            public String getDiscount_price() {
                return discount_price;
            }

            public void setDiscount_price(String discount_price) {
                this.discount_price = discount_price;
            }

            public Object getPayment_price() {
                return payment_price;
            }

            public void setPayment_price(Object payment_price) {
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
             * goods_id : 3
             * title : 炸酱面
             * thumb : http://develop2.01nnt.com/uploads/catering/20180626020919862.jpg
             * total : 1
             * price : 10.05
             */


            private int goods_id;
            private String title;
            private String thumb;
            private int total;
            private String price;

            @Override
            public String toString() {
                return "OrdergoodsBean{" +
                        "goods_id=" + goods_id +
                        ", title='" + title + '\'' +
                        ", thumb='" + thumb + '\'' +
                        ", total=" + total +
                        ", price='" + price + '\'' +
                        '}';
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
}
