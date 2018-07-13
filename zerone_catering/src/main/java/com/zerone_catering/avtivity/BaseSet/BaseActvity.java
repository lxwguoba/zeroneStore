package com.zerone_catering.avtivity.BaseSet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;

/**
 * Created by on 2018/6/14 0014 09 38.
 * Author  LiuXingWen
 */

public class BaseActvity extends AppCompatActivity {
    private MyApplication baseApp;
    private BaseActvity oContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        //去掉标题栏8be7b2
//        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#8be7b2"));
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#fedc42"));
        if (baseApp == null) {
            // 得到Application对象
            baseApp = (MyApplication) getApplication();
        }
        // 把当前的上下文对象赋值给BaseActivity
        oContext = this;
        // 调用添加方法
        addActivity();
        //权限的设置
        initPermission();
    }

    /**
     * 添加Activity方法
     */
    public void addActivity() {
        //调用myApplication的添加Activity方法
        baseApp.addActivity(oContext);
    }

    /**
     * 销毁当个Activity方法
     */
    public void removeActivity() {
        baseApp.removeActivity(oContext);// 调用myApplication的销毁单个Activity方法
    }

    /**
     * 销毁所有Activity方法
     */
    public void removeALLActivity() {
        // 调用myApplication的销毁所有Activity方法
        baseApp.removeALLActivity();
    }

    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };
        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

}
