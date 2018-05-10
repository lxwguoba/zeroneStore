package com.zerone.shopingtimetest.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.zerone.shopingtimetest.Base64AndMD5.CreateToken;
import com.zerone.shopingtimetest.Bean.UserInfo;
import com.zerone.shopingtimetest.Bean.main.CatingBean;
import com.zerone.shopingtimetest.Bean.main.ProductBean;
import com.zerone.shopingtimetest.Contants.IpConfig;
import com.zerone.shopingtimetest.DB.impl.UserInfoImpl;
import com.zerone.shopingtimetest.Utils.NetUtils;
import com.zerone.shopingtimetest.Utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/5/10 0010 10 24.
 * Author  LiuXingWen
 */

public class JavaText extends AppCompatActivity {

    private UserInfo userInfo;
    private List<CatingBean> clist;
    private List<ProductBean> plist;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String shoplistJson = (String) msg.obj;
                    Log.i("URL", "shoplistJson=" + shoplistJson);
                    try {
                        JSONObject jsonshoplist = new JSONObject(shoplistJson);
                        int status = jsonshoplist.getInt("status");
                        if (status == 1) {
                            JSONArray jsonArray = jsonshoplist.getJSONObject("data").getJSONArray("goodslist");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject shopObject = jsonArray.getJSONObject(i);
                                ProductBean shopBean = new ProductBean();
                                shopBean.setName(shopObject.getString("name"));
                                shopBean.setId(shopObject.getInt("id"));
                                shopBean.setCategory_id(shopObject.getInt("category_id"));
                                shopBean.setCategory_name(shopObject.getString("category_name"));
                                shopBean.setDetails(shopObject.getString("details"));
                                shopBean.setPrice(shopObject.getString("price"));
                                shopBean.setStock(shopObject.getInt("stock"));
//                                shopBean.setShop_Count("0");
                                List<ProductBean.ThumbBean> Tlist = new ArrayList<ProductBean.ThumbBean>();
                                ProductBean.ThumbBean thumbBean = new ProductBean.ThumbBean();
                                if (shopObject.getJSONArray("thumb").length() > 0) {
                                    thumbBean.setThumb(shopObject.getJSONArray("thumb").getJSONObject(0).getString("thumb"));
                                } else {
                                    thumbBean.setThumb("");
                                }
                                Tlist.add(thumbBean);
                                shopBean.setThumb(Tlist);
                                plist.add(shopBean);
                            }
                        }
                        getCategoryInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    String cateJson = (String) msg.obj;
                    Log.i("URL", "cateJson=" + cateJson);
                    try {
                        JSONObject jsonObject = new JSONObject(cateJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("categorylist");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonBean = jsonArray.getJSONObject(i);
                                CatingBean cb = new CatingBean();
                                Map<String, List<ProductBean>> map = new HashMap<>();
                                List<ProductBean> list = new ArrayList<>();
                                int id = jsonBean.getInt("id");
                                Log.i("URL", "分类ID=" + id);
                                for (int j = 0; j < plist.size(); j++) {
                                    if (id == plist.get(j).getCategory_id()) {
                                        Log.i("URL", "商品所属分类的ID=" + plist.get(j).getCategory_id());
                                        list.add(plist.get(j));
                                    }
                                }
                                map.put("" + id, list);
                                cb.setMap(map);
                                cb.setDisplayorder(jsonBean.getInt("displayorder"));
                                cb.setId(jsonBean.getInt("id"));
                                cb.setName(jsonBean.getString("name"));
                                clist.add(cb);
                            }
                            Log.i("URL", clist.toString());
                            JSONArray jsonArray1 = new JSONArray(clist);
                            Log.i("URL", jsonArray1.toString());
                        } else if (status == 0) {
                            Toast.makeText(JavaText.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plist = new ArrayList<>();
        clist = new ArrayList<>();
        initGetUserInfo();
        initGetData();
    }

    private void initGetData() {
        if (userInfo == null) {
            return;
        }
        Log.i("URL", "5555555555555555555555");
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> loginMap = new HashMap<String, String>();
        loginMap.put("organization_id", userInfo.getOrganization_id());
        loginMap.put("account_id", userInfo.getAccount_id());
        Log.i("URL", "token" + token);
        loginMap.put("token", token);
        loginMap.put("timestamp", timestamp);
        Log.i("URL", "timestamp" + timestamp);
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }

        NetUtils.netWorkByMethodPost(this, loginMap, IpConfig.URL_GOODSLIST, handler, 0);

    }

    /**
     * 获取分类信息
     */
    public void getCategoryInfo() {
        if (userInfo == null) {
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> loginMap = new HashMap<String, String>();
        loginMap.put("organization_id", userInfo.getOrganization_id());
        loginMap.put("account_id", userInfo.getAccount_id());
        loginMap.put("token", token);
        loginMap.put("timestamp", timestamp);
        NetUtils.netWorkByMethodPost(this, loginMap, IpConfig.URL_CATEGORY, handler, 10);

    }

    /**
     * 获取用户信息
     */
    private void initGetUserInfo() {
        UserInfoImpl userInfoImpl = new UserInfoImpl(JavaText.this);
        try {
            userInfo = userInfoImpl.getUserInfo("10");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
