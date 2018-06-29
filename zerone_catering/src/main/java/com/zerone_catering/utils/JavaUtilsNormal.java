package com.zerone_catering.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by on 2018/6/29 0029 13 48.
 * Author  LiuXingWen
 */

public class JavaUtilsNormal {

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 将毫秒值转化为时间（PHP服务器给的时间搓）
     *
     * @param longtime
     * @return
     */
    public static String getTime(Long longtime) {
        long l = longtime * 1000;
        Date nowTime = new Date(l);
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;

    }
}
