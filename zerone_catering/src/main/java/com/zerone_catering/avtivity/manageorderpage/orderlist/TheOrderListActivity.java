package com.zerone_catering.avtivity.manageorderpage.orderlist;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.adapter.cart_list.OrderListItemAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.details.OrderDetailsYFKActivity;
import com.zerone_catering.avtivity.details.OrderDetailsYQXActivity;
import com.zerone_catering.avtivity.details.Order_Details_Cashier_ForThePayment_Activity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.refresh.RefreshBean;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by on 2018/3/30 0030 13 52.
 * Author  LiuXingWen
 * 订单类表
 */

public class TheOrderListActivity extends BaseActvity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private BGARefreshLayout mRefreshLayout;
    private ListView orderlistview;
    private List<TheOrderListBean.DataBean.OrderListBean> list;
    private List<TheOrderListBean.DataBean.OrderListBean> tlist;
    private OrderListItemAdapter orderListItemAdapter;
    private TextView alltvorder;
    private TextView alltvdfk;
    private TextView alltvywc;
    private TextView alltvyqx;
    private View alltvorder_line;
    private View alltvdfk_line;
    private View alltvywc_line;
    private View alltvyqx_line;
    private UserInfo userInfo;
    private TheOrderListActivity mContext;
    private ZLoadingDialog loading_dailog;
    private TextView orderTotal;
    private TextView orderTotalPrice;
    private ImageView back;
    //这个是用来结账后判断是全部订单还是待付款订单按钮触发的  0为全部订单  1为待付款订单
    private int post;
    private boolean isnext = true;
    //这个是 判断当前页面是那个订单转态，"" 空字符串是全部订单、 "'0'" 这个是待付款订单 、"1"是已完成订单、"-1"是已取消订单
    private String orderState = "";
    private Dialog dialog;
    private RelativeLayout allrelative_qx;
    private RelativeLayout allrelative;
    private RelativeLayout allrelative_dfk;
    private RelativeLayout allrelative_wc;
    private LinearLayout layout_back;
    private int page = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String orderListJSon = (String) msg.obj;
                    loading_dailog.dismiss();
                    Log.i("URL", orderListJSon + "");
                    try {
                        JSONObject jsonObject = new JSONObject(orderListJSon);
                        int status = jsonObject.getInt("status");
                        list.clear();
                        if (status == 1) {
                            Gson gson = new Gson();
                            TheOrderListBean theOrderListBean = gson.fromJson(orderListJSon, TheOrderListBean.class);
                            List<TheOrderListBean.DataBean.OrderListBean> order_list = theOrderListBean.getData().getOrder_list();
                            for (int i = 0; i < order_list.size(); i++) {
                                list.add(order_list.get(i));
                            }
                            //记住当前页面的总金额
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "money", theOrderListBean.getData().getPrice_sum() + "");
                            orderTotal.setText(theOrderListBean.getData().getOrder_num() + "");
                            Log.i("URL", theOrderListBean.getData().getPrice_sum() + "");
                            orderTotalPrice.setText(theOrderListBean.getData().getPrice_sum() + "");
                            page++;
                            orderListItemAdapter.notifyDataSetChanged();
                        } else if (status == 0) {
                            //获取失败
                            page = 1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (mRefreshLayout != null) {
                            mRefreshLayout.endRefreshing();
                        }
                        orderListItemAdapter.notifyDataSetChanged();
                    }
                    break;

                case 2:
                    String orderListJSo = (String) msg.obj;

                    try {
                        JSONObject jsonObject = new JSONObject(orderListJSo);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            if (page == 1) {
                                list.clear();
                            }
                            Gson gson = new Gson();
                            TheOrderListBean theOrderListBean = gson.fromJson(orderListJSo, TheOrderListBean.class);
                            List<TheOrderListBean.DataBean.OrderListBean> order_list = theOrderListBean.getData().getOrder_list();
                            for (int i = 0; i < order_list.size(); i++) {
                                list.add(order_list.get(i));
                            }
                            //记住当前页面的总金额
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "money", theOrderListBean.getData().getPrice_sum() + "");
                            orderTotal.setText(theOrderListBean.getData().getOrder_num() + "");
                            orderTotalPrice.setText(theOrderListBean.getData().getPrice_sum() + "");
                            page++;
                            orderListItemAdapter.notifyDataSetChanged();
                        } else if (status == 0) {
                            //获取失败
                            page = 1;
                            isnext = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (loading_dailog != null) {
                            loading_dailog.dismiss();
                        }
                        if (mRefreshLayout != null) {
                            mRefreshLayout.endLoadingMore();
                        }
                    }
                    break;
                case 100:
                    try {
                        if (post == 0) {
                            initGetDataOrderList("", 0);
                        } else if (post == 1) {
                            initGetDataOrderList("'0'", 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 511:
                    Toast.makeText(TheOrderListActivity.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    loading_dailog.dismiss();
                    break;
                case 20000:
                    int postion = (int) msg.obj;
                    Intent intent = null;
                    if ("0".equals(list.get(postion).getStatus())) {
                        //待付款
                        intent = new Intent(TheOrderListActivity.this, Order_Details_Cashier_ForThePayment_Activity.class);
                        intent.putExtra("pagestatus", "6000");
                    } else if ("-1".equals(list.get(postion).getStatus())) {
                        //已取消
                        intent = new Intent(TheOrderListActivity.this, OrderDetailsYQXActivity.class);
                    } else if ("1".equals(list.get(postion).getStatus())) {
                        //已完成
                        intent = new Intent(TheOrderListActivity.this, OrderDetailsYFKActivity.class);
                    }
                    intent.putExtra("order_id", list.get(postion).getId() + "");
                    startActivityForResult(intent, 1220);
                    break;
            }
        }
    };
    private SwipeRefreshLayout swRefresh;
    private BGANormalRefreshViewHolder refreshViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_theorderlsit);
        //注册广播接受器
        EventBus.getDefault().register(this);
        list = new ArrayList<>();
        tlist = new ArrayList<>();
        post = 0;
        mContext = this;
        userInfo = GetUserInfo.initGetUserInfo(this);
        initView();
        initRefreshLayout();
        action();
        initGetDataOrderList("", 0);
    }

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格    参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        refreshViewHolder = new BGANormalRefreshViewHolder(this, true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        mRefreshLayout.setIsShowLoadingMoreView(true);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在获取更多...");
        refreshViewHolder.setReleaseRefreshText("正在获取更多...");
        // 设置整个加载更多控件的背景颜色资源 id

    }

    /**
     * 初始化数据
     */
    private void initView() {
//        swRefresh = (SwipeRefreshLayout) findViewById(R.id.swRefresh);
        //刷新按钮
        orderTotal = (TextView) findViewById(R.id.orderTotal);
        orderTotalPrice = (TextView) findViewById(R.id.orderTotalPrice);
        //导航按钮============================
        allrelative = (RelativeLayout) findViewById(R.id.allrelative);
        allrelative_dfk = (RelativeLayout) findViewById(R.id.allrelative_dfk);
        allrelative_wc = (RelativeLayout) findViewById(R.id.allrelative_wc);
        allrelative_qx = (RelativeLayout) findViewById(R.id.allrelative_qx);
        alltvorder = (TextView) findViewById(R.id.alltvorder);
        alltvdfk = (TextView) findViewById(R.id.alltvdfk);
        alltvywc = (TextView) findViewById(R.id.alltvywc);
        alltvyqx = (TextView) findViewById(R.id.alltvyqx);
        alltvorder_line = findViewById(R.id.alltvorder_line);
        alltvdfk_line = findViewById(R.id.alltvdfk_line);
        alltvywc_line = findViewById(R.id.alltvywc_line);
        alltvyqx_line = findViewById(R.id.alltvyqx_line);
        //导航按钮============================
        orderlistview = (ListView) findViewById(R.id.orderlistview);
        orderListItemAdapter = new OrderListItemAdapter(this, list, handler);
        orderlistview.setAdapter(orderListItemAdapter);
        back = (ImageView) findViewById(R.id.back);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
    }

    /**
     * 导航按钮的点击事件
     */
    private void action() {
        //所有订单
        allrelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post = 0;
                alltvdfk.setTextColor(Color.parseColor("#7c7c7c"));
                alltvywc.setTextColor(Color.parseColor("#7c7c7c"));
                alltvyqx.setTextColor(Color.parseColor("#7c7c7c"));
                alltvorder.setTextColor(Color.parseColor("#000000"));
                alltvorder_line.setVisibility(View.VISIBLE);
                alltvdfk_line.setVisibility(View.GONE);
                alltvywc_line.setVisibility(View.GONE);
                alltvyqx_line.setVisibility(View.GONE);
                initGetDataOrderList("", 0);
                orderState = "";
                isnext = true;
                orderListItemAdapter.notifyDataSetChanged();
            }
        });
        //待付款
        allrelative_dfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post = 1;
                alltvdfk.setTextColor(Color.parseColor("#000000"));
                alltvywc.setTextColor(Color.parseColor("#7c7c7c"));
                alltvyqx.setTextColor(Color.parseColor("#7c7c7c"));
                alltvorder.setTextColor(Color.parseColor("#7c7c7c"));
                alltvorder_line.setVisibility(View.GONE);
                alltvdfk_line.setVisibility(View.VISIBLE);
                alltvywc_line.setVisibility(View.GONE);
                alltvyqx_line.setVisibility(View.GONE);
                initGetDataOrderList("'0'", 0);
                orderState = "'0'";
                isnext = true;
                orderListItemAdapter.notifyDataSetChanged();

            }
        });
        //已完成
        allrelative_wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alltvdfk.setTextColor(Color.parseColor("#7c7c7c"));
                alltvywc.setTextColor(Color.parseColor("#000000"));
                alltvyqx.setTextColor(Color.parseColor("#7c7c7c"));
                alltvorder.setTextColor(Color.parseColor("#7c7c7c"));
                alltvorder_line.setVisibility(View.GONE);
                alltvdfk_line.setVisibility(View.GONE);
                alltvywc_line.setVisibility(View.VISIBLE);
                alltvyqx_line.setVisibility(View.GONE);
                initGetDataOrderList("1", 0);
                orderState = "1";
                isnext = true;
                orderListItemAdapter.notifyDataSetChanged();
            }
        });
        //已取消
        allrelative_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alltvdfk.setTextColor(Color.parseColor("#7c7c7c"));
                alltvywc.setTextColor(Color.parseColor("#7c7c7c"));
                alltvyqx.setTextColor(Color.parseColor("#000000"));
                alltvorder.setTextColor(Color.parseColor("#7c7c7c"));
                alltvorder_line.setVisibility(View.GONE);
                alltvdfk_line.setVisibility(View.GONE);
                alltvywc_line.setVisibility(View.GONE);
                alltvyqx_line.setVisibility(View.VISIBLE);
                initGetDataOrderList("-1", 0);
                orderState = "-1";
                isnext = true;
                orderListItemAdapter.notifyDataSetChanged();
            }
        });
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TheOrderListActivity.this.finish();
            }
        });
    }

    /**
     * 获取订单列表数据
     */
    private void initGetDataOrderList(String status, int sta) {
        //默认是没有开启的
        try {
            String timestamp = System.currentTimeMillis() + "";
            String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
            Map<String, String> map = new HashMap<String, String>();
            map.put("account_id", userInfo.getAccount_id());
            map.put("organization_id", userInfo.getOrganization_id());
            if (sta == 2) {
                map.put("page", page + "");
            } else if (sta == 0) {
                map.put("page", "1");
            }
            if ("".equals(status) && sta == 0) {
                map.put("page", "0");
            }
            map.put("token", token);
            map.put("timestamp", timestamp);
            if (!"".equals(status)) {
                map.put("status", status);
            }
            loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取订单列表...");
            loading_dailog.show();
            if (sta == 2) {
                NetUtils.netWorkByMethodPost(mContext, map, IpConfig.URL_ORDER_MERGE_LIST, handler, 2);
            } else {
                NetUtils.netWorkByMethodPost(mContext, map, IpConfig.URL_ORDER_MERGE_LIST, handler, 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 返回的数据处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1220:
                if (resultCode == RESULT_OK) {
                    //这个是返回的页面
                    initGetDataOrderList("'0'", 0);
                } else if (resultCode == 300) {
                    if (post == 0) {
                        initGetDataOrderList("", 0);
                    } else if (post == 1) {
                        initGetDataOrderList("'0'", 0);
                    }
                }
                break;
        }

    }

    /**
     * 接收搜索框的添加数据的商品信息
     *
     * @param refreshBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void freshBuy(RefreshBean refreshBean) {
        //接收到清空购车的信息了
        if (refreshBean.getRefreshCode() == 100) {
            Message message = new Message();
            message.what = 100;
            handler.sendMessage(message);
        } else if (refreshBean.getRefreshCode() == 6000 && "orderlist".equals(refreshBean.getRefreshName())) {
            initGetDataOrderList(orderState, 0);
        }
    }

    //==============================下拉刷新操作========================
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        initGetDataOrderList(orderState, 0);
        isnext = true;
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (isnext) {
            initGetDataOrderList(orderState, 2);
        } else {
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }
}
