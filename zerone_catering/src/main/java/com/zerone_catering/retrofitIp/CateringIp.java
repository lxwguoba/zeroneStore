package com.zerone_catering.retrofitIp;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by on 2018-07-11 09 29.
 * Author  LiuXingWen
 * 访问服务器的接口
 */

public interface CateringIp {
    /**
     * 登录接口
     *
     * @param username   用户名
     * @param passwrod   密码
     * @param terminalId 终端号
     * @return
     */
    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> login(@Field("account") String username, @Field("password") String passwrod, @Field("terminal_num") String terminalId);

    /**
     * 获取桌子列表
     *
     * @return
     */
    @POST("table_list")
    @FormUrlEncoded
    Call<ResponseBody> getTables(@FieldMap() Map<String, String> map);


    /**
     * 获取商品列表
     *
     * @return
     */
    @POST("goods_list")
    @FormUrlEncoded
    Call<ResponseBody> getGoodsInfo(@FieldMap() Map<String, String> map);

    /**
     * 获取商品分类列表
     *
     * @return
     */
    @POST("category")
    @FormUrlEncoded
    Call<ResponseBody> getGoodsCat(@FieldMap() Map<String, String> map);


    /**
     * 提交订单
     *
     * @param map
     * @return
     */
    @POST("order_submit")
    @FormUrlEncoded
    Call<ResponseBody> subOrder(@FieldMap() Map<String, String> map);

    /**
     * 获取所有有的桌子(收银时的桌子列表)
     *
     * @param map
     * @return
     */
    @POST("table_order")
    @FormUrlEncoded
    Call<ResponseBody> getAllTable(@FieldMap() Map<String, String> map);


    /**
     * 获取所有的桌子分类(收银时的桌子分类列表)
     *
     * @param map
     * @return
     */
    @POST("room_list")
    @FormUrlEncoded
    Call<ResponseBody> getAllTableCat(@FieldMap() Map<String, String> map);


    /**
     * 获取收银订单列表(收银时的桌子分类列表)
     *
     * @param map
     * @return
     */
    @POST("order_list")
    @FormUrlEncoded
    Call<ResponseBody> getCashierOrderList(@FieldMap() Map<String, String> map);


    /**
     * 获取订单详情
     *
     * @param map
     * @return
     */
    @POST("order_detail")
    @FormUrlEncoded
    Call<ResponseBody> getDetails(@FieldMap() Map<String, String> map);


    /**
     * 删除商品
     *
     * @param map
     * @return
     */
    @POST("order_delete_goods")
    @FormUrlEncoded
    Call<ResponseBody> deleteGoods(@FieldMap() Map<String, String> map);

    /**
     * @param map
     * @return
     */
    @POST("order_goods_list")
    @FormUrlEncoded
    Call<ResponseBody> orderGoodsList(@FieldMap() Map<String, String> map);


    /**
     * 提交收银订单接口
     *
     * @param map
     * @return
     */
    @POST("order_merge")
    @FormUrlEncoded
    Call<ResponseBody> subCashierOrder(@FieldMap() Map<String, String> map);


    /**
     * 收银订单的详情接口
     *
     * @param map
     * @return
     */
    @POST("order_merge_detail")
    @FormUrlEncoded
    Call<ResponseBody> subCashierOrderDetails(@FieldMap() Map<String, String> map);


    /**
     * 收银 现金支付
     *
     * @param map
     * @return
     */
    @POST("cash_payment")
    @FormUrlEncoded
    Call<ResponseBody> CashPayment(@FieldMap() Map<String, String> map);


    /**
     * 收银 其他收银（吊起支付成功后发送给服务器）
     *
     * @param map
     * @return
     */
    @POST("other_payment")
    @FormUrlEncoded
    Call<ResponseBody> otherPayment(@FieldMap() Map<String, String> map);


    /**
     * 获取签入二维码
     *
     * @param map
     * @return
     */
    @POST("code")
    @FormUrlEncoded
    Call<ResponseBody> signCode(@FieldMap() Map<String, String> map);

    /**
     * 获取签入会员信息
     *
     * @param map
     * @return
     */
    @POST("deviceInfo")
    @FormUrlEncoded
    Call<ResponseBody> getSignUserInfoVip(@FieldMap() Map<String, String> map);


    /**
     * 把会员签出
     *
     * @param map
     * @return
     */
    @POST("deviceCheckOut")
    @FormUrlEncoded
    Call<ResponseBody> removeUserInfoVip(@FieldMap() Map<String, String> map);


    /**
     * 订单收银列表
     *
     * @param map
     * @return
     */
    @POST("order_merge_list")
    @FormUrlEncoded
    Call<ResponseBody> cashierOrderList(@FieldMap() Map<String, String> map);


    /**
     * 商品详情
     *
     * @param map
     * @return
     */
    @POST("goods_detail")
    @FormUrlEncoded
    Call<ResponseBody> cashierOrderListDetails(@FieldMap() Map<String, String> map);


    /**
     * 获取打印机列表
     *
     * @param map
     * @return
     */
    @POST("printer_list")
    @FormUrlEncoded
    Call<ResponseBody> getPrintList(@FieldMap() Map<String, String> map);

    //


    /**
     * 获取打印转态
     *
     * @param map
     * @return
     */
    @POST("order_printer")
    @FormUrlEncoded
    Call<ResponseBody> getPrintPrintStatus(@FieldMap() Map<String, String> map);


    /**
     * 检查是否有新的订单
     *
     * @param map
     * @return
     */
    @POST("order_news")
    @FormUrlEncoded
    Call<ResponseBody> checkNewOrder(@FieldMap() Map<String, String> map);


    /**
     * 取消订单
     *
     * @param map
     * @return
     */
    @POST("cancel_order")
    @FormUrlEncoded
    Call<ResponseBody> cancelOrder(@FieldMap() Map<String, String> map);

    /**
     * 取消合并订单
     *
     * @param map
     * @return
     */
    @POST("cancel_order_merge")
    @FormUrlEncoded
    Call<ResponseBody> cancelOrderMerge(@FieldMap() Map<String, String> map);
}
