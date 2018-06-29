package com.zerone_catering.avtivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.dialog.PaymentWaitDialog;
import com.zerone_catering.payutils.PayUtils;
import com.zerone_catering.view.AmountInputView;
import com.zerone_catering.view.NumberKeyboard;

/**
 * Created by on 2018/6/14 0014 10 42.
 * Author  LiuXingWen
 */

public class CashierActivity extends BaseActvity implements NumberKeyboard.KeyClickCallback, PaymentWaitDialog.PaymentSuccessListener {

    private AmountInputView mAmountView;
    private NumberKeyboard mKeyBoard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_cashier);
        initView();

    }

    private void initView() {
        mAmountView = (AmountInputView) findViewById(R.id.amount_view);
        mKeyBoard = (NumberKeyboard) findViewById(R.id.key_board);
        mKeyBoard.setKeyClickCallback(this);
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = mAmountView.getText().toString().trim();
                money = money.substring(1);
                Log.i("URL", money);
                if (!(money != null && money.length() > 0)) {
                    Toast.makeText(CashierActivity.this, "请输入金额！！！", Toast.LENGTH_SHORT).show();
                } else {
                    PayUtils.LiftThePayment(money, CashierActivity.this);
                }

            }
        });
    }

    @Override
    public void onPaymentSuccess(String json) {

    }

    @Override
    public void onNumClick(int keyNum) {
        mAmountView.addText(keyNum + "");
    }

    @Override
    public void onDelClick() {
        mAmountView.delLast();
    }

    @Override
    public void onCleanClick() {
        mAmountView.clean();
    }


}
