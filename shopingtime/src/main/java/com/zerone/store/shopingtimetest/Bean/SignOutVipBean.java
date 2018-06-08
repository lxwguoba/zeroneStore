package com.zerone.store.shopingtimetest.Bean;

import java.io.Serializable;

/**
 * Created by on 2018/5/29 0029 11 45.
 * Author  LiuXingWen
 */

public class SignOutVipBean implements Serializable {
    private String name;
    private int code;

    public SignOutVipBean(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
