package com.zerone.store.shopingtimetest.Activity.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.zerone.store.shopingtimetest.Adapter.cart_list.OrderDetialsListItemAdapter;
import com.zerone.store.shopingtimetest.Base64AndMD5.CreateToken;
import com.zerone.store.shopingtimetest.Bean.UserInfo;
import com.zerone.store.shopingtimetest.Bean.order.GoodsBean;
import com.zerone.store.shopingtimetest.Contants.IpConfig;
import com.zerone.store.shopingtimetest.DB.impl.UserInfoImpl;
import com.zerone.store.shopingtimetest.R;
import com.zerone.store.shopingtimetest.Utils.LoadingUtils;
import com.zerone.store.shopingtimetest.Utils.NetUtils;
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
 * Created by on 2018/4/2 0002 19 32.
 * Author  LiuXingWen
 */

public class OrderDetailsYFKActivity extends AppCompatActivity {

    private ListView listView;
    private OrderDetailsYFKActivity mContext;
    private UserInfo userInfo;
    private ZLoadingDialog loading_dailog;
    private Intent intent;
    private List<GoodsBean> list;
    private OrderDetialsListItemAdapter odlia;
    private TextView totalmoney;
    private TextView zhifufangshi;
    private TextView xiaofeizhe;
    private TextView jiedaiy;
    private TextView ordersn;
    private TextView beizhu;
    private RelativeLayout back;
    private TextView ordertime;
    private TextView discountmoney;
    private TextView paymoney;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String yfkJSon = (String) msg.obj;
                    loading_dailog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(yfkJSon);
                        int status = jsonObject.getInt("status");

                        if (status == 1) {
                            long aLong = jsonObject.getJSONObject("data").getJSONObject("orderdata").getLong("created_at") * 1000;
                            //下单时间
                            Date d = new Date(aLong);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("ordergoods");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                GoodsBean gb = new GoodsBean();
                                gb.setGoods_total(jsonArray.getJSONObject(i).getString("total"));
                                gb.setGoods_price(jsonArray.getJSONObject(i).getString("price"));
                                gb.setGoods_title(jsonArray.getJSONObject(i).getString("title"));
                                gb.setGoods_details(jsonArray.getJSONObject(i).getString("details"));
                                gb.setGoods_id(jsonArray.getJSONObject(i).getString("goods_id"));
                                gb.setGoods_thumb(jsonArray.getJSONObject(i).getString("thumb"));
                                list.add(gb);
                            }
                            totalmoney.setText("￥" + jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("order_price"));
                            discountmoney.setText("￥" + jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("discount_price"));
                            paymoney.setText("￥" + jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("payment_price"));

                            String paytype = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("paytype");
                            if ("-1".equals(paytype)) {
                                zhifufangshi.setText("现金支付");
                            } else if ("0".equals(paytype)) {
                                zhifufangshi.setText("银行卡支付");
                            } else if ("1".equals(paytype)) {
                                zhifufangshi.setText("支付宝扫码");
                            } else if ("2".equals(paytype)) {
                                zhifufangshi.setText("支付宝二维码");
                            } else if ("3".equals(paytype)) {
                                zhifufangshi.setText("微信扫码");
                            } else if ("4".equals(paytype)) {
                                zhifufangshi.setText("微信二维码");
                            }
                            int userid = jsonObject.getJSONObject("data").getJSONObject("orderdata").getInt("user_id");
                            if (userid == 0) {
                                xiaofeizhe.setText("散客");
                            } else {
                                xiaofeizhe.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("nickname"));
                            }
                            ordertime.setText(sdf.format(d));
                            jiedaiy.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("realname"));
                            ordersn.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("ordersn"));
                            beizhu.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("remarks"));
                        } else if (status == 0) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        odlia.notifyDataSetChanged();
                    }
                    break;
                case 511:
                    Toast.makeText(OrderDetailsYFKActivity.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    loading_dailog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_yfk);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
        list = new ArrayList<>();
        intent = getIntent();
        mContext = this;
        initGetUserInfo();
        initView();
        initGetDataOrderDetails();
        action();

    }

    private void action() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsYFKActivity.this.finish();
            }
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        totalmoney = (TextView) findViewById(R.id.totalmoney);
        discountmoney = (TextView) findViewById(R.id.discountmoney);
        paymoney = (TextView) findViewById(R.id.paymoney);
        zhifufangshi = (TextView) findViewById(R.id.zhifufangshi);
        xiaofeizhe = (TextView) findViewById(R.id.xiaofeizhe);
        jiedaiy = (TextView) findViewById(R.id.jiedaiy);
        ordersn = (TextView) findViewById(R.id.ordersn);
        beizhu = (TextView) findViewById(R.id.beizhu);
        back = (RelativeLayout) findViewById(R.id.back);
        ordertime = (TextView) findViewById(R.id.ordertime);
        listView = (ListView) findViewById(R.id.goodslist);
        odlia = new OrderDetialsListItemAdapter(this, list);
        listView.setAdapter(odlia);

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
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> getOrderDetails = new HashMap<String, String>();
        getOrderDetails.put("account_id", userInfo.getAccount_id());
        getOrderDetails.put("organization_id", userInfo.getOrganization_id());
        getOrderDetails.put("order_id", intent.getStringExtra("orderid"));
        getOrderDetails.put("token", token);
        getOrderDetails.put("timestamp", timestamp);
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取订单详情中。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(mContext, getOrderDetails, IpConfig.URL_ORDERDETAILS, handler, 0);
    }
}
