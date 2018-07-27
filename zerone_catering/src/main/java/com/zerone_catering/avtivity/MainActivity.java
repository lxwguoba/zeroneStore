package com.zerone_catering.avtivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.cashierpage.Have_Order_Check_TableActivity;
import com.zerone_catering.avtivity.loginPage.Login_Activity;
import com.zerone_catering.avtivity.manageorderpage.orderlist.TheOrderListActivity;
import com.zerone_catering.avtivity.manageorderpage.printlist.Print_Order_Check_TableActivity;
import com.zerone_catering.avtivity.openorderpage.CheckTableActivity;
import com.zerone_catering.avtivity.system.SystemSettingsActivity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.colse.CloseActivity;
import com.zerone_catering.service.PushNotificationService;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActvity {

    //声明一个操作常量字符串
    public static final String ACTION_UPDATEUI = "action.updateUI";
    //声明一个广播实例
    public UpdateUIBroadcastReceiver broadcastReceiver;
    //==============================语音============================

    @Bind(R.id.btn_main_open_order)
    RelativeLayout btnMainOpenOrder;
    @Bind(R.id.btn_main_set_system)
    RelativeLayout btnMainSetSystem;
    @Bind(R.id.btn_main_order_manage)
    RelativeLayout btnMainOrderManage;
    @Bind(R.id.main_order_count)
    TextView mainOrderCount;
    @Bind(R.id.out)
    ImageView out;
    private Dialog cashier_dialog;
    private UserInfo userInfo;
    private int ocount = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        String countJson = (String) msg.obj;
                        Log.i("URL", "countJosn=" + countJson);
                        JSONObject jsonObject = new JSONObject(countJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            int anInt = jsonObject.getJSONObject("data").getInt("num");
                            if (anInt > 0) {
                                mainOrderCount.setVisibility(View.VISIBLE);
                                mainOrderCount.setText("" + anInt);
                            } else {
                                mainOrderCount.setVisibility(View.GONE);
                                mainOrderCount.setText("");
                            }
                            boolean lean = (boolean) AppSharePreferenceMgr.get(MainActivity.this, "speak", true);
                            //语音播报
                            if (anInt == 0) {
                            } else if (anInt == ocount) {
                                //没有新的订单
                            } else {
                                ocount = anInt;
                                if (lean) {
                                    speechUtil.speak(ocount + "");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        userInfo = GetUserInfo.initGetUserInfo(this);
        startService();

    }

    private void startService() {
        //启动服务
        Intent i = new Intent(this, PushNotificationService.class);
        //下面写自己的路径
        i.setAction("com.zerone_catering.service.PushNotificationService");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startService(i);
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATEUI);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
    }

    /**
     * 自定义对话框
     * 这个是点单管理页面
     */
    private void orderDialog() {
        cashier_dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_cashier_view, null);
        Button pbtn = view.findViewById(R.id.btn_pop_order_print_list);
        Button pbtnlist = view.findViewById(R.id.btn_pop_order_list);
        Button pqx = view.findViewById(R.id.quxiao);
        cashier_dialog.setContentView(view);
        pbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Print_Order_Check_TableActivity.class);
                startActivity(intent);
                cashier_dialog.dismiss();
            }
        });
        pbtnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TheOrderListActivity.class);
                startActivity(intent);
                cashier_dialog.dismiss();
            }
        });
        pqx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashier_dialog.dismiss();
            }
        });


        cashier_dialog.show();
    }

    /**
     * 自定义对话框
     */
    private void out_Activity_Dialog() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_out_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.removeALLActivity();
                System.exit(0);
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btn_main_open_order, R.id.btn_main_collect_money, R.id.btn_main_set_system, R.id.btn_main_order_manage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_open_order:
                //开单
                Intent intent = new Intent(MainActivity.this, CheckTableActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_main_collect_money:
                //收银
                Intent intent01 = new Intent(MainActivity.this, Have_Order_Check_TableActivity.class);
                startActivity(intent01);
                break;
            case R.id.btn_main_set_system:
                //系统设置
                Intent intent02 = new Intent(MainActivity.this, SystemSettingsActivity.class);
                startActivity(intent02);
                break;
            case R.id.btn_main_order_manage:
                //修改为订单管理
                orderDialog();
                break;
        }
    }

    /**
     * 点击返回键后跳出退出对话框
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //如果点击的是后退键  首先判断webView是否能够后退
            //如果点击的是后退键  首先判断webView是否能够后退   返回值是boolean类型的
            out_Activity_Dialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 获取新的订单数
     */
    private void goto_Get_Order_Number() {
        try {
            String timestamp = System.currentTimeMillis() + "";
            String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
            Map<String, String> kdMap = new HashMap<String, String>();
            kdMap.put("account_id", userInfo.getAccount_id());
            kdMap.put("organization_id", userInfo.getOrganization_id());
            kdMap.put("token", token);
            kdMap.put("timestamp", timestamp);
            NetUtils.netWorkByMethodPost(MainActivity.this, kdMap, IpConfig.URL_ORDER_NEWS, handler, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.out)
    public void onViewClicked() {
        out_Activity_Dialog();
    }

    /**
     * 关闭页面
     *
     * @param closeActivity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeActivity(CloseActivity closeActivity) {
        if (closeActivity.getCode() == 748) {
            Intent intent = new Intent(MainActivity.this, Login_Activity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, closeActivity.getMsg(), Toast.LENGTH_SHORT).show();
            MainActivity.this.removeALLActivity();
        }
    }

    /**
     * 这个是用来接收服务器跑来的数据更新UI
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra("code", 0);
            if (code == 1) {
                goto_Get_Order_Number();
            } else if (code == 0) {
            }
        }
    }
}
