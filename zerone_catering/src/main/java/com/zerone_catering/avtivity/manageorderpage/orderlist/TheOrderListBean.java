package com.zerone_catering.avtivity.manageorderpage.orderlist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/7/2 0002 18 02.
 * Author  LiuXingWen
 */

public class TheOrderListBean implements Serializable {


    /**
     * status : 1
     * msg : 订单列表查询成功
     * data : {"order_list":[{"id":8,"ordersn":"XC20180702032904_14_100008","order_price":"328717.25","status":"0","created_at":"1530516544"},{"id":7,"ordersn":"XC20180702032522_14_100007","order_price":"21865.00","status":"1","created_at":"1530516322"}],"price_sum":3609891.45,"order_num":8}
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
         * order_list : [{"id":8,"ordersn":"XC20180702032904_14_100008","order_price":"328717.25","status":"0","created_at":"1530516544"},{"id":7,"ordersn":"XC20180702032522_14_100007","order_price":"21865.00","status":"1","created_at":"1530516322"}]
         * price_sum : 3609891.45
         * order_num : 8
         */

        private String price_sum;
        private int order_num;
        private List<OrderListBean> order_list;

        public String getPrice_sum() {
            return price_sum;
        }

        public void setPrice_sum(String price_sum) {
            this.price_sum = price_sum;
        }

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }

        public List<OrderListBean> getOrder_list() {
            return order_list;
        }

        public void setOrder_list(List<OrderListBean> order_list) {
            this.order_list = order_list;
        }

        public static class OrderListBean implements Serializable {

            /**
             * id : 1
             * ordersn : XC20180702054249_14_100001
             * order_price : 484790.00
             * status : 1
             * discount_price : 484790.00
             * payment_price : 484790.00
             * created_at : 1530524569
             */

            private int id;
            private String ordersn;
            private String order_price;
            private String status;
            private String discount_price;
            private String payment_price;
            private String created_at;

            private String table_name;
            private String room_name;


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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
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

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }
        }
    }
}
