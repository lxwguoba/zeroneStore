package com.zerone_catering.avtivity.cashierpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.adapter.Print_Order_Table_Adapter;
import com.zerone_catering.adapter.shopplistadapter.RecycleTableCategoryListAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.colse.CloseActivity;
import com.zerone_catering.domain.refresh.RefreshBean;
import com.zerone_catering.domain.tablefinal.cashiertable.TableCatCashierFinal;
import com.zerone_catering.domain.tablefinal.cashiertable.TableListInfoCashierFinal;
import com.zerone_catering.recycleview.GridSpacingItemDecoration;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 2018/6/14 0014 14 48.
 * Author  LiuXingWen
 * 桌子选择页面
 */

public class Have_Order_Check_TableActivity extends BaseActvity {
    @Bind(R.id.btn_return)
    LinearLayout btnReturn;
    private RecyclerView table_category_list;
    private RecycleTableCategoryListAdapter rcpAdapter;
    private Print_Order_Table_Adapter tableAdapter;
    private Context mContext;
    private RecyclerView table_det_recycleView;
    private UserInfo userInfo;
    private ZLoadingDialog loading_dailog;
    private List<TableCatCashierFinal> ctablelist;
    private List<TableListInfoCashierFinal> ctinfolist;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        if (loading_dailog != null) {
                            loading_dailog.dismiss();
                        }
                        String tablelistJson = (String) msg.obj;
                        Log.i("URL", "tablelistJson=" + tablelistJson);
                        JSONObject jsonObject = new JSONObject(tablelistJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            ctinfolist.clear();
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("tablelist");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                TableListInfoCashierFinal tlicf = new TableListInfoCashierFinal();
                                tlicf.setTable_sort(jsonArray.getJSONObject(i).getInt("table_sort"));
                                tlicf.setTable_name(jsonArray.getJSONObject(i).getString("table_name"));
                                tlicf.setId(jsonArray.getJSONObject(i).getInt("id"));
                                tlicf.setOrder_num(jsonArray.getJSONObject(i).getInt("order_num"));
                                tlicf.setOrder_unpaid(jsonArray.getJSONObject(i).getInt("order_unpaid"));
                                tlicf.setRoom_id(jsonArray.getJSONObject(i).getInt("room_id"));
                                ctinfolist.add(tlicf);
                            }
                            getTableDataCatAndList();
                        } else if (status == 0) {
                            Toast.makeText(Have_Order_Check_TableActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {

                    }
                    break;
                case 1:
                    try {
                        String ctableJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(ctableJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            ctablelist.clear();
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("roomlist");
                            Map<String, List<TableListInfoCashierFinal>> map = new HashMap<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int id = jsonArray.getJSONObject(i).getInt("id");
                                TableCatCashierFinal tcbf = new TableCatCashierFinal();
                                tcbf.setRoom_sort(jsonArray.getJSONObject(i).getInt("room_sort"));
                                tcbf.setId(id);
                                tcbf.setRoom_name(jsonArray.getJSONObject(i).getString("room_name"));
                                List<TableListInfoCashierFinal> list = new ArrayList<>();
                                for (int j = 0; j < ctinfolist.size(); j++) {
                                    if (id == ctinfolist.get(j).getRoom_id()) {
                                        list.add(ctinfolist.get(j));
                                    }
                                }
                                map.put(id + "", list);
                                tcbf.setMap(map);
                                ctablelist.add(tcbf);
                            }
                            ctinfolist.clear();
                            List<TableListInfoCashierFinal> tableListInfoCashierFinals = ctablelist.get(0).getMap().get(ctablelist.get(0).getId() + "");
                            for (int s = 0; s < tableListInfoCashierFinals.size(); s++) {
                                ctinfolist.add(tableListInfoCashierFinals.get(s));
                            }
                            tableAdapter.notifyDataSetChanged();
                            rcpAdapter.setCheckPosition(0);
                            rcpAdapter.notifyDataSetChanged();
                        } else if (status == 0) {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        loading_dailog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_table_acticity);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mContext = Have_Order_Check_TableActivity.this;
        userInfo = GetUserInfo.initGetUserInfo(this);
        initView();
        initData();
    }

    private void initData() {
        getTableDataList();
    }

    private void setUlTableData(int index, String key) {
        ctinfolist.clear();
        List<TableListInfoCashierFinal> tableListInfoCashierFinals = ctablelist.get(index).getMap().get(key);
        for (int i = 0; i < ctablelist.get(index).getMap().get(key).size(); i++) {
            ctinfolist.add(ctablelist.get(index).getMap().get(key).get(i));
        }
    }

    private void initView() {
        ctablelist = new ArrayList<>();
        ctinfolist = new ArrayList<>();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("选择桌子收银");
        insertDataToListView();
    }

    @OnClick(R.id.btn_return)
    public void onViewClicked() {
        Have_Order_Check_TableActivity.this.finish();
    }

    /**
     * 获取桌子的分类和列表
     */
    public void getTableDataCatAndList() {
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
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取桌子分类...");
        if (!Have_Order_Check_TableActivity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_CASHIERTABLE, handler, 1);
    }

    /**
     * 获取桌的数据
     */
    public void getTableDataList() {

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
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取桌子列表...");
        if (!Have_Order_Check_TableActivity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_CASHIERTABLELIST, handler, 0);
    }

    /**
     * 将数据放入列表中
     */
    private void insertDataToListView() {
        table_category_list = (RecyclerView) findViewById(R.id.table_category_list);
        rcpAdapter = new RecycleTableCategoryListAdapter(ctablelist, Have_Order_Check_TableActivity.this);
        table_category_list.setLayoutManager(new LinearLayoutManager(Have_Order_Check_TableActivity.this));
        table_category_list.setAdapter(rcpAdapter);
        rcpAdapter.setCheckPosition(0);
        rcpAdapter.setOnItemClickListener(new RecycleTableCategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rcpAdapter.setCheckPosition(position);
                setUlTableData(position, ctablelist.get(position).getId() + "");
                tableAdapter.notifyDataSetChanged();
            }
        });
        table_det_recycleView = (RecyclerView) findViewById(R.id.table_det_recycleView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        tableAdapter = new Print_Order_Table_Adapter(ctinfolist, Have_Order_Check_TableActivity.this);
        table_det_recycleView.setLayoutManager(gridLayoutManager);
        table_det_recycleView.setAdapter(tableAdapter);
        table_det_recycleView.addItemDecoration(new GridSpacingItemDecoration(1, 20, false));
        tableAdapter.setOnItemClickListener(new Print_Order_Table_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(Have_Order_Check_TableActivity.this, Have_Cashier_OrderList_Activity.class);
                intent.putExtra("talbe", ctinfolist.get(position));
                startActivity(intent);
                Have_Order_Check_TableActivity.this.finish();
            }
        });
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
            Have_Order_Check_TableActivity.this.finish();
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
//            getTableDataList();
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
