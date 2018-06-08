package com.zerone.store.shopingtimetest.handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 * Created by on 2018/5/31 0031 10 14.
 * Author  LiuXingWen
 * 测试中
 */

public class OrderHandler extends Handler {
    private Activity activity;

    public OrderHandler(Activity activity) {
        this.activity = activity;
        initView();
    }

    private void initView() {

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
}
