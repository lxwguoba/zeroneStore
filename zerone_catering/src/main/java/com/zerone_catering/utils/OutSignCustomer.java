package com.zerone_catering.utils;

import android.content.Context;
import android.os.Handler;

import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.domain.UserInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by on 2018/6/21 0021 16 35.
 * Author  LiuXingWen
 */

public class OutSignCustomer implements Serializable {
    /**
     * @param userInfo   登录账户的基本信息
     * @param context    上下文
     * @param terminalId pos终端号
     * @param handler    handler机制
     * @param ResponseId 响应ID
     */
    public static void signOut(UserInfo userInfo, Context context, String terminalId, Handler handler, int ResponseId) {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> codeMap = new HashMap<>();
        codeMap.put("device_num", terminalId);
        NetUtils.netWorkByMethodPost(context, codeMap, IpConfig.URL_SINGOUT, handler, 5);
    }

}
