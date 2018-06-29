package com.zerone_catering.Interface;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by on 2018/6/1 0001 15 16.
 * Author  LiuXingWen
 */

public interface OkHttpCallback {
    /**
     * 响应成功
     */
    void onSuccess(JSONObject oriData);


    /**
     * 响应失败
     */
    void onFailure(IOException e);
}
