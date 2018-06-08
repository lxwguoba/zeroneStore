package com.zerone.store.shopingtimetest.Utils;

import android.content.Context;

import com.zerone.store.shopingtimetest.Bean.UserInfo;
import com.zerone.store.shopingtimetest.DB.impl.UserInfoImpl;

/**
 * Created by on 2018/5/31 0031 11 26.
 * Author  LiuXingWen
 */

public class GetUserInfo {
    /**
     * 获取用户信息
     */
    public static UserInfo initGetUserInfo(Context context) {
        UserInfoImpl userInfoImpl = new UserInfoImpl(context);
        try {
            UserInfo userInfo = userInfoImpl.getUserInfo("10");
            return userInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
