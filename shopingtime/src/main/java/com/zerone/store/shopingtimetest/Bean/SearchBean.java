package com.zerone.store.shopingtimetest.Bean;

import com.zerone.store.shopingtimetest.Bean.shoplistbean.ShopMessageBean;

import java.io.Serializable;

/**
 * Created by on 2018/5/25 0025 16 36.
 * Author  LiuXingWen
 */

public class SearchBean implements Serializable {
    private int code;
    private ShopMessageBean shopMessageBean;

    public SearchBean(int code, ShopMessageBean shopMessageBean) {
        this.code = code;
        this.shopMessageBean = shopMessageBean;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ShopMessageBean getShopMessageBean() {
        return shopMessageBean;
    }

    public void setShopMessageBean(ShopMessageBean shopMessageBean) {
        this.shopMessageBean = shopMessageBean;
    }
}
