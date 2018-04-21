package com.zerone.shopingtimetest.Contants;

/**
 * Created by Administrator on 2017/10/10 0010.
 * <p>.`1   q去
 * 用来保存域名 就是链接
 */
public class IpConfig {
    //登录接口
    public static final String URL_LOGIN = "http://develop.01nnt.com/api/androidSimpleApi/login";
    //分类接口
    public static final String URL_CATEGORY = "http://develop.01nnt.com/api/androidSimpleApi/goodscategory";
    // 商品查询接口
    public static final String URL_SERACH = "http://develop.01nnt.com/api/androidSimpleApi/goodslist";
    //获取图片接口
    public static final String URL_GETPICTURE = "http://develop.01nnt.com/";
    public static final String URL_GOODSLIST = "http://develop.01nnt.com/api/androidSimpleApi/goodslist";
    //开启/关闭零库存开单接口
    public static final String URL_KQLKC = "http://develop.01nnt.com/api/androidSimpleApi/allow_zero_stock";
    //下单减库存/付款减库存接口
    public static final String URL_FKJKC = "http://develop.01nnt.com/api/androidSimpleApi/change_stock_role";
    //查询店铺设置
    public static final String URL_DPSZ = "http://develop.01nnt.com/api/androidSimpleApi/stock_cfg";
    //订单详情接口
    public static final String URL_ORDERDETAILS = "http://develop.01nnt.com/api/androidSimpleApi/order_detail";
    //订单列表接口
    public static final String URL_ORDERLIST = "http://develop.01nnt.com/api/androidSimpleApi/order_list";
    //取消订单
    public static final String URL_QXORDER = "http://develop.01nnt.com/api/androidSimpleApi/cancel_order";
    //取消订单
    public static final String URL_SUBMITORDER = "http://develop.01nnt.com/api/androidSimpleApi/order_check";
    //修改订单支付状态接口（现金除外）
    public static final String URL_UPDATAPAY = "http://develop.01nnt.com/api/androidSimpleApi/other_payment";
    //现金支付方式
    public static final String URL_CASHPAY = "http://develop.01nnt.com/api/androidSimpleApi/cash_payment";
}
