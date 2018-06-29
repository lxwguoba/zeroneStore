package com.zerone_catering.domain.colse;

/**
 * Created by on 2018/6/28 0028 11 14.
 * Author  LiuXingWen
 */

public class CloseActivity {
    private String msg;
    private int code;

    public CloseActivity(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
