package com.zerone_catering.avtivity.details;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.adapter.cart_list.Order_Detials_ForThePayment_ListItemAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.resutl.Success_Status_Activity;
import com.zerone_catering.domain.CashNormalBean;
import com.zerone_catering.domain.ForThePaymentBean;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.payorderlistbean.OrderCashierListBean;
import com.zerone_catering.domain.refresh.RefreshBean;
import com.zerone_catering.payutils.PayUtils;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.DoubleUtils;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.NetworkUtil;
import com.zerone_catering.utils.UtilsTime;
import com.zerone_catering.utils.view.ListViewSetHightUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/4/2 0002 19 32.
 * Author  LiuXingWen
 */

public class Order_Details_Cashier_ForThePayment_Activity extends BaseActvity {
    private ListView listView;
    private Order_Details_Cashier_ForThePayment_Activity mContext;
    private Intent intent;
    private LinearLayout exit;
    private TextView ordermoney;
    private RelativeLayout subSurePay;
    private String discount_mone;
    private TextView listOrderMoney;
    //下单时间
    private TextView ordertime;
    //消费者
    private TextView customer;
    //订单编号
    private TextView ordersn;
    //接待员
    private TextView receptionist;
    //备注
    private TextView remarks;
    private RelativeLayout relative_back;
    private LinearLayout refresh_data;
    private Dialog dpay;
    private TextView cancel_goods;
    private Order_Detials_ForThePayment_ListItemAdapter codlia;
    private OrderCashierListBean orderInfo;
    private ZLoadingDialog loading_dailog;
    private List<ForThePaymentBean.DataBean.OrdergoodsBean> orderlist;
    private UserInfo userInfo;
    private TextView discount;
    private String order_id;
    private Dialog pay_dialog;
    private Dialog dmsg;
    private String pagestatus;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        loading_dailog.dismiss();

                        String forthepaymentJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(forthepaymentJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Gson gson = new Gson();
                            ForThePaymentBean forThePaymentBean = gson.fromJson(forthepaymentJson, ForThePaymentBean.class);
                            String discount_price = forThePaymentBean.getData().getOrderdata().getDiscount_price();
                            String nmoney = DoubleUtils.setDouble(Double.parseDouble(forThePaymentBean.getData().getOrderdata().getOrder_price()));
                            String dnmoney = DoubleUtils.setDouble(Double.parseDouble(discount_price));


                            if (discount_price.length() > 0) {
                                ordermoney.setText(" ￥：" + DoubleUtils.subMoney(dnmoney));
                            } else {
                                ordermoney.setText(" ￥：" + DoubleUtils.subMoney(nmoney));
                            }
                            listOrderMoney.setText("￥：" + DoubleUtils.subMoney(nmoney));
                            ordertime.setText(UtilsTime.getTime(Long.parseLong(forThePaymentBean.getData().getOrderdata().getCreated_at())));
                            customer.setText(forThePaymentBean.getData().getOrderdata().getNickname());
                            ordersn.setText(forThePaymentBean.getData().getOrderdata().getOrdersn());
                            //接待员
                            receptionist.setText(forThePaymentBean.getData().getOrderdata().getRealname());
                            String remark = forThePaymentBean.getData().getOrderdata().getRemarks();
                            if ("null".equals(remark) && remark.length() > 0) {
                                remarks.setText("没有备注");
                            } else {
                                remarks.setText(remark);
                            }

                            String discounts = forThePaymentBean.getData().getOrderdata().getDiscount();
                            if ("10.00".equals(discounts)) {
                                discount.setText("无折扣");
                            } else {
                                discount.setText(discounts);
                            }
                            orderlist.clear();
                            List<ForThePaymentBean.DataBean.OrdergoodsBean> ordergoods = forThePaymentBean.getData().getOrdergoods();
                            for (int i = 0; i < ordergoods.size(); i++) {
                                orderlist.add(ordergoods.get(i));
                            }
                            ListViewData();
                        } else if (status == 0) {
                            Toast.makeText(Order_Details_Cashier_ForThePayment_Activity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!Order_Details_Cashier_ForThePayment_Activity.this.isFinishing()) {
                            loading_dailog.dismiss();
                        }
                    }
                    break;
                case 1:
                    String docashJson = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(docashJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Gson gson = new Gson();
                            CashNormalBean normalBean = gson.fromJson(docashJson, CashNormalBean.class);
                            Intent intent = new Intent(Order_Details_Cashier_ForThePayment_Activity.this, Success_Status_Activity.class);
                            intent.putExtra("payinfo", normalBean);
                            startActivity(intent);
                            if ("6000".equals(pagestatus)) {
                                EventBus.getDefault().post(new RefreshBean("orderlist", 6000));
                            }
                            Order_Details_Cashier_ForThePayment_Activity.this.finish();
                        } else if (status == 0) {
                            Toast.makeText(Order_Details_Cashier_ForThePayment_Activity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        loading_dailog.dismiss();
                        if (dpay != null) {
                            dpay.dismiss();
                        }
                        if (pay_dialog != null) {
                            pay_dialog.dismiss();
                        }
                    }
                    break;
                case 2:
                    String dms = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(dms);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Toast.makeText(Order_Details_Cashier_ForThePayment_Activity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            if ("6000".equals(pagestatus)) {
                                EventBus.getDefault().post(new RefreshBean("orderlist", 6000));
                            }
                            Order_Details_Cashier_ForThePayment_Activity.this.finish();
                        } else if (status == 0) {
                            Toast.makeText(Order_Details_Cashier_ForThePayment_Activity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (loading_dailog != null) {
                            loading_dailog.dismiss();
                        }

                        if (dmsg != null) {
                            dmsg.dismiss();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_forthepayment);
        mContext = Order_Details_Cashier_ForThePayment_Activity.this;
        orderlist = new ArrayList<>();
        intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        pagestatus = intent.getStringExtra("pagestatus");
        AppSharePreferenceMgr.put(this, "orderid", order_id);
        userInfo = GetUserInfo.initGetUserInfo(this);
        initView();
        getOrderDetailsInfo();
        intiAction();
    }


    private void intiAction() {
        //取消订单
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgDialog();
            }
        });
        //打开对话框
        subSurePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 支付确认 吊起收银页面
                payDialog();
            }
        });
        relative_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order_Details_Cashier_ForThePayment_Activity.this.finish();
            }
        });

        refresh_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderDetailsInfo();
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
        exit = (LinearLayout) findViewById(R.id.exit);
        //======================
        ordertime = (TextView) findViewById(R.id.ordertime);
        customer = (TextView) findViewById(R.id.xiaofeizhe);
        ordersn = (TextView) findViewById(R.id.ordersn);
        receptionist = (TextView) findViewById(R.id.jiedaiyuan);
        remarks = (TextView) findViewById(R.id.beizhu);
        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        refresh_data = (LinearLayout) findViewById(R.id.refresh_data);
        discount = (TextView) findViewById(R.id.discount);
        cancel_goods = (TextView) findViewById(R.id.cancel_goods);
    }

    public void ListViewData() {
        listView = (ListView) findViewById(R.id.goodslist);
        codlia = new Order_Detials_ForThePayment_ListItemAdapter(this, orderlist);
        listView.setAdapter(codlia);
        ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
    }

    /**
     * 获取订单详情
     */
    public void getOrderDetailsInfo() {
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
        if (!Order_Details_Cashier_ForThePayment_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_ORDER_MERGE_DETAIL, handler, 0);
    }

    /**
     * 自定义对话框
     * 支付对话框
     */
    private void payDialog() {
        pay_dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_pay_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        RelativeLayout cashPay = view.findViewById(R.id.cashPay);
        RelativeLayout otherPay = view.findViewById(R.id.otherPay);
        pay_dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        pay_dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_dialog.dismiss();
                subSurePay.setEnabled(true);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_dialog.dismiss();
                subSurePay.setEnabled(true);
            }
        });
        cashPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_dialog.dismiss();
                spayDialog();
            }
        });
        otherPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调起其他支付方式  盛付通支付
                //吊起支付
                discount_mone = "0.01";
                if (discount_mone != null) {
                    PayUtils.LiftThePayment(discount_mone, Order_Details_Cashier_ForThePayment_Activity.this);
                    pay_dialog.dismiss();
                    Order_Details_Cashier_ForThePayment_Activity.this.finish();
                }
            }
        });
        pay_dialog.show();
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

    private void msgDialog() {
        dmsg = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_msg_view, null);
        TextView cancel = view.findViewById(R.id.ordersure_cancel);
        TextView confirm = view.findViewById(R.id.ordersure_confirm);
        TextView msg = view.findViewById(R.id.msg);
        msg.setText("取消该订单吗？");
        dmsg.setContentView(view);
        //使得点击对话框外部不消失对话框
        dmsg.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dmsg.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用取消接口
                deleteOrderDetails();

            }
        });

        dmsg.show();
    }

    /**
     * 取消订单接口
     */
    private void deleteOrderDetails() {
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
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "取消订单中...");
        if (!Order_Details_Cashier_ForThePayment_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_CANCEL_ORDER_MERGE, handler, 2);

    }

    /**
     * 现金支付接口
     */
    private void docashPay() {
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
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "支付中...");
        if (!Order_Details_Cashier_ForThePayment_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_CASH_PAYMENT, handler, 1);
    }

}
