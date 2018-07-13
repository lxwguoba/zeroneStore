package com.zerone_catering.avtivity.manageorderpage.printlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class Print_Order_Check_TableActivity extends BaseActvity {
    @Bind(R.id.btn_return)
    LinearLayout btnReturn;
    private List<TableCatCashierFinal> list;
    private RecyclerView table_category_list;
    private RecycleTableCategoryListAdapter rcpAdapter;
    private Print_Order_Table_Adapter tableAdapter;
    private List<TableListInfoCashierFinal> listtable;
    private Activity mContext;
    private RecyclerView table_det_recycleView;
    private UserInfo userInfo;
    private ZLoadingDialog loading_dailog;
    private String resh = "0";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        if (!Print_Order_Check_TableActivity.this.isFinishing()) {
                            loading_dailog.dismiss();
                        }
                        String tablelistJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(tablelistJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            listtable.clear();
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("tablelist");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                TableListInfoCashierFinal tlicf = new TableListInfoCashierFinal();
                                tlicf.setTable_sort(jsonArray.getJSONObject(i).getInt("table_sort"));
                                tlicf.setTable_name(jsonArray.getJSONObject(i).getString("table_name"));
                                tlicf.setId(jsonArray.getJSONObject(i).getInt("id"));
                                tlicf.setNum(jsonArray.getJSONObject(i).getInt("num"));
                                tlicf.setOrder_num(jsonArray.getJSONObject(i).getInt("order_num"));
                                tlicf.setOrder_unpaid(jsonArray.getJSONObject(i).getInt("order_unpaid"));
                                tlicf.setRoom_id(jsonArray.getJSONObject(i).getInt("room_id"));
                                listtable.add(tlicf);
                            }
                            getTableDataCatAndList();
                        } else if (status == 0) {
                            Toast.makeText(Print_Order_Check_TableActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (!Print_Order_Check_TableActivity.this.isFinishing()) {
                            if (loading_dailog != null) {
                                loading_dailog.dismiss();
                            }
                        }
                    }
                    break;
                case 1:
                    try {
                        String ctableJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(ctableJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            list.clear();
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("roomlist");
                            Map<String, List<TableListInfoCashierFinal>> map = new HashMap<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int id = jsonArray.getJSONObject(i).getInt("id");
                                TableCatCashierFinal tcbf = new TableCatCashierFinal();
                                tcbf.setRoom_sort(jsonArray.getJSONObject(i).getInt("room_sort"));
                                tcbf.setId(id);
                                tcbf.setRoom_name(jsonArray.getJSONObject(i).getString("room_name"));
                                List<TableListInfoCashierFinal> lis = new ArrayList<>();
                                for (int j = 0; j < listtable.size(); j++) {
                                    if (id == listtable.get(j).getRoom_id()) {
                                        lis.add(listtable.get(j));
                                    }
                                }
                                map.put(id + "", lis);
                                tcbf.setMap(map);
                                list.add(tcbf);
                            }
                            listtable.clear();
                            List<TableListInfoCashierFinal> tableListInfoCashierFinals = list.get(0).getMap().get(list.get(0).getId() + "");
                            for (int s = 0; s < tableListInfoCashierFinals.size(); s++) {
                                listtable.add(tableListInfoCashierFinals.get(s));
                            }
                            rcpAdapter.notifyDataSetChanged();
                            rcpAdapter.setCheckPosition(0);
                            tableAdapter.notifyDataSetChanged();
                        } else if (status == 0) {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (!Print_Order_Check_TableActivity.this.isFinishing()) {
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
        setContentView(R.layout.check_table_acticity);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mContext = Print_Order_Check_TableActivity.this;
        userInfo = GetUserInfo.initGetUserInfo(this);
        initData();
        initView();
        insertDataToListView();
    }

    private void initData() {
        list = new ArrayList<>();
        listtable = new ArrayList<>();
        getTableDataList("0");
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("选择桌子打单");
    }

    @OnClick(R.id.btn_return)
    public void onViewClicked() {
        mContext.finish();
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
        if (!"1".equals(resh)) {
            loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取桌子分类。。。。");
            if (!Print_Order_Check_TableActivity.this.isFinishing()) {
                loading_dailog.show();
            }
        }

        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_CASHIERTABLE, handler, 1);
    }

    /**
     * 获取桌的数据
     */
    public void getTableDataList(String str) {
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
        if (!"1".equals(resh)) {
            loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取桌子列表。。。。");
            if (!Print_Order_Check_TableActivity.this.isFinishing()) {
                loading_dailog.show();
            }
        }

        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_CASHIERTABLELIST, handler, 0);
    }

    /**
     * 将数据放入列表中
     */
    private void insertDataToListView() {
        table_category_list = (RecyclerView) findViewById(R.id.table_category_list);
        rcpAdapter = new RecycleTableCategoryListAdapter(list, Print_Order_Check_TableActivity.this);
        table_category_list.setLayoutManager(new LinearLayoutManager(Print_Order_Check_TableActivity.this));
        table_category_list.setAdapter(rcpAdapter);
        rcpAdapter.setCheckPosition(0);
        rcpAdapter.setOnItemClickListener(new RecycleTableCategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rcpAdapter.setCheckPosition(position);
                setUlTableData(position, list.get(position).getId() + "");
                tableAdapter.notifyDataSetChanged();
            }
        });
        table_det_recycleView = (RecyclerView) findViewById(R.id.table_det_recycleView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        tableAdapter = new Print_Order_Table_Adapter(listtable, Print_Order_Check_TableActivity.this);
        table_det_recycleView.setLayoutManager(gridLayoutManager);
        table_det_recycleView.setAdapter(tableAdapter);
        table_det_recycleView.addItemDecoration(new GridSpacingItemDecoration(1, 20, false));
        tableAdapter.setOnItemClickListener(new Print_Order_Table_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(Print_Order_Check_TableActivity.this, Print_Order_List_Activity.class);
                intent.putExtra("tableInfo", listtable.get(position));
                startActivity(intent);
            }
        });
    }

    /**
     * 数据加载
     *
     * @param index
     * @param key
     */
    private void setUlTableData(int index, String key) {
        listtable.clear();
        for (int i = 0; i < list.get(index).getMap().get(key).size(); i++) {
            listtable.add(list.get(index).getMap().get(key).get(i));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 刷新页面的数据
     *
     * @param refreshBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshBean refreshBean) {
        //接收到清空购车的信息了
        if (refreshBean.getRefreshCode() == 100) {
            resh = "1";
            getTableDataList("1");

        }
    }

    /**
     * 关闭页面
     *
     * @param ca
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeActivity(CloseActivity ca) {
        //接收到清空购车的信息了
        if (ca.getCode() == 1000 && "print".equals(ca.getMsg())) {
            Print_Order_Check_TableActivity.this.finish();
        }
    }
}
