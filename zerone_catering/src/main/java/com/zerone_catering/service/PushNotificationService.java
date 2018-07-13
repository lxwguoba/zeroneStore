package com.zerone_catering.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zerone_catering.avtivity.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by on 2018/6/20 0020 16 32.
 * Author  LiuXingWen
 */

public class PushNotificationService extends Service {
    private Timer timer;
    private TimerTask task;
    private int count;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final Intent intent = new Intent();
        intent.setAction(MainActivity.ACTION_UPDATEUI);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                intent.putExtra("code", 1);
                sendBroadcast(intent);
            }
        };
        timer.schedule(task, 1000, 10000);
    }

}
