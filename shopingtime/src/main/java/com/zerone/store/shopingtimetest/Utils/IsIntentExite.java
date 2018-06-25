package com.zerone.store.shopingtimetest.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/6/23 0023 10 27.
 * Author  LiuXingWen
 */

public class IsIntentExite implements Serializable {

    public static boolean isIntentExisting(Intent intent, Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.size() > 0;
    }
}
