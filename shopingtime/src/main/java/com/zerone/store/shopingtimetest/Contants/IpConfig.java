package com.zerone.store.shopingtimetest.Contants;

/**
 * Created by Administrator on 2017/10/10 0010.
 * <p>
 * 用来保存域名 就是链接
 */
public class IpConfig {
    //正式版a
    public static final String URL_ZS = "http://o2o.01nnt.com/api/androidRetailApi/";
    //测试版消费共富版
    public static final String URL_CS = "http://develop.01nnt.com/api/androidRetailApi/";
    //通用的接口
    public static final String URL = URL_ZS;
    //登录接口
    public static final String URL_LOGIN = URL + "login";
    //分类接口
    public static final String URL_CATEGORY = URL + "goodscategory";
    // 商品查询接口
    public static final String URL_SERACH = URL + "goodslist";
    //获取图片接口
    public static final String URL_GETPICTURE = "http://o2o.01nnt.com/";
    public static final String URL_GOODSLIST = URL + "goodslist";
    //开启/关闭零库存开单接口
    public static final String URL_KQLKC = URL + "allow_zero_stock";
    //下单减库存/付款减库存接口
    public static final String URL_FKJKC = URL + "change_stock_role";
    //查询店铺设置
    public static final String URL_DPSZ = URL + "stock_cfg";
    //订单详情接口
    public static final String URL_ORDERDETAILS = URL + "order_detail";
    //订单列表接口
    public static final String URL_ORDERLIST = URL + "order_list";
    //取消订单
    public static final String URL_QXORDER = URL + "cancel_order";
    //提交订单
    public static final String URL_SUBMITORDER = URL + "order_check";
    //修改订单支付状态接口（现金除外）
    public static final String URL_UPDATAPAY = URL + "other_payment";
    //现金支付方式
    public static final String URL_CASHPAY = URL + "cash_payment";
    //获取签入二维码
    public static final String URL_GETCODE = URL + "code";
    //获取签入信息
    public static final String URL_GETUSERINFO = URL + "deviceInfo";
    //设备信息签出接口
    public static final String URL_SINGOUT = URL + "deviceCheckOut";
}
