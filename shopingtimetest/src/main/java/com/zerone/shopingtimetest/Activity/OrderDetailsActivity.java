package com.zerone.shopingtimetest.Activity;

import android.app.Dialog;
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

import com.android.qzs.voiceannouncementlibrary.VoiceUtils;
import com.zerone.shopingtimetest.Adapter.cart_list.OrderDetialsListItemAdapter;
import com.zerone.shopingtimetest.Base64AndMD5.CreateToken;
import com.zerone.shopingtimetest.BaseActivity.BaseAppActivity;
import com.zerone.shopingtimetest.Bean.UserInfo;
import com.zerone.shopingtimetest.Bean.order.GoodsBean;
import com.zerone.shopingtimetest.Bean.print.PrintBean;
import com.zerone.shopingtimetest.Bean.print.PrintItem;
import com.zerone.shopingtimetest.Contants.IpConfig;
import com.zerone.shopingtimetest.DB.impl.UserInfoImpl;
import com.zerone.shopingtimetest.R;
import com.zerone.shopingtimetest.Utils.LoadingUtils;
import com.zerone.shopingtimetest.Utils.NetUtils;
import com.zerone.shopingtimetest.Utils.payutils.PayUtils;
import com.zerone.shopingtimetest.Utils.printutils.PrintUtils;
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

public class OrderDetailsActivity extends BaseAppActivity {
    private ListView listView;
    private UserInfo userInfo;
    private OrderDetailsActivity mContext;
    private ZLoadingDialog loading_dailog;
    private Intent intent;
    private List<GoodsBean> list;
    private OrderDetialsListItemAdapter odlia;
    private LinearLayout qxorder;
    private TextView ordermoney;
    private RelativeLayout subSurePay;
    private List<PrintItem> printItemList;
    private PrintBean printBean;
    private String money;
    private TextView listOrderMoney;
    private TextView ordertime;
    private TextView xiaofeizhe;
    private TextView ordersn;
    private TextView jiedaiyuan;
    private TextView beizhu;
    private ImageView back;
    private Dialog dialog;
    private Dialog dialog1;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String dykJSon = (String) msg.obj;
                    loading_dailog.dismiss();
                    printBean = new PrintBean();
                    printItemList = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(dykJSon);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            //封装打印类 的数据
                            long aLong = jsonObject.getJSONObject("data").getJSONObject("orderdata").getLong("created_at") * 1000;
                            //下单时间
                            Date d = new Date(aLong);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            printBean.setCreateTime(sdf.format(d));
                            printBean.setOrdersn(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("ordersn"));
                            int payStatus = jsonObject.getJSONObject("data").getJSONObject("orderdata").getInt("status");
                            if (payStatus == -1) {
                                printBean.setOrderTuype("取消状态");
                            } else if (payStatus == 0) {
                                printBean.setOrderTuype("待付款");
                            } else if (payStatus == 1) {
                                printBean.setOrderTuype("已付款");
                            }
                            printBean.setPmoney(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("order_price"));
                            printBean.setRemark(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("remarks"));
                            /**
                             *  这个是商品列表
                             */
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("ordergoods");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Item = jsonArray.getJSONObject(i);
                                PrintItem printItem = new PrintItem();
                                printItem.setGcount(Item.getString("total"));
                                printItem.setGoodsname(Item.getString("title"));
                                printItem.setGprice(Item.getString("price"));
                                printItemList.add(printItem);
                            }
                            printBean.setList(printItemList);
                            money = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("order_price");
                            ordermoney.setText("￥" + money);
                            listOrderMoney.setText("￥" + money);
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
                            ordertime.setText(sdf.format(d));
                            ordersn.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("ordersn"));
                            jiedaiyuan.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("realname"));
                            beizhu.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("remarks"));
                        } else if (status == 0) {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        odlia.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    String qxJSOn = (String) msg.obj;
                    Log.i("URL", "qxJSOn==" + qxJSOn);
                    try {
                        JSONObject jsonObject = new JSONObject(qxJSOn);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Toast.makeText(OrderDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, intent);
                            dialog.dismiss();
                            OrderDetailsActivity.this.finish();
                        } else if (status == 0) {
                            Toast.makeText(OrderDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        qxorder.setEnabled(true);
                    }
                    break;
                case 2:
                    /**
                     * {
                     status: "1",
                     msg: "现金付款成功",
                     data: {
                     order_id: "98"
                     }
                     }
                     */
                    String cashJSon = (String) msg.obj;
                    Log.i("URL", "cashJSon=" + cashJSon);
                    try {
                        JSONObject jsonObject = new JSONObject(cashJSon);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Toast.makeText(OrderDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            //吊起打印机
                            if (printBean != null) {
                                if (printBean != null) {
                                    if (userInfo != null) {
                                        if (userInfo.getOrganization_name().length() > 0 && userInfo.getOrganization_name() != null) {
                                            PrintUtils.print(userInfo.getOrganization_name(), printBean);
                                        }
                                    }
                                }
                            }
                            //语音播报
                            VoiceUtils.with(OrderDetailsActivity.this).Play(jsonObject.getJSONObject("data").getString("price"), true);
                            setResult(300, intent);
                            OrderDetailsActivity.this.finish();
                        } else {
                            Toast.makeText(OrderDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        loading_dailog.dismiss();
                        dialog1.dismiss();
                        subSurePay.setEnabled(true);
                    }
                    break;
                case 511:
                    Toast.makeText(OrderDetailsActivity.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    loading_dailog.dismiss();
                    break;
            }
        }
    };
    private RelativeLayout relative_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        mContext = OrderDetailsActivity.this;
        list = new ArrayList<>();
        intent = getIntent();
        initGetUserInfo();
        initView();
        initCheckBoxStates();
        intiAction();
    }

    private void intiAction() {
        qxorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qxorder.setEnabled(false);
                outDialog();
            }
        });
        //打开对话框
        subSurePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subSurePay.setEnabled(false);
                payDialog();
            }
        });
        relative_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsActivity.this.finish();
            }
        });
    }
    /**
     * 初始化view
     */
    private void initView() {
        listOrderMoney = (TextView) findViewById(R.id.listOrderMoney);
        //确认订单按钮
        subSurePay = (RelativeLayout) findViewById(R.id.subSurePay);
        //确认订单的按钮 显示的价格
        ordermoney = (TextView) findViewById(R.id.ordermoney);
        qxorder = (LinearLayout) findViewById(R.id.qxorder);
        listView = (ListView) findViewById(R.id.goodslist);
        odlia = new OrderDetialsListItemAdapter(this, list);
        listView.setAdapter(odlia);
        //======================
        ordertime = (TextView) findViewById(R.id.ordertime);
        xiaofeizhe = (TextView) findViewById(R.id.xiaofeizhe);
        ordersn = (TextView) findViewById(R.id.ordersn);
        jiedaiyuan = (TextView) findViewById(R.id.jiedaiyuan);
        beizhu = (TextView) findViewById(R.id.beizhu);
        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        back = (ImageView) findViewById(R.id.back);
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

    private void initCheckBoxStates() {
        //默认是没有开启的
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> getOrderDetails = new HashMap<String, String>();
        getOrderDetails.put("account_id", userInfo.getAccount_id());
        getOrderDetails.put("organization_id", userInfo.getOrganization_id());
        getOrderDetails.put("order_id", intent.getStringExtra("orderid"));
        getOrderDetails.put("token", token);
        getOrderDetails.put("timestamp", timestamp);
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取订单中。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(mContext, getOrderDetails, IpConfig.URL_ORDERDETAILS, handler, 0);
    }

    /**
     * 自定义对话框
     * 支付对话框
     */
    private void payDialog() {
        dialog1 = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_pay_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        RelativeLayout cashPay = view.findViewById(R.id.cashPay);
        RelativeLayout otherPay = view.findViewById(R.id.otherPay);
        dialog1.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog1.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                subSurePay.setEnabled(true);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                subSurePay.setEnabled(true);
            }
        });
        cashPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击启动现金支付接口调试
                docashPay();

            }
        });

        otherPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调起其他支付方式  盛付通支付
                //吊起支付
                if (money != null) {
                    PayUtils.LiftThePayment(money, OrderDetailsActivity.this);
                    dialog1.dismiss();
                    OrderDetailsActivity.this.finish();
                }
            }
        });
        dialog1.show();
    }

    /**
     * 自定义对话框
     */
    private void outDialog() {
        dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_qx_order_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                subSurePay.setEnabled(true);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消接口
                String timestamp = System.currentTimeMillis() + "";
                String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
                Map<String, String> getOrderDetails = new HashMap<String, String>();
                getOrderDetails.put("account_id", userInfo.getAccount_id());
                getOrderDetails.put("organization_id", userInfo.getOrganization_id());
                getOrderDetails.put("order_id", intent.getStringExtra("orderid"));
                getOrderDetails.put("token", token);
                getOrderDetails.put("timestamp", timestamp);
                loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "取消订单中。。。。");
                loading_dailog.show();
                NetUtils.netWorkByMethodPost(mContext, getOrderDetails, IpConfig.URL_QXORDER, handler, 1);

            }
        });
        dialog.show();
    }

    /**
     * 现金支付方式
     */
    private void docashPay() {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> getOrderDetails = new HashMap<String, String>();
        getOrderDetails.put("account_id", userInfo.getAccount_id());
        getOrderDetails.put("organization_id", userInfo.getOrganization_id());
        getOrderDetails.put("order_id", intent.getStringExtra("orderid"));
        getOrderDetails.put("paytype", "-1");
        getOrderDetails.put("token", token);
        getOrderDetails.put("timestamp", timestamp);
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "现金付款中。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(mContext, getOrderDetails, IpConfig.URL_CASHPAY, handler, 2);
    }
}
