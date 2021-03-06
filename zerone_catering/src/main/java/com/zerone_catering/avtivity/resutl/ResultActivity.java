package com.zerone_catering.avtivity.resutl;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.DB.impl.UserInfoImpl;
import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.domain.PayOrderDetails;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.refresh.RefreshBean;
import com.zerone_catering.print.PrintItem;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.printutils.PrintPayUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private UserInfo userInfo;
    private ZLoadingDialog loading_dailog;
    private ResultActivity mContext;
    Handler handler = new Handler() {
        private List<PrintItem> printItemList;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    loading_dailog.dismiss();
                    String upDataStates = (String) msg.obj;
                    Log.i("URL", upDataStates);
                    try {
                        JSONObject jsonObject = new JSONObject(upDataStates);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String order_id = jsonObject.getJSONObject("data").getString("order_id");
                            gotoPrint(order_id);
                            EventBus.getDefault().post(new RefreshBean("orderlist", 6000));
                        } else if (status == 0) {
                            Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        AppSharePreferenceMgr.remove(ResultActivity.this, "orderid");
                    }
                    break;
                case 2:
                    String orderJSOn = (String) msg.obj;
                    String nmber = (String) AppSharePreferenceMgr.get(ResultActivity.this, "numberGroup", "1");
                    try {
                        JSONObject jsonObject = new JSONObject(orderJSOn);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Gson gson = new Gson();
                            PayOrderDetails payOrderDetails = gson.fromJson(orderJSOn, PayOrderDetails.class);
                            //语音播报
                            speechUtil.speakMone(payOrderDetails.getData().getOrderdata().getPayment_price());
                            for (int i = 0; i < Integer.parseInt(nmber); i++) {
                                PrintPayUtils.print(userInfo.getOrganization_name(), payOrderDetails, i);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (loading_dailog != null) {
                            loading_dailog.dismiss();
                        }
                    }
                    break;
                case 511:
                    Toast.makeText(ResultActivity.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    loading_dailog.dismiss();
                    break;
            }
        }
    };
    private String orderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mContext = this;
        initGetUserInfo();
        initView();
        action();
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
        result_tv = (TextView) findViewById(R.id.result_tv);
        result_iv = (ImageView) findViewById(R.id.result_iv);
        result_info = (TextView) findViewById(R.id.result_info);
        pay_complete = (TextView) findViewById(R.id.pay_complete);
        int resultCode = getIntent().getIntExtra("resultCode", -1);
        String errorMsg = getIntent().getStringExtra("errorMsg");
        String answerCode = getIntent().getStringExtra("answerCode");
        String resultInfo = getIntent().getStringExtra("resultInfo");
        paymentType = getIntent().getIntExtra("paymentType", 10);
        String money = getIntent().getStringExtra("money");
        errorCode = getIntent().getIntExtra("errorCode", 0);
        if (resultCode == 0) {
            // 交易成功
            result_info.setText("支付成功！");
//            double dmoney = Double.parseDouble(money) / 100;
//            //语音播报
//            VoiceUtils.with(this).Play(dmoney + "", true);
            //打印小票  获取订单详情
            goUpDataOrderState();
        } else if (resultCode == -1) {
            // 交易失败
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            Log.e(TAG, errorMsg);
            result_info.setTextColor(Color.parseColor("#ff0000"));
            result_iv.setImageResource(R.mipmap.pay_fails);
            result_info.setText(errorMsg);
            Log.i("BBB", "resultInfo=" + resultInfo);
        }
    }

    /**
     * 获取订单详情打印
     *
     * @param orderid
     */
    private void gotoPrint(String orderid) {
        if (userInfo == null) {
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> getOrderDetails = new HashMap<String, String>();
        getOrderDetails.put("account_id", userInfo.getAccount_id());
        getOrderDetails.put("organization_id", userInfo.getOrganization_id());
        getOrderDetails.put("order_id", orderid);
        getOrderDetails.put("token", token);
        getOrderDetails.put("timestamp", timestamp);
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取订单中。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(mContext, getOrderDetails, IpConfig.URL_ORDER_MERGE_DETAIL, handler, 2);
    }

    /**
     * 发送数据给服务器修改订单状态
     */
    private void goUpDataOrderState() {
        if (userInfo == null) {
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> pyMap = new HashMap<String, String>();
        pyMap.put("organization_id", userInfo.getOrganization_id());
        pyMap.put("account_id", userInfo.getAccount_id());
        pyMap.put("token", token);
        String orderid = (String) AppSharePreferenceMgr.get(ResultActivity.this, "orderid", "");
        if (orderid != null && orderid.length() > 0) {
            pyMap.put("order_id", orderid);
        }
        pyMap.put("paytype", "5");
        pyMap.put("timestamp", timestamp);
        pyMap.put("payment_company", "乐刷支付");
        loading_dailog = LoadingUtils.getDailog(ResultActivity.this, Color.RED, "修改中...");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(ResultActivity.this, pyMap, IpConfig.URL_OTHER_PAYMENT, handler, 0);
    }

    /**
     * 获取用户信息
     */
    private void initGetUserInfo() {
        UserInfoImpl userInfoImpl = new UserInfoImpl(ResultActivity.this);
        try {
            userInfo = userInfoImpl.getUserInfo("10");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
