package com.zerone_catering.utils;

import android.content.Context;

import com.zerone_catering.DB.impl.UserInfoImpl;
import com.zerone_catering.domain.UserInfo;

/**
 * Created by on 2018/6/27 0027 15 37.
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
