package com.zerone.store.shopingtimetest.Bean;

import java.io.Serializable;

/**
 * Created by on 2018/3/31 0031 16 38.
 * Author  LiuXingWen
 * <p>
 * 用户登录成功后保留用户的信息
 */

public class UserInfo implements Serializable {

    private String account_id;
    private String account;
    //店铺id
    private String organization_id;
    private String uuid;
    private String organization_name;
    private String realName;
    //公众号id也称联盟id
    private String fansnamage_id;


    public UserInfo() {

    }

    public String getFansnamage_id() {
        return fansnamage_id;
    }

    public void setFansnamage_id(String fansnamage_id) {
        this.fansnamage_id = fansnamage_id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "account_id='" + account_id + '\'' +
                ", account='" + account + '\'' +
                ", organization_id='" + organization_id + '\'' +
                ", uuid='" + uuid + '\'' +
                ", organization_name='" + organization_name + '\'' +
                ", realName='" + realName + '\'' +
                ", fansnamage_id='" + fansnamage_id + '\'' +
                '}';
    }
}
