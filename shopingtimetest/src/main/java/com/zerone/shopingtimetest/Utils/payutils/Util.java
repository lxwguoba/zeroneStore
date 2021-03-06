package com.zerone.shopingtimetest.Utils.payutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * @author by lj on 2017/5/15.
 */
public class Util {
    public static boolean isIntentExisting(Intent intent, Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.size() > 0;
    }
}
