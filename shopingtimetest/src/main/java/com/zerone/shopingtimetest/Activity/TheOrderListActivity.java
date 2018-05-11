package com.zerone.shopingtimetest.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone.shopingtimetest.Adapter.cart_list.OrderListItemAdapter;
import com.zerone.shopingtimetest.Base64AndMD5.CreateToken;
import com.zerone.shopingtimetest.Bean.UserInfo;
import com.zerone.shopingtimetest.Bean.order.OrderBean;
import com.zerone.shopingtimetest.Contants.IpConfig;
import com.zerone.shopingtimetest.DB.impl.UserInfoImpl;
import com.zerone.shopingtimetest.R;
import com.zerone.shopingtimetest.Utils.AppSharePreferenceMgr;
import com.zerone.shopingtimetest.Utils.DoubleUtils;
import com.zerone.shopingtimetest.Utils.LoadingUtils;
import com.zerone.shopingtimetest.Utils.NetUtils;
import com.zerone.shopingtimetest.view.RecycleView.PullRefreshLayout;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/3/30 0030 13 52.
 * Author  LiuXingWen
 * 订单类表
 */

public class TheOrderListActivity extends AppCompatActivity implements PullRefreshLayout.OnRefreshListener {

    private ListView orderlistview;
    private List<OrderBean> list;
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
    private PullRefreshLayout mSwipeLayout;
    //这个是 判断当前页面是那个订单转态，"" 空字符串是全部订单、 "'0'" 这个是待付款订单 、"1"是已完成订单、"-1"是已取消订单
    private String orderState = "";
    private Dialog dialog;
    /**
     * 定时器
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 20:
                    //关闭提示信息的对话框
                    dialog.dismiss();
                    break;
            }
        }

    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case 0:
//                    list.clear();
//                    String orderListJSon = (String) msg.obj;
//                    Log.i("URL", orderListJSon);
//                    loading_dailog.dismiss();
//                    try {
//                        JSONObject jsonObject = new JSONObject(orderListJSon);
//                        int status = jsonObject.getInt("status");
//                        if (status == 1) {
//                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("orderlist");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject orderbean = jsonArray.getJSONObject(i);
//                                OrderBean ob = new OrderBean();
//                                ob.setId(orderbean.getString("id"));
//                                ob.setStatus(orderbean.getString("status"));
//                                ob.setOrder_price(orderbean.getString("order_price"));
//                                ob.setOrdersn(orderbean.getString("ordersn"));
//                                long created_at = Long.parseLong(orderbean.getString("created_at")) * 1000;
//                                Date d = new Date(created_at);
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                ob.setCreated_at(sdf.format(d));
//                                list.add(ob);
//                            }
//                            orderTotal.setText(jsonObject.getJSONObject("data").getString("total_num"));
//                            orderTotalPrice.setText(DoubleUtils.setSSWRDouble(Double.parseDouble(jsonObject.getJSONObject("data").getString("total_amount"))));
//                        } else if (status == 0) {
//                            //获取失败
//                            customDialog(jsonObject.getString("msg") + "，2秒后自动关闭");
//                        }
//                    } catch (JSONException e) {
//                    } finally {
//                        if (mSwipeLayout != null) {
//                            if (mSwipeLayout.isRefreshing()) {
//                                //关闭刷新动画
//                                mSwipeLayout.setRefreshing(false);
//                            }
//                        }
//                        orderListItemAdapter.notifyDataSetChanged();
//                    }
//                    break;

                case 0:
                    list.clear();
                    String orderListJSon = (String) msg.obj;
                    Log.i("URL", orderListJSon);
                    loading_dailog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(orderListJSon);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data").getJSONObject("orderlist");
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject orderbean = jsonArray.getJSONObject(i);
                                OrderBean ob = new OrderBean();
                                ob.setId(orderbean.getString("id"));
                                ob.setStatus(orderbean.getString("status"));
                                ob.setOrder_price(orderbean.getString("order_price"));
                                ob.setOrdersn(orderbean.getString("ordersn"));
                                long created_at = Long.parseLong(orderbean.getString("created_at")) * 1000;
                                Date d = new Date(created_at);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                ob.setCreated_at(sdf.format(d));
                                list.add(ob);
                            }
                            //current_page 当前页面
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "current_page", jsonObject1.getString("current_page"));
                            //下一次要到的页面 当前页面
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "to", jsonObject1.getString("to"));
                            //last_page最后一页
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "last_page", jsonObject1.getString("last_page"));
                            //============================
                            //记住当前页面的总金额
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "money", jsonObject.getJSONObject("data").getString("total_amount"));
                            orderTotal.setText(jsonObject.getJSONObject("data").getString("total_num"));
                            orderTotalPrice.setText(DoubleUtils.setSSWRDouble(Double.parseDouble(jsonObject.getJSONObject("data").getString("total_amount"))));
                        } else if (status == 0) {
                            //获取失败
                            customDialog(jsonObject.getString("msg") + "，2秒后自动关闭");
                        }
                    } catch (JSONException e) {
                    } finally {
                        if (mSwipeLayout != null) {
                            //关闭刷新动画
                            mSwipeLayout.loadMoreFinished();

                        }
                        orderListItemAdapter.notifyDataSetChanged();
                    }
                    break;

                case 1:
                    //上拉加载更多
                    String orderListJS = (String) msg.obj;
                    Log.i("URL", orderListJS);
                    loading_dailog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(orderListJS);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data").getJSONObject("orderlist");
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject orderbean = jsonArray.getJSONObject(i);
                                OrderBean ob = new OrderBean();
                                ob.setId(orderbean.getString("id"));
                                ob.setStatus(orderbean.getString("status"));
                                ob.setOrder_price(orderbean.getString("order_price"));
                                ob.setOrdersn(orderbean.getString("ordersn"));
                                long created_at = Long.parseLong(orderbean.getString("created_at")) * 1000;
                                Date d = new Date(created_at);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                ob.setCreated_at(sdf.format(d));
                                list.add(ob);
                            }
                            //current_page 当前页面
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "current_page", jsonObject1.getString("current_page"));
                            //下一次要到的页面 当前页面
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "to", jsonObject1.getString("to"));
                            //last_page最后一页
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "last_page", jsonObject1.getString("last_page"));
                            //============================
                            //当前加载的数量
                            String count = jsonObject.getJSONObject("data").getString("total_num");
                            //订单列表中原来的数量
                            String ordercount = orderTotal.getText().toString().trim();
                            //当前页面的总数量
                            int con = Integer.parseInt(count) + Integer.parseInt(ordercount);
                            double money = Double.parseDouble((String) AppSharePreferenceMgr.get(TheOrderListActivity.this, "money", "0"));
                            double price = money + Double.parseDouble(jsonObject.getJSONObject("data").getString("total_amount"));
                            //重新记录
                            AppSharePreferenceMgr.put(TheOrderListActivity.this, "money", price);
                            orderTotal.setText(con + "");
                            orderTotalPrice.setText(DoubleUtils.setSSWRDouble(price));

                        } else if (status == 0) {
                            //获取失败
                            customDialog(jsonObject.getString("msg") + "，2秒后自动关闭");
                        }
                    } catch (JSONException e) {
                    } finally {
                        if (mSwipeLayout != null) {
                            mSwipeLayout.refreshFinished();
                        }
                        orderListItemAdapter.notifyDataSetChanged();
                    }
                    break;
                case 511:
                    Toast.makeText(TheOrderListActivity.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    loading_dailog.dismiss();
                    break;
                case 20000:
                    int postion = (int) msg.obj;
                    Intent intent = new Intent(TheOrderListActivity.this, OrderDetailsActivity.class);
                    intent.putExtra("orderid", list.get(postion).getId());
                    startActivityForResult(intent, 1220);
                    break;
            }
        }
    };
    private RelativeLayout allrelative_qx;
    private RelativeLayout allrelative;
    private RelativeLayout allrelative_dfk;
    private RelativeLayout allrelative_wc;
    private LinearLayout layout_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_theorderlsit);
        list = new ArrayList<>();
        post = 0;
        mContext = this;
        initGetUserInfo();
        initView();
        mSwipeLayout.setRefreshListener(this);
        action();
        initGetDataOrderList("");
    }

    /**
     * 初始化数据
     */
    private void initView() {
        //刷新按钮
        mSwipeLayout = (PullRefreshLayout) findViewById(R.id.swipe_ly);
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
                initGetDataOrderList("");
                orderState = "";
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
                initGetDataOrderList("'0'");
                orderState = "'0'";

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
                initGetDataOrderList("1");
                orderState = "1";
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
                initGetDataOrderList("-1");
                orderState = "-1";
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
    private void initGetDataOrderList(String status) {
        //默认是没有开启的
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> getOrderDetails = new HashMap<String, String>();
        getOrderDetails.put("account_id", userInfo.getAccount_id());
        getOrderDetails.put("organization_id", userInfo.getOrganization_id());
        getOrderDetails.put("status", status);
        getOrderDetails.put("token", token);
        getOrderDetails.put("timestamp", timestamp);
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取订单列表。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(mContext, getOrderDetails, IpConfig.URL_ORDERLIST, handler, 0);
    }

    /**
     * 获取订单列表数据 上拉加载
     */
    private void refreshpushdown(String status) {
        //默认是没有开启的
        //下一次要到的页面 当前页面
        String per_page = (String) AppSharePreferenceMgr.get(TheOrderListActivity.this, "current_page", "-1");
        //last_page最后一页
        String last_page = (String) AppSharePreferenceMgr.get(TheOrderListActivity.this, "last_page", "0");
        if (per_page.equals(last_page)) {
            if (mSwipeLayout != null) {
                mSwipeLayout.loadMoreFinished();
            }
            return;
        }
        int page = Integer.parseInt(per_page);
        page++;
        Log.i("URL", page + "");
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> getOrderDetails = new HashMap<String, String>();
        getOrderDetails.put("account_id", userInfo.getAccount_id());
        getOrderDetails.put("organization_id", userInfo.getOrganization_id());
        getOrderDetails.put("status", status);
        getOrderDetails.put("token", token);
        getOrderDetails.put("page", page + "");
        getOrderDetails.put("timestamp", timestamp);
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "加载中。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(mContext, getOrderDetails, IpConfig.URL_ORDERLIST, handler, 1);
    }

    /**
     * 获取用户信息
     */
    private void initGetUserInfo() {
        UserInfoImpl userInfoImpl = new UserInfoImpl(mContext);
        try {
            userInfo = userInfoImpl.getUserInfo("10");
        } catch (Exception e) {
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
                    initGetDataOrderList("'0'");
                } else if (resultCode == 300) {
                    if (post == 0) {
                        initGetDataOrderList("");
                    } else if (post == 1) {
                        initGetDataOrderList("'0'");
                    }
                }
                break;
        }

    }

    /**
     * 弹框提示
     *
     * @param msg 提示消息
     */
    private void customDialog(String msg) {
        dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        TextView errormsg = view.findViewById(R.id.errormsg);
        errormsg.setText(msg);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //第二个参数是几秒后关闭
        mHandler.sendEmptyMessageDelayed(20, 2000);
    }


    //++++++++++++++++++刷新加载+++++++++++++++++++++++++
    @Override
    public void refreshFinished() {
        //下拉刷新
        initGetDataOrderList(orderState);
    }

    @Override
    public void loadMoreFinished() {
        refreshpushdown(orderState);
    }
}
