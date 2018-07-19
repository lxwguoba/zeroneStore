package com.zerone_catering.avtivity.product_details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.main.ProductBean;
import com.zerone_catering.imgageloader.GlideImageLoader;
import com.zerone_catering.imgageloader.URLImageGetter;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018-07-03 09 57.
 * Author  LiuXingWen
 */

public class Activity_Product_Details extends BaseActvity {

    private Banner banner;
    private WebView webView;
    private Intent intent;
    private ProductBean productBean;
    private UserInfo userInfo;
    private Context mContent;
    private TextView product_name;
    private TextView liuyuan;
    private TextView product_price;
    private TextView product_discount;
    private List<String> list_img_url;
    private String html = "";
    private ZLoadingDialog loading_dailog;
    private TextView describe;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String pjson = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(pjson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Gson gson = new Gson();
                            com.zerone_catering.domain.ProductBean productBean = gson.fromJson(pjson, com.zerone_catering.domain.ProductBean.class);

                            describe.setMovementMethod(LinkMovementMethod.getInstance());
                            describe.setText(Html.fromHtml(productBean.getData().getGoods().getDetails(), new URLImageGetter(Activity_Product_Details.this, describe), null));
                            product_name.setText(productBean.getData().getGoods().getName());
                            liuyuan.setText("");
                            product_price.setText("￥" + productBean.getData().getGoods().getPrice());
                            product_discount.setText("");
                            list_img_url.clear();
                            List<com.zerone_catering.domain.ProductBean.DataBean.GoodsBean.ThumbBean> thumb = productBean.getData().getGoods().getThumb();
                            for (int i = 0; i < thumb.size(); i++) {
                                list_img_url.add(IpConfig.URL_GETPICTURE + thumb.get(i).getThumb());
                                Log.i("URL", IpConfig.URL_GETPICTURE + thumb.get(i).getThumb());
                            }
                            initImageLoader();
                        } else if (status == 0) {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        loading_dailog.dismiss();
                    }
                    break;
            }
        }
    };
    private LinearLayout back;
    private TextView store_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signle_product_details);
        intent = getIntent();
        mContent = Activity_Product_Details.this;
        userInfo = GetUserInfo.initGetUserInfo(this);
        productBean = (ProductBean) intent.getSerializableExtra("ordreinfo");
        list_img_url = new ArrayList<>();
        initData();
        initView();
        doAction();
    }

    private void doAction() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Product_Details.this.finish();
            }
        });

    }

    /**
     * 获取网若数据
     */
    private void initData() {
        if (userInfo == null) {
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> map = new HashMap<String, String>();
        map.put("organization_id", userInfo.getOrganization_id());
        map.put("account_id", userInfo.getAccount_id());
        map.put("token", token);
        map.put("timestamp", timestamp);
        map.put("goods_id", productBean.getId() + "");
        loading_dailog = LoadingUtils.getDailog(mContent, Color.RED, "获取信息中...");
        if (!Activity_Product_Details.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContent, map, IpConfig.URL_GOODS_DETAIL, handler, 0);

    }

    private void initView() {
        store_name = (TextView) findViewById(R.id.store_name);
        store_name.setText(userInfo.getOrganization_name());
        banner = (Banner) findViewById(R.id.banner);
        back = (LinearLayout) findViewById(R.id.back);
        product_name = (TextView) findViewById(R.id.product_name);
        liuyuan = (TextView) findViewById(R.id.liuyuan);
        product_price = (TextView) findViewById(R.id.product_price);
        product_discount = (TextView) findViewById(R.id.product_discount);
        describe = (TextView) findViewById(R.id.describe);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        //支持js的运行
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    /**
     * banner的设置
     */
    private void initImageLoader() {
//          if (!(list_img_url.size()>2)){
//              list_img_url.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1530583296&di=68b0e3437f67b61301613392e597b4be&src=http://img3.duitang.com/uploads/item/201609/23/20160923194737_THWmL.thumb.700_0.jpeg");
//              list_img_url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530593418862&di=eed57513034187e2e17d3ce7bbc44bff&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201204%2F22%2F20120422214918_VJjXe.thumb.700_0.jpeg");
//              list_img_url.add("http://pic.enorth.com.cn/0/08/71/89/8718907_600879.jpg");
//              list_img_url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530593604384&di=06a16edb8c36231177240015e12a6ebc&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fblog%2F201511%2F14%2F20151114115901_jxEy8.jpeg");
//          }
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(list_img_url);
        //设置banner动画效果  DepthPage
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(list_title);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
}
