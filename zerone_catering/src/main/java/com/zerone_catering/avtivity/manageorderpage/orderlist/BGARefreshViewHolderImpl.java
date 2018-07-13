package com.zerone_catering.avtivity.manageorderpage.orderlist;

import android.content.Context;
import android.view.View;

/**
 * Created by on 2018-07-11 15 22.
 * Author  LiuXingWen
 */

public class BGARefreshViewHolderImpl extends cn.bingoogolapple.refreshlayout.BGARefreshViewHolder {
    /**
     * @param context
     * @param isLoadingMoreEnabled 上拉加载更多是否可用
     */
    public BGARefreshViewHolderImpl(Context context, boolean isLoadingMoreEnabled) {
        super(context, isLoadingMoreEnabled);
    }

    @Override
    public View getRefreshHeaderView() {
        return null;
    }

    @Override
    public void handleScale(float scale, int moveYDistance) {

    }

    @Override
    public void changeToIdle() {

    }

    @Override
    public void changeToPullDown() {

    }

    @Override
    public void changeToReleaseRefresh() {

    }

    @Override
    public void changeToRefreshing() {

    }

    @Override
    public void onEndRefreshing() {

    }
}
