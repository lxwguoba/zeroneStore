package com.zerone.store.shopingtimetest.Bean.refresh;

import java.io.Serializable;

/**
 * Created by on 2018/4/21 0021 14 07.
 * Author  LiuXingWen
 * 用来发送广播刷新商品列表的数据用，清空购物车等等。
 */

public class RefreshBean implements Serializable {
    //自定义刷新的名称。
    private String refreshName;
    //刷新的转态值 用来区分 下一个动作。
    private int refreshCode;

    /**
     * @param refreshName 自定义刷新的名称。
     * @param refreshCode 刷新的转态值 用来区分 下一个动作。
     */
    public RefreshBean(String refreshName, int refreshCode) {
        this.refreshName = refreshName;
        this.refreshCode = refreshCode;
    }

    public String getRefreshName() {
        return refreshName;
    }

    public void setRefreshName(String refreshName) {
        this.refreshName = refreshName;
    }

    public int getRefreshCode() {
        return refreshCode;
    }

    public void setRefreshCode(int refreshCode) {
        this.refreshCode = refreshCode;
    }
}
