package com.zerone.store.shopingtimetest.Utils;

import android.content.Context;
import android.os.Handler;

import com.zerone.store.shopingtimetest.Base64AndMD5.CreateToken;
import com.zerone.store.shopingtimetest.Bean.UserInfo;
import com.zerone.store.shopingtimetest.Contants.IpConfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by on 2018/5/22 0022 15 45.
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
