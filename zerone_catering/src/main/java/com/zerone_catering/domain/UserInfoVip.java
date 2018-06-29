package com.zerone_catering.domain;

import java.io.Serializable;

/**
 * Created by on 2018/5/22 0022 15 00.
 * Author  LiuXingWen
 */

public class UserInfoVip implements Serializable {

    /**
     * nickname : 红色海洋
     * head_imgurl : http://thirdwx.qlogo.cn/mmopen/Q3auHgzwzM6YhcrdcdUqqXKNa89kiaUnJC6WicSVEjREiby1RDv9C4aOhicetVa1NhPianWszEGgYhNXiaiclNRbJ2Yw5ryw0vqNekYHYib033NcpJo/132
     * user_id : 10
     * device_num : 011VENB8
     */

    private String nickname;
    private String head_imgurl;
    private String user_id;
    private String device_num;
    private String fansmanage_user_id;

    public String getFansmanage_user_id() {
        return fansmanage_user_id;
    }

    public void setFansmanage_user_id(String fansmanage_user_id) {
        this.fansmanage_user_id = fansmanage_user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_imgurl() {
        return head_imgurl;
    }

    public void setHead_imgurl(String head_imgurl) {
        this.head_imgurl = head_imgurl;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_num() {
        return device_num;
    }

    public void setDevice_num(String device_num) {
        this.device_num = device_num;
    }
}
