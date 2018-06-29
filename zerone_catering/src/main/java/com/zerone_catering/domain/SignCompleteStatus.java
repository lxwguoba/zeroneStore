package com.zerone_catering.domain;

/**
 * Created by on 2018/6/23 0023 10 37.
 * Author  LiuXingWen
 * 用于签到时发送广播时使用
 */

public class SignCompleteStatus {
    private String msg;
    private int resultCode;

    public SignCompleteStatus(String msg, int resultCode) {
        this.msg = msg;
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
