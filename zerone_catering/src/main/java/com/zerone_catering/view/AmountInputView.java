package com.zerone_catering.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 输入金额键盘
 */
@SuppressLint("AppCompatCustomView")
public class AmountInputView extends TextView {

    //缓存输入的密码
    private StringBuilder inputSB;
    private String def = "0.00";

    public AmountInputView(Context context, AttributeSet attrs) {

        super(context, attrs);

        inputSB = new StringBuilder();
        inputSB.append(def);

        setText(def.toString());

    }

    /**
     * 输入一个字符
     *
     * @param text
     */
    public synchronized void addText(String text) {
        if (inputSB.length() >= 10) {
            return;
        }
        inputSB.append(text);
        String str = inputSB.toString();
        String cuttedStr = filter(str);
        inputSB.setLength(0);
        inputSB.append(cuttedStr);

        setText("￥" + inputSB.toString());

        invalidate();

    }

    @NonNull
    private String filter(String str) {

        String cutedStr = str;

        /* 删除字符串中的dot */
        for (int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);
            if ('.' == c) {
                cutedStr = str.substring(0, i) + str.substring(i + 1);
                break;
            }
        }

        /* 删除前面多余的0 */
        int NUM = cutedStr.length();
        int zeroIndex = -1;

        for (int i = 0; i < NUM - 2; i++) {
            char c = cutedStr.charAt(i);
            if (c != '0') {
                zeroIndex = i;
                break;
            } else if (i == NUM - 3) {
                zeroIndex = i;
                break;
            }
        }

        if (zeroIndex != -1) {
            cutedStr = cutedStr.substring(zeroIndex);
        }

        /* 不足3位补0 */
        if (cutedStr.length() < 3) {
            cutedStr = "0" + cutedStr;
        }

        /* 加上dot，以显示小数点后两位 */
        cutedStr = cutedStr.substring(0, cutedStr.length() - 2) + "." + cutedStr.substring(cutedStr.length() - 2);

        return cutedStr;

    }

    /**
     * 删除最后一个
     */
    public void delLast() {

        if ((inputSB.toString()).equals(def)) return;

        inputSB.deleteCharAt(inputSB.length() - 1);
        String temp = inputSB.toString();
        temp = filter(temp);
        inputSB.setLength(0);
        inputSB.append(temp);

        setText("￥" + inputSB.toString());

    }

    /**
     * 清空
     */
    public void clean() {

        inputSB.setLength(0);
        inputSB.append(def);
        setText("￥" + String.valueOf(inputSB));

    }

    public CharSequence getAmountText() {

        return inputSB.toString();

    }

}
