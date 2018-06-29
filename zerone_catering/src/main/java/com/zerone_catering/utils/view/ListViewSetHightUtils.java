package com.zerone_catering.utils.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by on 2018/6/27 0027 10 25.
 * Author  LiuXingWen
 * Listveiw的高度变化
 * 要注意的是：
 * 子ListView的每个Item必须是LinearLayout，不能是其他的，因为其他的Layout(如RelativeLayout)没有重写onMeasure()，所以会在onMeasure()时抛出异常。
 */

public class ListViewSetHightUtils {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //若是高度小小于300px那么固定高度为300
        int ht;
        if (height < 100) {
            ht = 100;
        } else {
            ht = height;
        }
        params.height = ht;
        listView.setLayoutParams(params);
    }
}
