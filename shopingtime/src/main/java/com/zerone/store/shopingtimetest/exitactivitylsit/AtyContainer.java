package com.zerone.store.shopingtimetest.exitactivitylsit;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2018/5/29 0029 11 12.
 * Author  LiuXingWen
 */

public class AtyContainer {
    private static AtyContainer instance = new AtyContainer();
    private static List<Activity> activityStack = new ArrayList<Activity>();

    private AtyContainer() {
    }

    public static AtyContainer getInstance() {
        return instance;
    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
}
