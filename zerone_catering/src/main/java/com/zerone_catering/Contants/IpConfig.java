package com.zerone_catering.Contants;

/**
 * Created by Administrator on 2017/10/10 0010.
 * <p>
 * 用来保存域名 就是链接
 */
public class IpConfig {

    //测试版消费共富版
    public static final String URL_CS = "http://develop2.01nnt.com/api/androidCateringApi/";
    //通用的接口
    public static final String URL = URL_CS;
    //登录接口
    public static final String URL_LOGIN = URL + "login";
    //获取桌子接口
    public static final String URL_TABLE = URL + "table_list";
    //商品分类列表
    public static final String URL_GCATERING = URL + "category";
    //商品列表
    public static final String URL_GLIST = URL + "goods_list";
    // 商品查询接口
    public static final String URL_SERACH = URL + "goods_list";
    //获取图片接口
    public static final String URL_GETPICTURE = "http://develop2.01nnt.com/";
    //提交订单接口
    public static final String URL_SUBMIT = URL + "order_submit";
    //收银订单的桌子分类
    public static final String URL_CASHIERTABLE = URL + "room_list";
    //获取所有的桌子
    public static final String URL_CASHIERTABLELIST = URL + "table_order";
    //获取收银订单列表
    public static final String URL_CASHIER_ORDER_LIST = URL + "order_list";
    //获取订单详情
    public static final String URL_ORDER_DETAILS = URL + "order_detail";
    //删除商品
    public static final String URL_CANCEL_GOODS = URL + "order_delete_goods";
    //删除商品
    public static final String URL_ORDER_GOODS_LIST = URL + "order_goods_list";


    //====================================================================
    //分类接口
    public static final String URL_CATEGORY = URL + "goodscategory";
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
