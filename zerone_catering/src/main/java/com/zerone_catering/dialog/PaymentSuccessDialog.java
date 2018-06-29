package com.zerone_catering.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.zerone_catering.R;

/**
 * Created by bps .
 */

public class PaymentSuccessDialog extends Dialog {

    public PaymentSuccessDialog(@NonNull Context context) {
        super(context, R.style.defaultDialogStyle);
        setContentView(R.layout.dialog_payment_success);
    }
}
