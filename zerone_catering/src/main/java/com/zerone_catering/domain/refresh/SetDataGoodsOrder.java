package com.zerone_catering.domain.refresh;


import com.zerone_catering.domain.shoplistbean.ShopBean;

import java.io.Serializable;

/**
 * Created by on 2018/4/24 0024 09 59.
 * Author  LiuXingWen
 */

public class SetDataGoodsOrder implements Serializable {
    private ShopBean shopMessageBean;

    private int code;

    public SetDataGoodsOrder(ShopBean shopMessageBean, int code) {
        this.shopMessageBean = shopMessageBean;
        this.code = code;
    }

    public ShopBean getShopBean() {
        return shopMessageBean;
    }

    public void setShopBean(ShopBean shopMessageBean) {
        this.shopMessageBean = shopMessageBean;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
