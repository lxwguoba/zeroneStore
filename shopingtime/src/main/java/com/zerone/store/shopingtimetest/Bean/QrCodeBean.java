package com.zerone.store.shopingtimetest.Bean;

import java.io.Serializable;

/**
 * Created by on 2018/5/22 0022 14 38.
 * Author  LiuXingWen
 * 存二维码
 */

public class QrCodeBean implements Serializable {
    private String qrCode;

    public QrCodeBean(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
