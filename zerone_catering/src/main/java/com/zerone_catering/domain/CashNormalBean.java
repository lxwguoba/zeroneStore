package com.zerone_catering.domain;

import java.io.Serializable;

/**
 * Created by on 2018/7/2 0002 14 37.
 * Author  LiuXingWen
 */

public class CashNormalBean implements Serializable {


    /**
     * status : 1
     * msg : 现金付款成功
     * data : {"order_id":"32","payment_price":791563}
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
         * order_id : 32
         * payment_price : 791563
         */

        private String order_id;
        private String payment_price;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getPayment_price() {
            return payment_price;
        }

        public void setPayment_price(String payment_price) {
            this.payment_price = payment_price;
        }
    }
}
