package com.zerone_catering.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018-07-03 14 53.
 * Author  LiuXingWen
 */

public class ProductBean implements Serializable {


    /**
     * status : 1
     * msg : 获取商品成功
     * data : {"goods":{"id":1,"name":"1","details":"1","price":"1.00","thumb":[{"thumb":"public/thumb.png"}]}}
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
         * goods : {"id":1,"name":"1","details":"1","price":"1.00","thumb":[{"thumb":"public/thumb.png"}]}
         */

        private GoodsBean goods;

        public GoodsBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean goods) {
            this.goods = goods;
        }

        public static class GoodsBean implements Serializable {
            /**
             * id : 1
             * name : 1
             * details : 1
             * price : 1.00
             * thumb : [{"thumb":"public/thumb.png"}]
             */

            private int id;
            private String name;
            private String details;
            private String price;
            private List<ThumbBean> thumb;

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

            public List<ThumbBean> getThumb() {
                return thumb;
            }

            public void setThumb(List<ThumbBean> thumb) {
                this.thumb = thumb;
            }

            public static class ThumbBean implements Serializable {
                /**
                 * thumb : public/thumb.png
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
    }
}
