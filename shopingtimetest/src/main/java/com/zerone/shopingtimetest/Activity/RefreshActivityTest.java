package com.zerone.shopingtimetest.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zerone.shopingtimetest.BaseActivity.BaseAppActivity;
import com.zerone.shopingtimetest.R;
import com.zerone.shopingtimetest.view.HeaderAndFooter;
import com.zerone.shopingtimetest.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2018/4/26 0026 17 04.
 * Author  LiuXingWen
 */

public class RefreshActivityTest extends BaseAppActivity {

    private static final int REFRESH = 0;
    private static final int LOADMORE = 1;
    private HeaderAndFooter headerAndFooterWrapper;
    private RefreshRecyclerViewAdapter recyclerAdapter;
    private RefreshRecyclerView custom_recyclerview;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    custom_recyclerview.onFinishRefresh(true);
                    break;
                case LOADMORE:
                    custom_recyclerview.onFinishRefresh(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_test);
        custom_recyclerview = (RefreshRecyclerView) findViewById(R.id.custom_recyclerview);
        initRefreshRecyclerView();
    }

    private void initRefreshRecyclerView() {
//        给Recycler设置分割线
//        custom_recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerAdapter = new RefreshRecyclerViewAdapter(this);
        headerAndFooterWrapper = new HeaderAndFooter(recyclerAdapter);
//        不要忘记设置布局管理器
        custom_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        custom_recyclerview.setAdapter(headerAndFooterWrapper);
        custom_recyclerview.addHeaderView(custom_recyclerview.getHeaderView(), headerAndFooterWrapper);
        custom_recyclerview.addFooterView(custom_recyclerview.getFooterView(), headerAndFooterWrapper);
        custom_recyclerview.setOnRefreshListener(new OnRecyclerRefreshListener());

    }

    private class OnRecyclerRefreshListener implements RefreshRecyclerView.OnRefreshListener {
        @Override
        public void onPullDownRefresh() {
            Log.i("URL", "下拉刷新操作哦7777777777777777777777777777777777777777777");
        }

        @Override
        public void onLoadingMore() {
//            执行上拉加载操作，一般是联网请求更多分页数据
            Log.i("URL", "上拉加载更多88888888888888888888888888888888888888888888");
        }
    }
}


class RefreshRecyclerViewAdapter extends RecyclerView.Adapter<RefreshRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<String> datas;

    public RefreshRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            datas.add("我是Content ：" + i);
        }
    }

    @Override
    public RefreshRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = View.inflate(mContext, R.layout.item_recycler, null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RefreshRecyclerViewAdapter.ViewHolder holder, final int position) {
        String data = datas.get(position);
        holder.tv_content.setText(data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_content;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv);
        }
    }
}
