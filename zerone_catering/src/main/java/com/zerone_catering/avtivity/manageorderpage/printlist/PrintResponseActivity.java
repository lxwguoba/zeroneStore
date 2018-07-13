package com.zerone_catering.avtivity.manageorderpage.printlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 2018/6/20 0020 15 58.
 * Author  LiuXingWen
 * 打印成功后的页面
 */

public class PrintResponseActivity extends BaseActvity {

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.success_title)
    TextView successTitle;
    @Bind(R.id.icon_logo_item)
    ImageView iconLogoItem;
    @Bind(R.id.icon_logo)
    RelativeLayout iconLogo;
    @Bind(R.id.print_status)
    TextView printStatus;
    @Bind(R.id.back_mactivity)
    Button backMactivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_response_activity);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.back_mactivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                PrintResponseActivity.this.finish();
                break;
            case R.id.back_mactivity:
                PrintResponseActivity.this.finish();
                break;
        }
    }
}
