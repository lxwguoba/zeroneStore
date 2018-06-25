package com.zerone.store.shopingtimetest.Activity.details;

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
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.zerone.store.shopingtimetest.Activity.resutl.Success_Status_Activity;
import com.zerone.store.shopingtimetest.Adapter.cart_list.OrderDetialsListItemAdapter;
import com.zerone.store.shopingtimetest.Base64AndMD5.CreateToken;
import com.zerone.store.shopingtimetest.BaseActivity.BaseAppActivity;
import com.zerone.store.shopingtimetest.Bean.UserInfo;
import com.zerone.store.shopingtimetest.Bean.order.GoodsBean;
import com.zerone.store.shopingtimetest.Bean.print.PrintBean;
import com.zerone.store.shopingtimetest.Bean.print.PrintItem;
import com.zerone.store.shopingtimetest.Contants.IpConfig;
import com.zerone.store.shopingtimetest.DB.impl.UserInfoImpl;
import com.zerone.store.shopingtimetest.R;
import com.zerone.store.shopingtimetest.Utils.AppSharePreferenceMgr;
import com.zerone.store.shopingtimetest.Utils.LoadingUtils;
import com.zerone.store.shopingtimetest.Utils.NetUtils;
import com.zerone.store.shopingtimetest.Utils.payutils.PayUtils;
import com.zerone.store.shopingtimetest.Utils.printutils.PrintUtils;
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

public class OrderDetailsDFKActivity extends BaseAppActivity {
    private ListView listView;
    private UserInfo userInfo;
    private OrderDetailsDFKActivity mContext;
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
    private String discount_mone;
    private TextView listOrderMoney;
    private TextView ordertime;
    private TextView xiaofeizhe;
    private TextView ordersn;
    private TextView jiedaiyuan;
    private TextView beizhu;
    private ImageView back;
    private Dialog dialog;
    private Dialog dialog1;
    private RelativeLayout relative_back;
    private TextView discount_price;
    private String orderid;
    private LinearLayout refresh_data;
    private Dialog dpay;
    private TextView discount;
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
                        Log.i("URL", jsonObject.toString());
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
                            printBean.setPayment_price(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("discount_price"));
                            printBean.setDiscount(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("discount"));
                            printBean.setList(printItemList);
                            money = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("order_price");
                            listOrderMoney.setText("￥" + money);
                            discount_mone = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("discount_price");
                            if ("10.00".equals(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("discount"))) {
                                discount.setText("无折扣");
                            } else {
                                discount.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("discount") + "折");
                            }

                            discount_price.setText("￥" + jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("discount_price"));
                            ordermoney.setText("￥" + jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("discount_price"));
                            int userid = jsonObject.getJSONObject("data").getJSONObject("orderdata").getInt("user_id");
                            if (userid == 0) {
                                xiaofeizhe.setText("散客");
                            } else {
                                xiaofeizhe.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("nickname"));
                            }
                            list.clear();
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
                            if ("null".equals(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("remarks"))) {
                                beizhu.setText("");
                                printBean.setRemark("");
                            } else {
                                beizhu.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("remarks"));
                                printBean.setRemark(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("remarks"));
                            }
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
                    try {
                        JSONObject jsonObject = new JSONObject(qxJSOn);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Toast.makeText(OrderDetailsDFKActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, intent);
                            dialog.dismiss();
                            OrderDetailsDFKActivity.this.finish();
                        } else if (status == 0) {
                            Toast.makeText(OrderDetailsDFKActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        qxorder.setEnabled(true);
                        if (loading_dailog != null) {
                            loading_dailog.dismiss();
                        }
                    }
                    break;
                case 2:
                    String cashJSon = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(cashJSon);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Toast.makeText(OrderDetailsDFKActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            //吊起打印机
                            if (printBean != null) {
                                if (printBean != null) {
                                    if (userInfo != null) {
                                        if (userInfo.getOrganization_name().length() > 0 && userInfo.getOrganization_name() != null) {
                                            PrintUtils.print(OrderDetailsDFKActivity.this, userInfo.getOrganization_name(), printBean);
                                        }
                                    }
                                }
                            }
                            //语音播报
                            VoiceUtils.with(OrderDetailsDFKActivity.this).Play(jsonObject.getJSONObject("data").getString("payment_price"), true);
                            setResult(300, intent);
                            Intent intent = new Intent(OrderDetailsDFKActivity.this, Success_Status_Activity.class);
                            startActivity(intent);
                            OrderDetailsDFKActivity.this.finish();
                        } else {
                            Toast.makeText(OrderDetailsDFKActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (loading_dailog != null) {
                            loading_dailog.dismiss();
                        }
                        if (dpay != null) {
                            dpay.dismiss();
                        }
                        subSurePay.setEnabled(true);
                    }
                    break;
                case 511:
                    VolleyError error = (VolleyError) msg.obj;
                    if (error != null) {
                        if (error instanceof TimeoutError) {
                            Toast.makeText(OrderDetailsDFKActivity.this, "网络请求超时，请重试！", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        if (error instanceof ServerError) {
                            Toast.makeText(OrderDetailsDFKActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        if (error instanceof NetworkError) {
                            Toast.makeText(OrderDetailsDFKActivity.this, "请检查网络，或点击右上角的刷新", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        if (error instanceof ParseError) {
                            Toast.makeText(OrderDetailsDFKActivity.this, "数据格式错误", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        Toast.makeText(OrderDetailsDFKActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        loading_dailog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        mContext = OrderDetailsDFKActivity.this;
        list = new ArrayList<>();
        intent = getIntent();
        orderid = intent.getStringExtra("orderid");
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
                if (orderid != null) {
                    if (money != null && money.length() > 0) {
                        AppSharePreferenceMgr.put(OrderDetailsDFKActivity.this, "orderid", orderid);
                        payDialog();
                    } else {
                        Toast.makeText(OrderDetailsDFKActivity.this, "获取订单失败，请刷新页面", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderDetailsDFKActivity.this, "订单不存在哦", Toast.LENGTH_SHORT).show();
                }

            }
        });
        relative_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsDFKActivity.this.finish();
            }
        });

        refresh_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCheckBoxStates();
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
        discount_price = (TextView) findViewById(R.id.discount_price);
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
        refresh_data = (LinearLayout) findViewById(R.id.refresh_data);
        discount = (TextView) findViewById(R.id.discount);
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
        if (orderid != null) {
            getOrderDetails.put("order_id", orderid);
        } else {
            return;
        }
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
                dialog1.dismiss();
                spayDialog();
            }
        });

        otherPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调起其他支付方式  盛付通支付
                //吊起支付
                if (discount_mone != null) {
                    PayUtils.LiftThePayment(discount_mone, OrderDetailsDFKActivity.this);
                    dialog1.dismiss();
                    OrderDetailsDFKActivity.this.finish();
                }
            }
        });
        dialog1.show();
    }


    /**
     * 自定义对话框
     * 支付对话框
     */
    private void spayDialog() {
        dpay = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_pay_cash_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        RelativeLayout cashPay = view.findViewById(R.id.cashPay);
        RelativeLayout otherPay = view.findViewById(R.id.otherPay);
        dpay.setContentView(view);
        //使得点击对话框外部不消失对话框
        dpay.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpay.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击启动现金支付接口调试
                docashPay();

            }
        });

        dpay.show();
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
