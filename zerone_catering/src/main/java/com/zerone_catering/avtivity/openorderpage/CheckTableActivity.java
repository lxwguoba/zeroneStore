package com.zerone_catering.avtivity.openorderpage;

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
import android.widget.Toast;

import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.adapter.RecycleProductCategoryListAdapter;
import com.zerone_catering.adapter.Recycleview_Table_Adapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.colse.CloseActivity;
import com.zerone_catering.domain.tablefinal.TableCatBeanFinal;
import com.zerone_catering.domain.tablefinal.TableInfoBeanFianl;
import com.zerone_catering.recycleview.GridSpacingItemDecoration;
import com.zerone_catering.retrofitIp.CateringIp;
import com.zerone_catering.retrofitIp.ResponseUtils;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.NetworkUtil;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by on 2018/6/14 0014 14 48.
 * Author  LiuXingWen
 * 桌子选择页面
 */

public class CheckTableActivity extends BaseActvity {
    @Bind(R.id.btn_return)
    LinearLayout btnReturn;
    private List<TableCatBeanFinal> tCList;
    private RecyclerView table_category_list;
    private RecycleProductCategoryListAdapter rcpAdapter;
    private Recycleview_Table_Adapter tableAdapter;
    private List<TableInfoBeanFianl> tBList;
    private Context mContext;
    private RecyclerView table_det_recycleView;
    private UserInfo userInfo;
    private ZLoadingDialog loading_dailog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    loading_dailog.dismiss();
                    String tableJson = (String) msg.obj;
                    try {
                        JSONObject tableObject = new JSONObject(tableJson);
                        int status = tableObject.getInt("status");
                        if (status == 1) {
                            JSONArray roomArray = tableObject.getJSONObject("data").getJSONArray("roomlist");
                            for (int i = 0; i < roomArray.length(); i++) {
                                JSONObject jsonObject = roomArray.getJSONObject(i);
                                TableCatBeanFinal tcbf = new TableCatBeanFinal();
                                tcbf.setId(jsonObject.getInt("id"));
                                tcbf.setRoom_name(jsonObject.getString("room_name"));
                                tcbf.setRoom_sort(jsonObject.getInt("room_sort"));
                                JSONArray tablelist = jsonObject.getJSONArray("tablelist");
                                List<TableInfoBeanFianl> tablist = new ArrayList<>();
                                for (int j = 0; j < tablelist.length(); j++) {
                                    JSONObject tab = tablelist.getJSONObject(j);
                                    TableInfoBeanFianl tibf = new TableInfoBeanFianl();
                                    tibf.setId(tab.getInt("id"));
                                    tibf.setRoom_id(tab.getInt("room_id"));
                                    tibf.setTable_name(tab.getString("table_name"));
                                    tibf.setTable_sort(tab.getInt("table_sort"));
                                    tablist.add(tibf);
                                }
                                tcbf.setTableList(tablist);
                                tCList.add(tcbf);
                                tBList.clear();
                                for (int f = 0; f < tCList.get(0).getTableList().size(); f++) {
                                    TableInfoBeanFianl tibf = tCList.get(0).getTableList().get(f);
                                    tBList.add(tibf);
                                }
                            }
                            rcpAdapter.notifyDataSetChanged();
                            tableAdapter.notifyDataSetChanged();
                        } else if (status == 0) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
        mContext = CheckTableActivity.this;
        userInfo = GetUserInfo.initGetUserInfo(this);
        initData();
        initView();
    }

    private void initData() {
        tCList = new ArrayList<>();
        tBList = new ArrayList<>();
//        getTableData();
        retrofit_getTable();
    }

    private void initView() {
        table_category_list = (RecyclerView) findViewById(R.id.table_category_list);
        rcpAdapter = new RecycleProductCategoryListAdapter(tCList, CheckTableActivity.this);
        table_category_list.setLayoutManager(new LinearLayoutManager(CheckTableActivity.this));
        table_category_list.setAdapter(rcpAdapter);
        rcpAdapter.setCheckPosition(0);
        rcpAdapter.setOnItemClickListener(new RecycleProductCategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rcpAdapter.setCheckPosition(position);
                tBList.clear();
                List<TableInfoBeanFianl> tableList = tCList.get(position).getTableList();
                for (int i = 0; i < tableList.size(); i++) {
                    TableInfoBeanFianl tibf = tableList.get(i);
                    tBList.add(tibf);
                }
                tableAdapter.notifyDataSetChanged();
            }
        });
        table_det_recycleView = (RecyclerView) findViewById(R.id.table_det_recycleView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        tableAdapter = new Recycleview_Table_Adapter(tBList, CheckTableActivity.this);
        table_det_recycleView.setLayoutManager(gridLayoutManager);
        table_det_recycleView.setAdapter(tableAdapter);
        table_det_recycleView.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));
        tableAdapter.setOnItemClickListener(new Recycleview_Table_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CheckTableActivity.this, OrderListActvity.class);
                intent.putExtra("tableid", tBList.get(position).getId() + "");
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.btn_return)
    public void onViewClicked() {
        CheckTableActivity.this.finish();
    }

    /**
     * 登陆操作
     */
    private void getTableData() {
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
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取桌子中...");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_TABLE, handler, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 关闭页面
     *
     * @param ca
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeActivity(CloseActivity ca) {
        //接收到清空购车的信息了
        if (ca.getCode() == 1000 && "open".equals(ca.getMsg())) {
            CheckTableActivity.this.finish();
        }
    }


    public void retrofit_getTable() {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> tMap = new HashMap<String, String>();
        tMap.put("account_id", userInfo.getAccount_id());
        tMap.put("timestamp", timestamp);
        tMap.put("organization_id", userInfo.getOrganization_id());
        tMap.put("token", token);
        CateringIp cateringIp = ResponseUtils.getCateringIp();
        Call<ResponseBody> userInfo = cateringIp.getTables(tMap);
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取桌子中...");
        loading_dailog.show();
        userInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Message message = new Message();
                    message.obj = response.body().string();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("URL", call.toString());
            }
        });
    }
}
