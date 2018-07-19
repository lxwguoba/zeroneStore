package com.zerone_catering.avtivity.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.DB.impl.UserInfoImpl;
import com.zerone_catering.R;
import com.zerone_catering.adapter.cart_list.CheckCancel_OrderListItemAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.order.Order_Cashier_Details_Bean;
import com.zerone_catering.utils.DoubleUtils;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.NetworkUtil;
import com.zerone_catering.utils.UtilsTime;
import com.zerone_catering.utils.view.ListViewSetHightUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 2018/4/2 0002 19 32.
 * Author  LiuXingWen
 */

public class OrderDetailsYFKActivity extends BaseActvity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.relative_back)
    RelativeLayout relativeBack;
    @Bind(R.id.refresh_data)
    LinearLayout refreshData;
    @Bind(R.id.head)
    LinearLayout head;
    @Bind(R.id.ordertime)
    TextView ordertime;
    @Bind(R.id.daohang)
    LinearLayout daohang;
    @Bind(R.id.cancel_goods)
    TextView cancelGoods;
    @Bind(R.id.goodslist)
    ListView listView;
    @Bind(R.id.listOrderMoney)
    TextView listOrderMoney;
    @Bind(R.id.discount)
    TextView discount;
    @Bind(R.id.discount_price)
    TextView discountPrice;
    @Bind(R.id.mingxi)
    LinearLayout mingxi;
    @Bind(R.id.ordersn)
    TextView ordersn;
    @Bind(R.id.jiedaiyuan)
    TextView jiedaiyuan;
    @Bind(R.id.xiaofeizhe)
    TextView xiaofeizhe;
    @Bind(R.id.beizhu)
    TextView beizhu;
    private OrderDetailsYFKActivity mContext;
    private UserInfo userInfo;
    private ZLoadingDialog loading_dailog;
    private Intent intent;
    private List<Order_Cashier_Details_Bean> list;
    private CheckCancel_OrderListItemAdapter codlia;
    private String order_id;
    private TextView roomAndTable;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        String detailsJson = (String) msg.obj;
                        Log.i("URL", detailsJson);
                        JSONObject jsonObject = new JSONObject(detailsJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            list.clear();
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("ordergoods");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Order_Cashier_Details_Bean ocdb = new Order_Cashier_Details_Bean();
                                ocdb.setGoods_id(object.getInt("goods_id"));
                                ocdb.setPrice(object.getString("price"));
                                ocdb.setThumb(object.getString("thumb"));
                                ocdb.setTitle(object.getString("title"));
                                ocdb.setTotal(object.getInt("total"));
                                ocdb.setGoods_ckeck(false);
                                list.add(ocdb);
                            }
                            ordertime.setText(UtilsTime.getTime(Long.parseLong(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("created_at"))));
                            String table_name = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("table_name");
                            String room_name = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("room_name");
                            roomAndTable.setText(room_name + "：" + table_name);
                            ordersn.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("ordersn"));

                            xiaofeizhe.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("nickname"));
                            jiedaiyuan.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("realname"));

                            String remarks = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("remarks");

                            if ("null".equals(remarks) && remarks.length() > 0) {
                                beizhu.setText("没有备注");
                            } else {
                                beizhu.setText(remarks);
                            }
                            String nmoney = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("order_price");
                            listOrderMoney.setText("￥" + DoubleUtils.subMoney(nmoney));
                            String discoun = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("discount");
                            if ("10.00".equals(discoun)) {
                                discount.setText("没有打折");
                            } else {
                                discount.setText(discoun);
                            }
                            String payment_price = DoubleUtils.subMoney(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("payment_price"));
                            if ("null".equals(payment_price)) {
                                discountPrice.setText("您还没有收钱");
                            } else {
                                discountPrice.setText(payment_price);
                            }
                            codlia.notifyDataSetChanged();
                            ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
                        } else if (status == 0) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!OrderDetailsYFKActivity.this.isFinishing()) {
                            loading_dailog.dismiss();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_paymented);
        ButterKnife.bind(this);
        intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        mContext = this;
        initGetUserInfo();
        initView();
        initGetDataOrderDetails();
    }

    /**
     * 初始化view
     */
    private void initView() {
        list = new ArrayList<>();
        roomAndTable = (TextView) findViewById(R.id.roomAndTable);
        codlia = new CheckCancel_OrderListItemAdapter(this, list);
        listView.setAdapter(codlia);
        ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
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

    private void initGetDataOrderDetails() {
        //默认是没有开启的
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
        tMap.put("order_id", order_id + "");
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取详情中...");
        if (!OrderDetailsYFKActivity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_ORDER_MERGE_DETAIL, handler, 0);
    }

    @OnClick({R.id.relative_back, R.id.refresh_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relative_back:
                OrderDetailsYFKActivity.this.finish();
                break;
            case R.id.refresh_data:
                initGetDataOrderDetails();
                break;

        }
    }
}
