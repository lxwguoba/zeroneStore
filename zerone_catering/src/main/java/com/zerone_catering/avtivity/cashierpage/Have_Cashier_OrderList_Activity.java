package com.zerone_catering.avtivity.cashierpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.adapter.OrderItemCashierListAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.colse.CloseActivity;
import com.zerone_catering.domain.payorderlistbean.OrderCashierListBean;
import com.zerone_catering.domain.refresh.RefreshBean;
import com.zerone_catering.domain.tablefinal.cashiertable.TableListInfoCashierFinal;
import com.zerone_catering.utils.DoubleUtils;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.NetworkUtil;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/6/14 0014 15 06.
 * Author  LiuXingWen
 * 这个是有单收银 订单列表页面
 */

public class Have_Cashier_OrderList_Activity extends BaseActvity {
    private RecyclerView order_list;
    private OrderItemCashierListAdapter oadaper;
    private RelativeLayout all_check_order_layout;
    private ImageView all_check_order;
    private List<OrderCashierListBean> list;
    private TextView allPrice;
    //桌子信息
    private TableListInfoCashierFinal tabinfo;
    private TextView table_name;
    private UserInfo userInfo;
    private Context mContext;
    private ZLoadingDialog loading_dailog;

    private boolean allcheck = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String orderlistJson = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(orderlistJson);
                        int status = jsonObject.getInt("status");
                        list.clear();
                        if (status == 1) {
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("orderlist");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                OrderCashierListBean ocl = new OrderCashierListBean();
                                ocl.setId(jsonArray.getJSONObject(i).getInt("id"));
                                ocl.setCreated_at(jsonArray.getJSONObject(i).getString("created_at"));
                                ocl.setOchecklean(false);
                                ocl.setOrder_price(jsonArray.getJSONObject(i).getString("order_price"));
                                ocl.setOrdersn(jsonArray.getJSONObject(i).getString("ordersn"));
                                ocl.setStatus(jsonArray.getJSONObject(i).getString("status"));
                                list.add(ocl);
                            }
                        } else if (status == 0) {
                        }
                        all_check_order.setImageResource(R.mipmap.check_box);
                        allcheck = false;
                        allPrice.setText("共计：");
                        oadaper.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (!Have_Cashier_OrderList_Activity.this.isFinishing()) {
                            loading_dailog.dismiss();
                        }
                    }
                    break;
                case 1:
                    //收到了点击按钮checkbox的按钮
                    Double money = 0.0;
                    boolean checkall = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isOchecklean()) {
                            money += Double.parseDouble(list.get(i).getOrder_price());
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isOchecklean()) {
                            checkall = true;
                        } else {
                            checkall = false;
                            break;
                        }
                    }
                    if (checkall) {
                        all_check_order.setImageResource(R.mipmap.checked_box);
                        allcheck = true;
                    } else {
                        all_check_order.setImageResource(R.mipmap.check_box);
                        allcheck = false;
                    }

                    String nmoney = DoubleUtils.subMoney(money + "");
                    allPrice.setText("共计：￥" + nmoney);
                    break;
                case 10:
                    //收到了点击按钮checkbox的按钮
                    Double mmoney = 0.0;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isOchecklean()) {
                            mmoney += Double.parseDouble(list.get(i).getOrder_price());
                        }
                    }
                    String mmmoney = DoubleUtils.subMoney(mmoney + "");
                    allPrice.setText("共计：￥" + mmmoney);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.have_order_cashier_activity);
        EventBus.getDefault().register(this);
        Intent intent = this.getIntent();
        mContext = this;
        tabinfo = (TableListInfoCashierFinal) intent.getSerializableExtra("talbe");
        userInfo = GetUserInfo.initGetUserInfo(this);
        list = new ArrayList<>();
        initView();
        initData();

    }
    /**
     * 获取订单列表信息
     */
    private void initData() {
        if (userInfo == null) {
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> tMap = new HashMap<String, String>();
        tMap.put("account_id", userInfo.getAccount_id());
        tMap.put("timestamp", timestamp);
        tMap.put("organization_id", userInfo.getOrganization_id());
        tMap.put("token", token);
        tMap.put("table_id", tabinfo.getId() + "");

        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取订单中...");
        if (!Have_Cashier_OrderList_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_CASHIER_ORDER_LIST, handler, 0);
    }
    private void initView() {
        setRecycleView();
        table_name = (TextView) findViewById(R.id.table_name);
        table_name.setText("桌位：" + tabinfo.getTable_name());
        all_check_order_layout = (RelativeLayout) findViewById(R.id.all_check_order_layout);
        all_check_order = (ImageView) findViewById(R.id.all_check_order);
        all_check_order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setOchecklean(!allcheck);
                }
                if (allcheck) {
                    all_check_order.setImageResource(R.mipmap.check_box);
                    allcheck = false;
                } else {
                    all_check_order.setImageResource(R.mipmap.checked_box);
                    allcheck = true;
                }
                oadaper.notifyDataSetChanged();
                Message message = new Message();
                message.what = 10;
                handler.sendMessage(message);
            }
        });
        findViewById(R.id.sub_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认收银订单
                Intent intent = new Intent(Have_Cashier_OrderList_Activity.this, MakeSure_Cashier_Order_Activity.class);
                List<OrderCashierListBean> orderCashierListBeen = setSubmitList();
                if (orderCashierListBeen.size() > 0) {
                    intent.putExtra("list", (Serializable) orderCashierListBeen);
                    intent.putExtra("tableinfo", tabinfo);
                    startActivity(intent);
                } else {
                    Toast.makeText(Have_Cashier_OrderList_Activity.this, "该桌没有订单或者您没有选", Toast.LENGTH_SHORT).show();
                }

            }
        });
        allPrice = (TextView) findViewById(R.id.all_order_checked_money);
    }

    private void setRecycleView() {
        oadaper = new OrderItemCashierListAdapter(list, this, handler);
        order_list = (RecyclerView) findViewById(R.id.order_list);
        order_list.setLayoutManager(new LinearLayoutManager(Have_Cashier_OrderList_Activity.this));
        //添加Android自带的分割线
        order_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        order_list.setAdapter(oadaper);
        oadaper.setOnItemClickListener(new OrderItemCashierListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private List<OrderCashierListBean> setSubmitList() {
        List<OrderCashierListBean> su = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isOchecklean()) {
                su.add(list.get(i));
            }
        }
        return su;
    }

    /**
     * 关闭页面
     *
     * @param closeActivity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeActivity(CloseActivity closeActivity) {
        //接收到清空购车的信息了
        if (closeActivity.getCode() == 400) {
            Have_Cashier_OrderList_Activity.this.finish();

        }
    }

    /**
     * 刷新页面的内容
     *
     * @param rb
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fresh(RefreshBean rb) {
        //接收到清空购车的信息了
        if (rb.getRefreshCode() == 80 && "fresh".equals(rb.getRefreshName())) {
            initData();
        }
    }

    /**
     * 页面摧毁时关闭广播注册
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消广播的注册
        EventBus.getDefault().unregister(this);
    }


}
