package com.zerone_catering.domain;

import java.io.Serializable;

/**
 * Created by on 2018/6/20 0020 15 40.
 * Author  LiuXingWen
 */

public class PrinterMachine implements Serializable {
    private String MaName;
    private String MaId;
    private boolean chblen;
    private String type; //1为前台打印机
    //每台打印机打印的联数
    private int printNum;
    /**
     * @param maName 打印机名称
     * @param maId   打印机ID
     * @param chblen 打印机是否被选中，默认为不选中
     */
    public PrinterMachine(String maName, String maId, boolean chblen) {
        MaName = maName;
        MaId = maId;
        this.chblen = chblen;
    }

    public PrinterMachine() {
    }

    public int getPrintNum() {
        return printNum;
    }

    public void setPrintNum(int printNum) {
        this.printNum = printNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaName() {
        return MaName;
    }

    public void setMaName(String maName) {
        MaName = maName;
    }

    public String getMaId() {
        return MaId;
    }

    public void setMaId(String maId) {
        MaId = maId;
    }

    public boolean isChblen() {
        return chblen;
    }

    public void setChblen(boolean chblen) {
        this.chblen = chblen;
    }

    @Override
    public String toString() {
        return "PrinterMachine{" +
                "MaName='" + MaName + '\'' +
                ", MaId='" + MaId + '\'' +
                ", chblen=" + chblen +
                ", type='" + type + '\'' +
                ", printNum=" + printNum +
                '}';
    }
}
