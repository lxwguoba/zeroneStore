package com.zerone.store.shopingtimetest.Utils.recycleview;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import com.zerone.store.shopingtimetest.Adapter.shopplistadapter.RecycleGoodsCategoryListAdapter;
import com.zerone.store.shopingtimetest.Bean.ShopBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2018/5/31 0031 10 36.
 * Author  LiuXingWen
 */

public class MyOnGoodsScrollListener extends RecyclerView.OnScrollListener implements AbsListView.OnScrollListener {
    //上一个标题的小标位置
    private int lastTitlePoi;
    //商品列表
    private List<ShopBean> goodsitemlist = new ArrayList<>();
    private RecycleGoodsCategoryListAdapter mGoodsCategoryListAdapter;

    public MyOnGoodsScrollListener(int lastTitlePoi, List<ShopBean> goodsitemlist) {
        this.lastTitlePoi = lastTitlePoi;
        this.goodsitemlist = goodsitemlist;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!(lastTitlePoi == goodsitemlist.get(firstVisibleItem).getId())) {
            lastTitlePoi = goodsitemlist.get(firstVisibleItem).getId();
            mGoodsCategoryListAdapter.setCheckPosition(goodsitemlist.get(firstVisibleItem).getId());
            mGoodsCategoryListAdapter.notifyDataSetChanged();
        }
    }

//    /**
//     * 处理滑动 是两个ListView联动
//     * 需要改动
//     */
//    private class MyOnGoodsScrollListener extends RecyclerView.OnScrollListener implements AbsListView.OnScrollListener {
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//        }
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            if (!(lastTitlePoi == goodsitemlist.get(firstVisibleItem).getId())) {
//                lastTitlePoi = goodsitemlist.get(firstVisibleItem).getId();
//                mGoodsCategoryListAdapter.setCheckPosition(goodsitemlist.get(firstVisibleItem).getId());
//                mGoodsCategoryListAdapter.notifyDataSetChanged();
//            }
//        }
//    }
}
