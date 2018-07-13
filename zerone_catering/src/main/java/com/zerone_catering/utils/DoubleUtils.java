package com.zerone_catering.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by on 2018/4/10 0010 11 14.
 * Author  LiuXingWen
 */

public class DoubleUtils {

    /**
     * 把double保留两位小数并设置为String
     *
     * @param money
     * @return
     */
    public static String setDouble(double money) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(money);
    }

    /**
     * 也是把double保留两位小数 并
     *
     * @param money
     * @return
     */
    public static String setSSWRDouble(double money) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 2是显示的小数点后的显示的最多位,显示的最后位是舍入的
        nf.setMaximumFractionDigits(2);
        return nf.format(money);
    }

    /**
     * 把钱的后面的小数点保留2位。多余的删除
     *
     * @param price
     * @return
     */
    public static String subMoney(String price) {
        String behind = "00";
        String[] split = price.split("\\.");
        if (split.length > 1) {
            if (split[1].length() > 3) {
                behind = split[1].substring(0, 2);
            } else {
                behind = split[1];
            }
        }
        return split[0] + "." + behind;
    }
}
