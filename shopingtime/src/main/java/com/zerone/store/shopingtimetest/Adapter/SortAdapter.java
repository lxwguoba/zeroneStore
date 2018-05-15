package com.zerone.store.shopingtimetest.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.zerone.store.shopingtimetest.Interface.RvListener;
import com.zerone.store.shopingtimetest.R;
import com.zerone.store.shopingtimetest.ViewHolder.RvHolder;

import java.util.List;

public class SortAdapter extends RvAdapter<String> {

    private int checkedPosition;

    public SortAdapter(Context context, List<String> list, RvListener listener) {
        super(context, list, listener);
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_sort_list;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new SortHolder(view, viewType, listener);
    }

    private class SortHolder extends RvHolder<String> {

        private TextView tvName;
        private View mView;

        SortHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            this.mView = itemView;
            tvName = itemView.findViewById(R.id.tv_sort);
        }

        @Override
        public void bindHolder(String string, int position) {
            tvName.setText(string);
            if (position == checkedPosition) {
                mView.setBackgroundColor(Color.parseColor("#f3f3f3"));
                tvName.setTextColor(Color.parseColor("#0068cf"));
            } else {
                mView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tvName.setTextColor(Color.parseColor("#1e1d1d"));
            }
        }

    }
}
