package com.zerone.store.shopingtimetest.Activity.resutl;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.zerone.store.shopingtimetest.BaseActivity.BaseAppActivity;
import com.zerone.store.shopingtimetest.Bean.UserInfo;
import com.zerone.store.shopingtimetest.DB.impl.UserInfoImpl;
import com.zerone.store.shopingtimetest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 2018/6/23 0023 09 58.
 * Author  LiuXingWen
 */

public class Success_Status_Activity extends BaseAppActivity {
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
    private UserInfo userInfo;
    private Success_Status_Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_success_pay_page);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
        ButterKnife.bind(this);
        mContext = Success_Status_Activity.this;
        initGetUserInfo();
        initView();
    }

    private void initView() {
        storeName.setText(userInfo.getOrganization_name());
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

    /**
     * 获取用户信息
     */
    private void initGetUserInfo() {
        UserInfoImpl userInfoImpl = new UserInfoImpl(mContext);
        try {
            userInfo = userInfoImpl.getUserInfo("10");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
