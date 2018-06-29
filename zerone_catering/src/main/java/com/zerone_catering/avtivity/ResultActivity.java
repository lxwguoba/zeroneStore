package com.zerone_catering.avtivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zerone_catering.avtivity.BaseSet.BaseActvity;

/**
 * @author xurong on 2017/5/15.
 */

public class ResultActivity extends BaseActvity {

    private static final String TAG = "ResultActivity";

    TextView result_tv;
    private ImageView result_iv;
    private TextView result_info;
    private int errorCode;
    private TextView pay_complete;
    private int paymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_result);
//        mContext = this;
//        initGetUserInfo();
//        initView();
//        action();
    }

    private void action() {
        pay_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里需要更新下收银台颜色
                ResultActivity.this.finish();
            }
        });
    }

    private void initView() {
//        result_tv = (TextView) findViewById(R.id.result_tv);
//        result_iv = (ImageView) findViewById(R.id.result_iv);
//        result_info = (TextView) findViewById(R.id.result_info);
//        pay_complete = (TextView) findViewById(R.id.pay_complete);
//        int resultCode = getIntent().getIntExtra("resultCode", -1);
//        String errorMsg = getIntent().getStringExtra("errorMsg");
//        String answerCode = getIntent().getStringExtra("answerCode");
//        String resultInfo = getIntent().getStringExtra("resultInfo");
//        paymentType = getIntent().getIntExtra("paymentType", 10);
//        String money = getIntent().getStringExtra("money");
//        errorCode = getIntent().getIntExtra("errorCode", 0);
//        if (resultCode == 0) {
//            // 交易成功
//            result_info.setText("支付成功！");
//            double dmoney = Double.parseDouble(money) / 100;
//            //语音播报
//            VoiceUtils.with(this).Play(dmoney + "", true);
//            //打印小票  获取订单详情
//            goUpDataOrderState();
//        } else if (resultCode == -1) {
//            // 交易失败
//            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
//            Log.e(TAG, errorMsg);
//            result_info.setTextColor(Color.parseColor("#ff0000"));
//            result_iv.setImageResource(R.mipmap.pay_fails);
//            result_info.setText(errorMsg);
//            Log.i("BBB", "resultInfo=" + resultInfo);
//        }
//    }
//
//    /**
//     * 获取订单详情打印
//     *
//     * @param orderid
//     */
//    private void gotoPrint(String orderid) {
//        if (userInfo == null) {
//            return;
//        }
//        String timestamp = System.currentTimeMillis() + "";
//        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
//        Map<String, String> getOrderDetails = new HashMap<String, String>();
//        getOrderDetails.put("account_id", userInfo.getAccount_id());
//        getOrderDetails.put("organization_id", userInfo.getOrganization_id());
//        getOrderDetails.put("order_id", orderid);
//        getOrderDetails.put("token", token);
//        getOrderDetails.put("timestamp", timestamp);
//        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取订单中。。。。");
//        loading_dailog.show();
//        NetUtils.netWorkByMethodPost(mContext, getOrderDetails, IpConfig.URL_ORDERDETAILS, handler, 2);
//
//    }
//
//    /**
//     * 发送数据给服务器修改订单状态
//     */
//    private void goUpDataOrderState() {
//        if (userInfo == null) {
//            return;
//        }
//
//        String timestamp = System.currentTimeMillis() + "";
//        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
//        Map<String, String> loginMap = new HashMap<String, String>();
//        loginMap.put("organization_id", userInfo.getOrganization_id());
//        loginMap.put("account_id", userInfo.getAccount_id());
//        loginMap.put("token", token);
//        String orderid = (String) AppSharePreferenceMgr.get(ResultActivity.this, "orderid", "");
//        if (orderid != null && orderid.length() > 0) {
//            loginMap.put("order_id", orderid);
//        }
//        loginMap.put("paytype", "5");
//        loginMap.put("timestamp", timestamp);
//        loginMap.put("payment_company", "乐刷支付");
//        loading_dailog = LoadingUtils.getDailog(ResultActivity.this, Color.RED, "修改中...");
//        loading_dailog.show();
//        NetUtils.netWorkByMethodPost(ResultActivity.this, loginMap, IpConfig.URL_UPDATAPAY, handler, 0);
//    }
//
//    /**
//     * 获取用户信息
//     */
//    private void initGetUserInfo() {
//        UserInfoImpl userInfoImpl = new UserInfoImpl(ResultActivity.this);
//        try {
//            userInfo = userInfoImpl.getUserInfo("10");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
