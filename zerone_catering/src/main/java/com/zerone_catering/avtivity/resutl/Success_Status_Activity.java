package com.zerone_catering.avtivity.resutl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.qzs.voiceannouncementlibrary.VoiceUtils;
import com.google.gson.Gson;
import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.domain.CashNormalBean;
import com.zerone_catering.domain.PayOrderDetails;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.NetworkUtil;
import com.zerone_catering.utils.printutils.PrintPayUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 2018/6/23 0023 09 49.
 * Author  LiuXingWen
 */

public class Success_Status_Activity extends BaseActvity {
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.success_title)
    TextView successTitle;
    @Bind(R.id.icon_logo)
    RelativeLayout iconLogo;
    @Bind(R.id.store_name)
    TextView storeName;
    @Bind(R.id.store_state_pay)
    TextView storeStatePay;
    @Bind(R.id.next_mactivity)
    Button nextMactivity;
    private Intent intent;
    private CashNormalBean payinfo;
    private UserInfo userInfo;
    private Context mContext;
    private ZLoadingDialog loading_dailog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String nmber = (String) AppSharePreferenceMgr.get(Success_Status_Activity.this, "numberGroup", "1");
                    String orderJSOn = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(orderJSOn);
                        Log.i("URL", orderJSOn);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Gson gson = new Gson();
                            PayOrderDetails payOrderDetails = gson.fromJson(orderJSOn, PayOrderDetails.class);
                            for (int i = 0; i < Integer.parseInt(nmber); i++) {
                                PrintPayUtils.print(userInfo.getOrganization_name(), payOrderDetails, i);
                            }
                            VoiceUtils.with(Success_Status_Activity.this).Play(payOrderDetails.getData().getOrderdata().getPayment_price(), true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!Success_Status_Activity.this.isFinishing()) {
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
        setContentView(R.layout.acitivity_success_pay_page);
        ButterKnife.bind(this);
        mContext = Success_Status_Activity.this;
        intent = getIntent();
        userInfo = GetUserInfo.initGetUserInfo(this);
        payinfo = (CashNormalBean) intent.getSerializableExtra("payinfo");
        initOrderDetailsInfo();
    }

    /**
     * 获取订单详情中
     */
    private void initOrderDetailsInfo() {
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
        tMap.put("order_id", payinfo.getData().getOrder_id());
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取详情中...");
        if (!Success_Status_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_ORDER_MERGE_DETAIL, handler, 0);


    }

    @OnClick({R.id.back, R.id.next_mactivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                Success_Status_Activity.this.finish();
                break;
            case R.id.next_mactivity:
                Success_Status_Activity.this.finish();
                break;
        }
    }
}
