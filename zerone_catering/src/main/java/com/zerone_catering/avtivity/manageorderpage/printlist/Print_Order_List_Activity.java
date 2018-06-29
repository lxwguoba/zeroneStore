package com.zerone_catering.avtivity.manageorderpage.printlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.adapter.Print_Order_Item_ListAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.details.Order_Details_Print_Activity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.payorderlistbean.OrderCashierListBean;
import com.zerone_catering.domain.tablefinal.cashiertable.TableListInfoCashierFinal;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.NetworkUtil;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 2018/6/14 0014 15 06.
 * Author  LiuXingWen
 * 这个是打印管理的list列表
 */

public class Print_Order_List_Activity extends BaseActvity {
    @Bind(R.id.btn_return)
    LinearLayout btnReturn;
    private RecyclerView order_list;
    private Print_Order_Item_ListAdapter oadaper;
    private List<OrderCashierListBean> list;
    private Activity mContext;
    private UserInfo userInfo;
    private ZLoadingDialog loading_dailog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String orderlistJson = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(orderlistJson);
                        Log.i("URL", jsonObject.toString());
                        int status = jsonObject.getInt("status");
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
                            setRecycleView();
                        } else if (status == 0) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (!mContext.isFinishing()) {
                            loading_dailog.dismiss();
                        }
                    }
                    break;
                case 1:
                    OrderCashierListBean oclb = (OrderCashierListBean) msg.obj;
                    Intent intent = new Intent(Print_Order_List_Activity.this, Order_Details_Print_Activity.class);
                    intent.putExtra("orderInfo", oclb);
                    startActivity(intent);
                    break;
            }
        }
    };
    private TableListInfoCashierFinal tableInfo;
    private TextView table_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_order_list_activity);
        ButterKnife.bind(this);
        userInfo = GetUserInfo.initGetUserInfo(this);
        mContext = Print_Order_List_Activity.this;
        tableInfo = (TableListInfoCashierFinal) getIntent().getSerializableExtra("tableInfo");
        list = new ArrayList<>();
        initView();
        initData();

    }

    /**
     * 订单列表
     */
    private void initView() {
        table_name = (TextView) findViewById(R.id.table_name);
        table_name.setText("桌位：" + tableInfo.getTable_name());
    }

    private void setRecycleView() {
        oadaper = new Print_Order_Item_ListAdapter(list, this, handler);
        order_list = (RecyclerView) findViewById(R.id.order_list);
        order_list.setLayoutManager(new LinearLayoutManager(mContext));
        //添加Android自带的分割线
        order_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        order_list.setAdapter(oadaper);
    }

    @OnClick(R.id.btn_return)
    public void onViewClicked() {
        mContext.finish();
    }

    /**
     * 获取订单列表信息
     */
    private void initData() {
        list = new ArrayList<>();
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
        tMap.put("table_id", tableInfo.getId() + "");
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取订单中...");
        if (!mContext.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_CASHIER_ORDER_LIST, handler, 0);
    }
}
