package com.zerone_catering.domain;

import java.io.Serializable;

/**
 * Created by on 2018/6/30 0030 10 20.
 * Author  LiuXingWen
 * 通用测试类
 */

public class NormalBean implements Serializable {

    /**
     * status : 1
     * msg : 提交订单成功
     * data : {"order_id":5}
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
        return "NormalBean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean implements Serializable {
        /**
         * order_id : 5
         */

        private int order_id;

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "order_id=" + order_id +
                    '}';
        }
    }
}
