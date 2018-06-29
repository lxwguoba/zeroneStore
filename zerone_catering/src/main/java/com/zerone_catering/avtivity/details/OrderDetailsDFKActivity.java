package com.zerone_catering.avtivity.details;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.adapter.Print_Choose_Item_Adapter;
import com.zerone_catering.adapter.cart_list.CheckCancel_OrderListItemAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.manageorderpage.printlist.PrintResponseActivity;
import com.zerone_catering.domain.PrinterMachine;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.order.Order_Cashier_Details_Bean;
import com.zerone_catering.domain.payorderlistbean.OrderCashierListBean;
import com.zerone_catering.utils.GetUserInfo;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/4/2 0002 19 32.
 * Author  LiuXingWen
 */

public class OrderDetailsDFKActivity extends BaseActvity {
    List<Order_Cashier_Details_Bean> list;
    private ListView listView;
    private OrderDetailsDFKActivity mContext;
    private Intent intent;
    private LinearLayout qxorder;
    private TextView ordermoney;
    private RelativeLayout subSurePay;
    private String money;
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
    private ImageView back;
    private Dialog dialog;
    private RelativeLayout relative_back;
    private LinearLayout refresh_data;
    private Dialog dpay;
    private List<PrinterMachine> printList;
    private TextView cancel_goods;
    private CheckCancel_OrderListItemAdapter codlia;
    private OrderCashierListBean orderInfo;
    private ZLoadingDialog loading_dailog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        String detailsJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(detailsJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
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
                            customer.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("realname"));
                            ordersn.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("ordersn"));
                            receptionist.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("nickname"));
                            remarks.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("remarks"));
                            listOrderMoney.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("order_price") + " 元");
                            codlia.notifyDataSetChanged();
                            ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
                        } else if (status == 0) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!OrderDetailsDFKActivity.this.isFinishing()) {
                            loading_dailog.dismiss();
                        }
                    }
                    break;
            }
        }
    };
    private UserInfo userInfo;
    private TextView discount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_dfk);
        mContext = OrderDetailsDFKActivity.this;
        list = new ArrayList<Order_Cashier_Details_Bean>();
        intent = getIntent();
        orderInfo = (OrderCashierListBean) intent.getSerializableExtra("orderInfo");
        userInfo = GetUserInfo.initGetUserInfo(this);
        initView();
        initDataSet();
        intiAction();
    }

    /**
     * 测试数据
     */
    private void initDataSet() {
        getOrderDetailsInfo();
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
                checkMachine();
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
                getOrderDetailsInfo();
            }
        });


        cancel_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Order_Cashier_Details_Bean> it = list.iterator();
                while (it.hasNext()) {
                    Order_Cashier_Details_Bean cc = it.next();
                    if (cc.getGoods_ckeck()) {
                        it.remove();
                    }
                }
                codlia.notifyDataSetChanged();
                ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
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
        //======================
        ordertime = (TextView) findViewById(R.id.ordertime);
        customer = (TextView) findViewById(R.id.xiaofeizhe);
        ordersn = (TextView) findViewById(R.id.ordersn);
        receptionist = (TextView) findViewById(R.id.jiedaiyuan);
        remarks = (TextView) findViewById(R.id.beizhu);
        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        back = (ImageView) findViewById(R.id.back);
        refresh_data = (LinearLayout) findViewById(R.id.refresh_data);
        discount = (TextView) findViewById(R.id.discount);
        cancel_goods = (TextView) findViewById(R.id.cancel_goods);
        listView = (ListView) findViewById(R.id.goodslist);
        codlia = new CheckCancel_OrderListItemAdapter(this, list);
        listView.setAdapter(codlia);
        ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
    }

    /**
     * 选择需要打印的打印机
     */
    private void checkMachine() {
        dpay = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_check_p_view, null);
        initDataTest();
        RecyclerView TheprinterList = view.findViewById(R.id.TheprinterList);
        final Print_Choose_Item_Adapter pchooseAdapter = new Print_Choose_Item_Adapter(printList, OrderDetailsDFKActivity.this);
        TheprinterList.setLayoutManager(new LinearLayoutManager(OrderDetailsDFKActivity.this));
        TheprinterList.setAdapter(pchooseAdapter);
        pchooseAdapter.setOnItemClickListener(new Print_Choose_Item_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                printList.get(position).setChblen(!printList.get(position).isChblen());
                pchooseAdapter.notifyDataSetChanged();
            }
        });
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
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
                //吊起打印接口成功后跳转到成功页面
                Intent intent = new Intent(OrderDetailsDFKActivity.this, PrintResponseActivity.class);
                startActivity(intent);
                OrderDetailsDFKActivity.this.finish();
                dpay.dismiss();
            }
        });
        dpay.show();
    }

    private void initDataTest() {
        printList = new ArrayList<PrinterMachine>();
        printList.add(new PrinterMachine("前台打印", "12", false));
        printList.add(new PrinterMachine("后厨打印", "12", false));
        printList.add(new PrinterMachine("酒水打印", "12", false));
        printList.add(new PrinterMachine("小菜打印", "12", false));
        printList.add(new PrinterMachine("仓库打印", "12", false));
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
            }
        });
        dialog.show();
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
        tMap.put("order_id", orderInfo.getId() + "");
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取详情中...");
        if (!OrderDetailsDFKActivity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_ORDER_DETAILS, handler, 0);
    }

}
