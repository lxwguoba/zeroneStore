package com.zerone.store.shopingtimetest.Utils.thirty;

import java.math.BigInteger;

/**
 * Created by on 2018/5/2 * Author  LiuXingWen4 0024 12 18.
 */

public class Thirty36 {
    //十进制转换中把字符转换为数 这个是
    public static int changeDec(char ch) {
        int num = 0;
        if (ch >= 'A' && ch <= 'Z')
            num = ch - 'A' + 10;
        else if (ch >= 'a' && ch <= 'z')
            num = ch - 'a' + 36;
        else
            num = ch - '0';
        return num;
    }

    /**
     * @param input 需要转换的字符串
     * @param base  需要转换的进制
     * @return
     */
    public static String toDecimal(String input, int base) {
        BigInteger Bigtemp = BigInteger.ZERO, temp = BigInteger.ONE;
        int len = input.length();
        for (int i = len - 1; i >= 0; i--) {
            if (i != len - 1)
                temp = temp.multiply(BigInteger.valueOf(base));
            int num = changeDec(input.charAt(i));
            Bigtemp = Bigtemp.add(temp.multiply(BigInteger.valueOf(num)));
        }
        return Bigtemp + "";
    }

    //====================================================
//数字转换为字符
    public static char changToNum(BigInteger temp) {
        int n = temp.intValue();

        if (n >= 10 && n <= 35)
            return (char) (n - 10 + 'A');
        else if (n >= 36 && n <= 61)
            return (char) (n - 36 + 'a');
        else
            return (char) (n + '0');
    }
    //十进制转换为任意进制

    /**
     * @param Bigtemp 需要转换的10进制数
     * @param base    转换的位数
     * @return
     */
    public static String toAnyConversion(BigInteger Bigtemp, BigInteger base) {
        String ans = "";
        while (Bigtemp.compareTo(BigInteger.ZERO) != 0) {
            BigInteger temp = Bigtemp.mod(base);
            Bigtemp = Bigtemp.divide(base);
            char ch = changToNum(temp);
            ans = ch + ans;
        }
        return ans;
    }
}
