package com.zerone_catering.avtivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.cashierpage.Have_Order_Check_TableActivity;
import com.zerone_catering.avtivity.manageorderpage.orderlist.TheOrderListActivity;
import com.zerone_catering.avtivity.manageorderpage.printlist.Print_Order_Check_TableActivity;
import com.zerone_catering.avtivity.openorderpage.CheckTableActivity;
import com.zerone_catering.avtivity.system.SystemSettingsActivity;
import com.zerone_catering.domain.push.OrderBeanPush;
import com.zerone_catering.service.PushNotificationService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActvity {

    //声明一个操作常量字符串
    public static final String ACTION_UPDATEUI = "action.updateUI";
    //声明一个广播实例
    public UpdateUIBroadcastReceiver broadcastReceiver;
    @Bind(R.id.btn_main_open_order)
    LinearLayout btnMainOpenOrder;
    @Bind(R.id.btn_main_collect_money)
    LinearLayout btnMainCollectMoney;
    @Bind(R.id.btn_main_set_system)
    LinearLayout btnMainSetSystem;
    @Bind(R.id.btn_main_order_manage)
    LinearLayout btnMainOrderManage;
    @Bind(R.id.btn_main_out_app)
    LinearLayout btnMainOutApp;
    private Dialog cashier_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        startService();
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
    }

    @OnClick({R.id.btn_main_open_order, R.id.btn_main_collect_money, R.id.btn_main_set_system, R.id.btn_main_order_manage, R.id.btn_main_out_app})
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
            case R.id.btn_main_out_app:
                //注销app
                out_Activity_Dialog();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 这个是用来接收服务器跑来的数据更新UI
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            OrderBeanPush orderBeanPush = (OrderBeanPush) intent.getSerializableExtra("bean");
            if (orderBeanPush.getOnum() > 0) {

            }
        }
    }
}
