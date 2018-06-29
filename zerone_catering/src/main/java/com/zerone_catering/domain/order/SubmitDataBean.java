package com.zerone_catering.domain.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/4/8 0008 10 43.
 * Author  LiuXingWen
 */

public class SubmitDataBean implements Serializable {
    private List<SubmitShopBean> goods_list;

    public SubmitDataBean(List<SubmitShopBean> goods_list) {
        this.goods_list = goods_list;
    }

    public List<SubmitShopBean> getData() {
        return goods_list;
    }

    public void setData(List<SubmitShopBean> goods_list) {
        this.goods_list = goods_list;
    }

}
