package com.zerone_catering.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zerone_catering.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by bps .
 */

public class PaymentWaitDialog extends Dialog {

    private final EditText pay_money_authcode;
    private PaymentSuccessListener mListener;
    private Context context;
    private Handler handler;

    public PaymentWaitDialog(@NonNull Context context, PaymentSuccessListener listener, Handler handler) {
        super(context, R.style.defaultDialogStyle);
        setContentView(R.layout.dialog_payment_wait);
        ImageView img = findViewById(R.id.image);
        pay_money_authcode = findViewById(R.id.pay_money);
        this.mListener = listener;
        setCanceledOnTouchOutside(false);
        this.context = context;
        this.handler = handler;
        Glide.with(context).load(R.drawable.payment_wait).asGif().override(600, 363).into(img);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && mListener != null) {
            //pay_money_authcode 这里调用接口
            mListener.onPaymentSuccess("");
            Toast.makeText(context, pay_money_authcode.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            gotoauthpay(pay_money_authcode.getText().toString().trim());
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param trim
     */
    private void gotoauthpay(String trim) {
        RequestParams params = new RequestParams("https://ctwxl.com/HibernateTest/scanqr");
        params.addBodyParameter("auth_code", trim);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                Message message = new Message();
                message.what = 0;
                message.obj = result;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public interface PaymentSuccessListener {
        void onPaymentSuccess(String json);
    }
}
