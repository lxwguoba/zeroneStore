package com.zerone_catering.avtivity.openorderpage;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.widget.NumberPicker;

import com.zerone_catering.R;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by on 2018/6/1 0001 09 50.
 * Author  LiuXingWen
 * 这个页面的方法整合
 */

public class MakeSureMethod {


    /**
     * @param numberPicker 要设置的numberpicker的组件
     * @param context      上下文
     */
    public static void setNumberPickerDividerColor(NumberPicker numberPicker, Context context) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.green)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * @param timer
     * @param task
     */
    public static void stopTimer(Timer timer, TimerTask task, Dialog dialog) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
