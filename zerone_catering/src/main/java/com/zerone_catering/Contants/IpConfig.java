package com.zerone_catering.Contants;

/**
 * Created by Administrator on 2017/10/10 0010.
 * <p>
 * 用来保存域名 就是链接
 */
public class IpConfig {

    public static final String URL_ZS = "http://o2o.01nnt.com/api/androidCateringApi/";
    //测试版消费共富版
    public static final String URL_CS = "http://develop2.01nnt.com/api/androidCateringApi/";
    //通用的接口
    public static final String URL = URL_ZS;
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
    public static final String URL_GETPICTURE = "http://o2o.01nnt.com/catering/";
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
    //提交收银订单接口
    public static final String URL_SUBMIT_CASHIER = URL + "order_merge";
    //收银订单的详情接口
    public static final String URL_ORDER_MERGE_DETAIL = URL + "order_merge_detail";
    //收银订单的详情接口
    public static final String URL_CASH_PAYMENT = URL + "cash_payment";

    public static final String URL_OTHER_PAYMENT = URL + "other_payment";

    public static final String URL_GETCODE = "http://o2o.01nnt.com/api/androidRetailApi/" + "code";

    public static final String URL_GETUSERINFO = "http://o2o.01nnt.com/api/androidRetailApi/" + "deviceInfo";
    //设备信息签出接口
    public static final String URL_SINGOUT = "http://o2o.01nnt.com/api/androidRetailApi/" + "deviceCheckOut";
    //订单收银列表
    public static final String URL_ORDER_MERGE_LIST = URL + "order_merge_list";
    //商品详情
    public static final String URL_GOODS_DETAIL = URL + "goods_detail";
    //获取打印机列表
    public static final String URL_PRINTER_LIST = URL + "printer_list";
    //获取打印转态
    public static final String URL_ORDER_PRINTER = URL + "order_printer";
    //催单打印
    public static final String URL_ORDER_PRINTER_AGAIN = URL + "order_printer_again";
    //查看是否有新的订单
    public static final String URL_ORDER_NEWS = URL + "order_news";
    //取消订单
    public static final String URL_CANCEL_ORDER = URL + "cancel_order";
    //取消合并订单
    public static final String URL_CANCEL_ORDER_MERGE = URL + "cancel_order_merge";

    //====================================================================

}
