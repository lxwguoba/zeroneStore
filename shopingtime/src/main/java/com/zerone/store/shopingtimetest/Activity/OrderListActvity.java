package com.zerone.store.shopingtimetest.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone.store.shopingtimetest.Adapter.cart_list.ListGoodsDetails_Adapter;
import com.zerone.store.shopingtimetest.Adapter.shopplistadapter.PersonAdapter;
import com.zerone.store.shopingtimetest.Adapter.shopplistadapter.ProductListAdapter;
import com.zerone.store.shopingtimetest.Adapter.shopplistadapter.RecycleGoodsCategoryListAdapter;
import com.zerone.store.shopingtimetest.Adapter.shopplistadapter.RecycleProductCategoryListAdapter;
import com.zerone.store.shopingtimetest.Base64AndMD5.CreateToken;
import com.zerone.store.shopingtimetest.BaseActivity.BaseAppActivity;
import com.zerone.store.shopingtimetest.Bean.SearchBean;
import com.zerone.store.shopingtimetest.Bean.ShopBean;
import com.zerone.store.shopingtimetest.Bean.UserInfo;
import com.zerone.store.shopingtimetest.Bean.main.CatingBean;
import com.zerone.store.shopingtimetest.Bean.main.ProductBean;
import com.zerone.store.shopingtimetest.Bean.refresh.RefreshBean;
import com.zerone.store.shopingtimetest.Bean.refresh.SetDataGoodsOrder;
import com.zerone.store.shopingtimetest.Bean.shoplistbean.GoodsCategroyListBean;
import com.zerone.store.shopingtimetest.Bean.shoplistbean.ShopMessageBean;
import com.zerone.store.shopingtimetest.Contants.ContantData;
import com.zerone.store.shopingtimetest.Contants.IpConfig;
import com.zerone.store.shopingtimetest.DB.impl.UserInfoImpl;
import com.zerone.store.shopingtimetest.R;
import com.zerone.store.shopingtimetest.Utils.DoubleUtils;
import com.zerone.store.shopingtimetest.Utils.LoadingUtils;
import com.zerone.store.shopingtimetest.Utils.MapUtilsSetParam;
import com.zerone.store.shopingtimetest.Utils.NetUtils;
import com.zerone.store.shopingtimetest.Utils.NetworkUtil;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/3/31 0031 09 51.
 * Author  LiuXingWen
 * OnHeaderClickListener
 */
public class OrderListActvity extends BaseAppActivity {
    private static final String TAG = "OrderListActvity";
    //0为初始化状态就是不刷新 若是1是点击了菜单兰里的刷新按钮 刷新成功后重置
    private static int REFRESH_CODE = 0;
    //商品类别列表
    private List<GoodsCategroyListBean.DataBean.CategorylistBean> catelist = new ArrayList<>();
    //存储含有标题的第一个含有商品类别名称的条目的下表
    private List<Integer> titlePois = new ArrayList<>();
    //商品列表
    private List<ShopBean> goodsitemlist = new ArrayList<>();
    private List<ShopBean> buyShoppingList = new ArrayList<>();
    //    private StickyHeadersItemDecoration top;
    //商品分类
    private RecyclerView mGoodsCateGoryList;
    //上一个标题的小标位置
    private int lastTitlePoi;
    //商品信息
    private RecyclerView mGoodsInfoRecyclerView;
    private RecycleGoodsCategoryListAdapter mGoodsCategoryListAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private PersonAdapter personAdapter;
    //    private BigramHeaderAdapter headerAdapter;
    private ZLoadingDialog loading_dailog;
    private UserInfo userInfo;
    private List<ShopMessageBean> buycartshoplist;
    private View shopview;
    private CheckBox allcheck;
    private TextView checkCount;
    private LinearLayout clear_cart;
    private ListView shoplistview;
    private TextView shopCount;
    private LinearLayout parentView;
    private ListGoodsDetails_Adapter listAdapter;
    private int popupWidth;
    private int popupHeight;
    private TextView pop_price;
    private View menuView;
    private LinearLayout systemset;
    private LinearLayout ordermenu;
    private PopupWindow mPopupWindowMenu;
    private TextView quxiaoMenu;
    private LinearLayout f_menu;
    private MyLayoutManage myLayoutManage;
    private RelativeLayout showOrderList;
    private PopupWindow mPopupWindow;
    private LinearLayout settlement;
    private LinearLayout scan_btn;
    private LinearLayout search_head_btn;
    private TextView selectedgoodsmoney;
    private LinearLayout jiesaun;
    private OrderListActvity mContent;
    private ImageView cart_logo;
    //结算
    private TextView settlement_;
    //收银台显示文字 TextView
    private TextView cashier;
    private List<CatingBean> clist = new ArrayList<>();
    private List<ProductBean> plist = new ArrayList<>();
    private LinearLayout refresh_layout;
    //分类的适配器
    private RecycleProductCategoryListAdapter rcpAdapter;
    private ProductListAdapter plAdapter;
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    loading_dailog.dismiss();
                    plist.clear();
                    clist.clear();
                    shopCount.setText("0");
                    selectedgoodsmoney.setText("0");
                    String shoplistJson = (String) msg.obj;
                    Log.i("URL", "shoplistJson:::" + shoplistJson);
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
                                shopBean.setProductCount(0);
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
                                for (int j = 0; j < plist.size(); j++) {
                                    if (id == plist.get(j).getCategory_id()) {
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
                            initRecycleView();
                        } else if (status == 0) {
                            Toast.makeText(OrderListActvity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (loading_dailog != null) {
                            loading_dailog.dismiss();
                        }
                    }
                    break;
//                case 0:
//                    String shoplistJson = (String) msg.obj;
//
//                    Log.i("URL","shoplistJson="+shoplistJson);
//
//                    loading_dailog.dismiss();
//                    goodsitemlist.clear();
//                    shopCount.setText("0");
//                    selectedgoodsmoney.setText("0");
//                    if (buyShoppingList!=null){
//                        buyShoppingList.clear();
//                    }
//                    showOrderList.setVisibility(View.GONE);
//                    try {
//                        JSONObject jsonshoplist = new JSONObject(shoplistJson);
//                        int status = jsonshoplist.getInt("status");
//                        if (status == 1) {
//                            JSONArray jsonArray = jsonshoplist.getJSONObject("data").getJSONArray("goodslist");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject shopObject = jsonArray.getJSONObject(i);
//                                ShopBean shopBean = new ShopBean();
//                                shopBean.setName(shopObject.getString("name"));
//                                shopBean.setId(shopObject.getInt("id"));
//                                shopBean.setCategory_id(shopObject.getInt("category_id"));
//                                shopBean.setCategory_name(shopObject.getString("category_name"));
//                                shopBean.setDetails(shopObject.getString("details"));
//                                shopBean.setPrice(shopObject.getString("price"));
//                                shopBean.setStock(shopObject.getInt("stock"));
//                                shopBean.setShop_Count("0");
//                                List<ShopBean.ThumbBean> Tlist = new ArrayList<ShopBean.ThumbBean>();
//                                ShopBean.ThumbBean thumbBean = new ShopBean.ThumbBean();
//                                if (shopObject.getJSONArray("thumb").length() > 0) {
//                                    thumbBean.setThumb(shopObject.getJSONArray("thumb").getJSONObject(0).getString("thumb"));
//                                } else {
//                                    thumbBean.setThumb("");
//                                }
//                                Tlist.add(thumbBean);
//                                shopBean.setThumb(Tlist);
//                                goodsitemlist.add(shopBean);
//                            }
//                        }
//                        getCategoryInfo();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//
//                case 10:
//                    String cateJson = (String) msg.obj;
//                    Log.i("URL","cateJson="+cateJson);
//
//                    loading_dailog.dismiss();
//                    catelist.clear();
//                    try {
//                        JSONObject jsonObject = new JSONObject(cateJson);
//                        int status = jsonObject.getInt("status");
//                        if (status == 1) {
//                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("categorylist");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonBean = jsonArray.getJSONObject(i);
//                                GoodsCategroyListBean.DataBean.CategorylistBean clb = new GoodsCategroyListBean.DataBean.CategorylistBean();
//                                clb.setDisplayorder(jsonBean.getInt("displayorder"));
//                                clb.setId(jsonBean.getInt("id"));
//                                clb.setName(jsonBean.getString("name"));
//                                catelist.add(clb);
//                                titlePois.add(i);
//                            }
//                            initData(catelist);
//                            if (REFRESH_CODE == 1) {
//                                if (mPopupWindowMenu != null) {
//                                    mPopupWindowMenu.dismiss();
//                                }
//                                REFRESH_CODE = 0;
//                            }
//                        } else if (status == 0) {
//                            Toast.makeText(OrderListActvity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
                case 3:
                    /*清空购物车*/
                    for (int i = 0; i < clist.size(); i++) {
                        Map<String, List<ProductBean>> map = clist.get(i).getMap();
                        for (Map.Entry<String, List<ProductBean>> entry : map.entrySet()) {
                            List<ProductBean> value = entry.getValue();
                            for (int j = 0; j < value.size(); j++) {
                                value.get(j).setProductCount(0);
                            }
                        }
                    }
                    selectedgoodsmoney.setText("0");
                    buycartshoplist.clear();
                    buyShoppingList.clear();
                    checkCount.setText("没有商品");
                    pop_price.setText("0.00");
                    listAdapter.notifyDataSetChanged();
                    showOrderList.setVisibility(View.GONE);
                    plAdapter.notifyDataSetChanged();
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                    break;
                case 4:
                    /**
                     * 全选和全不选
                     */
                    String money = "";
                    double v = 0.00;
                    if (allcheck.isChecked()) {
                        for (int i = 0; i < buycartshoplist.size(); i++) {
                            buycartshoplist.get(i).setSp_check(true);
                            v += Double.parseDouble(buycartshoplist.get(i).getSp_price()) * Integer.parseInt(buycartshoplist.get(i).getSp_count());
                        }
                        pop_price.setText(DoubleUtils.setSSWRDouble(v) + "");
                    } else {
                        for (int i = 0; i < buycartshoplist.size(); i++) {
                            buycartshoplist.get(i).setSp_check(false);
                        }
                        pop_price.setText("0.00");
                    }
                    listAdapter.notifyDataSetChanged();
                    break;
                case 33:
                    Intent intent = new Intent(OrderListActvity.this, SearchActivity.class);
                    if (buyShoppingList != null) {
                        intent.putExtra("listobj", (Serializable) buyShoppingList);
                    }
                    startActivityForResult(intent, 1100);
                    if (personAdapter != null) {
                        personAdapter.notifyDataSetChanged();
                    }
                    break;

                case 1000:
                    //按了添加商品这个按钮
                    boolean sameShopboolean = false;
                    Integer sameIndex = null;
                    int index = (int) msg.obj;
                    //给列表设置个数
                    int productCount = plist.get(index).getProductCount();
                    productCount++;
                    plist.get(index).setProductCount(productCount);
                    int category_pos = plist.get(index).getCategory_pos();
                    Map<String, List<ProductBean>> map = clist.get(category_pos).getMap();
                    map.put(category_pos + "", plist);
                    clist.get(category_pos).setMap(map);
                    //给购物车添加商品
                    if (buyShoppingList.size() > 0) {
                        // 1、购物车已有商品
                        for (int i = 0; i < buyShoppingList.size(); i++) {
                            //判断是否是同一个商品
                            if (plist.get(index).getId() == buyShoppingList.get(i).getId()) {
                                sameShopboolean = true;
                                sameIndex = i;
                                break;
                            }
                        }
                        if (sameShopboolean) {
                            //相同商品 数量加一
                            if (sameIndex != null) {
                                //把这个商品从新设置一下
                                ShopBean shopBean = setShopBean(index);
                                buyShoppingList.set(sameIndex, shopBean);
                            }
                        } else {
                            //不同商品
                            ShopBean shopBean = setShopBean(index);
                            buyShoppingList.add(shopBean);
                        }
                    } else {
                        //2、购物车没有商品
                        ShopBean shopBean = setShopBean(index);
                        buyShoppingList.add(shopBean);
                    }
                    double dmoney = 0.00;
                    if (buyShoppingList.size() > 0) {
                        showOrderList.setVisibility(View.VISIBLE);
                        int buyNum = 0;
                        for (int i = 0; i < buyShoppingList.size(); i++) {
                            dmoney += Double.parseDouble(buyShoppingList.get(i).getPrice()) * Integer.parseInt(buyShoppingList.get(i).getShop_Count());
                            buyNum += Integer.parseInt(buyShoppingList.get(i).getShop_Count());
                            Log.i("URL", "{" + buyNum + "}");
                        }
                        shopCount.setText(buyNum + "");
                    }
                    selectedgoodsmoney.setText("" + DoubleUtils.setSSWRDouble(dmoney));

                    rcpAdapter.notifyDataSetChanged();
                    plAdapter.notifyDataSetChanged();
                    myCheckstandGray(true);
                    break;

                case 1110:
                    //减号点击按钮点击
                    boolean sameDShopboolean = false;
                    int sameDIndex = -1;
                    //按了减少商品这个按钮
                    int indx = (int) msg.obj;
                    int cou = plist.get(indx).getProductCount();
                    for (int i = 0; i < buyShoppingList.size(); i++) {
                        if (plist.get(indx).getId() == buyShoppingList.get(i).getId()) {
                            sameDIndex = i;
                            break;
                        }
                    }
                    if (cou > 0) {
                        cou--;
                        if (cou == 0) {
                            plist.get(indx).setProductCount(0);
                            if (sameDIndex != -1) {
                                buyShoppingList.remove(sameDIndex);
                            }
                        } else {
                            plist.get(indx).setProductCount(cou);
                            buyShoppingList.get(sameDIndex).setShop_Count(cou + "");
                        }
                    }

                    if (buyShoppingList.size() > 0) {
                        int buyNum = 0;
                        double dmone = 0;
                        for (int i = 0; i < buyShoppingList.size(); i++) {
                            buyNum += Integer.parseInt(buyShoppingList.get(i).getShop_Count());
                            dmone += Double.parseDouble(buyShoppingList.get(i).getPrice()) * Integer.parseInt(buyShoppingList.get(i).getShop_Count());
                        }
                        if (buyNum == 0) {
                            showOrderList.setVisibility(View.GONE);
                            selectedgoodsmoney.setText("0");
                            myCheckstandGray(false);
                        } else {
                            showOrderList.setVisibility(View.VISIBLE);
                            selectedgoodsmoney.setText(DoubleUtils.setSSWRDouble(dmone));
                            myCheckstandGray(true);
                        }
                        shopCount.setText(buyNum + "");
                    } else {
                        showOrderList.setVisibility(View.GONE);
                        myCheckstandGray(false);
                        selectedgoodsmoney.setText("0");
                        shopCount.setText("0");
                    }
                    plAdapter.notifyDataSetChanged();
                    break;
                case 207:
                    //搜索款返回来的数据
                    plist.clear();
                    ShopBean shopBean = (ShopBean) msg.obj;
                    int pos = changeClist(shopBean.getCategory_id(), shopBean.getId());
                    setPlist(pos, shopBean.getCategory_id());
                    //按了添加商品这个按钮
                    boolean sameSopboolean = false;
                    Integer sameindex = null;
                    //给购物车添加商品
                    if (buyShoppingList.size() > 0) {
                        // 1、购物车已有商品
                        for (int i = 0; i < buyShoppingList.size(); i++) {
                            //判断是否是同一个商品
                            if (shopBean.getId() == buyShoppingList.get(i).getId()) {
                                sameSopboolean = true;
                                sameindex = i;
                                break;
                            }
                        }
                        if (sameSopboolean) {
                            //相同商品 数量加一
                            if (sameindex != null) {
                                //把这个商品从新设置一下
                                int coun = Integer.parseInt(buyShoppingList.get(sameindex).getShop_Count());
                                coun++;
                                buyShoppingList.get(sameindex).setShop_Count(coun + "");
                                buyShoppingList.set(sameindex, buyShoppingList.get(sameindex));
                            }
                        } else {
                            buyShoppingList.add(shopBean);
                        }
                    } else {
                        //2、购物车没有商品
                        buyShoppingList.add(shopBean);
                    }
                    double dmone = 0.00;
                    if (buyShoppingList.size() > 0) {
                        showOrderList.setVisibility(View.VISIBLE);
                        int buyNum = 0;
                        for (int i = 0; i < buyShoppingList.size(); i++) {
                            dmone += Double.parseDouble(buyShoppingList.get(i).getPrice()) * Integer.parseInt(buyShoppingList.get(i).getShop_Count());
                            buyNum += Integer.parseInt(buyShoppingList.get(i).getShop_Count());
                            Log.i("URL", "{" + buyNum + "}");
                        }
                        shopCount.setText(buyNum + "");
                    }
                    selectedgoodsmoney.setText("" + DoubleUtils.setSSWRDouble(dmone));
                    plAdapter.notifyDataSetChanged();
                    myCheckstandGray(true);
                    break;

                case 12:
                    //扫描结果获取服务器获取商品的处理
                    loading_dailog.dismiss();
                    plist.clear();
                    String scanJson = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(scanJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            //判断这个商品在clist的哪个位置
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("goodslist");
                            int category_id = jsonArray.getJSONObject(0).getInt("category_id");
                            int id = jsonArray.getJSONObject(0).getInt("id");
                            int cposition = changeClist(category_id, id);
                            setPlist(cposition, category_id);
                            int tindex = 0;
                            boolean tlean = false;
                            if (buyShoppingList.size() > 0) {
                                //这个是有商品存在的 就要判断是不是同一个商品。是就在购物车里商品++。
                                for (int b = 0; b < buyShoppingList.size(); b++) {
                                    if (buyShoppingList.get(b).getId() == id) {
                                        tindex = b;
                                        tlean = true;
                                        break;
                                    }
                                }
                                if (tlean) {
                                    //购物车里有商品有扫码条形码的商品 所以将相同的数量加一，其它数据不变
                                    ShopBean sb1 = buyShoppingList.get(tindex);
                                    int bscount = Integer.parseInt(buyShoppingList.get(tindex).getShop_Count());
                                    bscount++;
                                    sb1.setShop_Count(bscount + "");
                                    sb1.setPrice(buyShoppingList.get(tindex).getPrice());
                                    sb1.setCategory_name(buyShoppingList.get(tindex).getCategory_name());
                                    sb1.setStock(buyShoppingList.get(tindex).getStock());
                                    sb1.setId(buyShoppingList.get(tindex).getId());
                                    sb1.setName(buyShoppingList.get(tindex).getName());
                                    sb1.setCategory_id(buyShoppingList.get(tindex).getCategory_id());
                                    sb1.setCatPosition(buyShoppingList.get(tindex).getCatPosition());
                                    sb1.setDetails(buyShoppingList.get(tindex).getDetails());
                                    sb1.setThumb(buyShoppingList.get(tindex).getThumb());
                                    buyShoppingList.set(tindex, sb1);
                                } else {
                                    //购物车里有商品但是没有扫码条形码的商品 所以需要添加
                                    ShopBean sb = new ShopBean();
                                    JSONArray jArray = jsonObject.getJSONObject("data").getJSONArray("goodslist");
                                    sb.setShop_Count("1");
                                    sb.setPrice(jArray.getJSONObject(0).getString("price"));
                                    sb.setCategory_name(jArray.getJSONObject(0).getString("category_name"));
                                    sb.setStock(jArray.getJSONObject(0).getInt("stock"));
                                    sb.setId(jArray.getJSONObject(0).getInt("id"));
                                    sb.setName(jArray.getJSONObject(0).getString("name"));
                                    sb.setCategory_id(jArray.getJSONObject(0).getInt("category_id"));
                                    sb.setCatPosition(cposition);
                                    sb.setDetails(jArray.getJSONObject(0).getString("details"));
                                    List<ShopBean.ThumbBean> Tlist = new ArrayList<ShopBean.ThumbBean>();
                                    ShopBean.ThumbBean thumbBean = new ShopBean.ThumbBean();
                                    if (jArray.getJSONObject(0).getJSONArray("thumb").length() > 0) {
                                        thumbBean.setThumb(jArray.getJSONObject(0).getJSONArray("thumb").getJSONObject(0).getString("thumb"));
                                    } else {
                                        thumbBean.setThumb("");
                                    }
                                    Tlist.add(thumbBean);
                                    sb.setThumb(Tlist);
                                    buyShoppingList.add(sb);
                                }
                            } else {
                                ShopBean sb = new ShopBean();
                                JSONArray jArray = jsonObject.getJSONObject("data").getJSONArray("goodslist");
                                sb.setShop_Count("1");
                                sb.setPrice(jArray.getJSONObject(0).getString("price"));
                                sb.setCategory_name(jArray.getJSONObject(0).getString("category_name"));
                                sb.setStock(jArray.getJSONObject(0).getInt("stock"));
                                sb.setId(jArray.getJSONObject(0).getInt("id"));
                                sb.setName(jArray.getJSONObject(0).getString("name"));
                                sb.setCategory_id(jArray.getJSONObject(0).getInt("category_id"));
                                sb.setCatPosition(cposition);
                                sb.setDetails(jArray.getJSONObject(0).getString("details"));
                                List<ShopBean.ThumbBean> Tlist = new ArrayList<ShopBean.ThumbBean>();
                                ShopBean.ThumbBean thumbBean = new ShopBean.ThumbBean();
                                if (jArray.getJSONObject(0).getJSONArray("thumb").length() > 0) {
                                    thumbBean.setThumb(jArray.getJSONObject(0).getJSONArray("thumb").getJSONObject(0).getString("thumb"));
                                } else {
                                    thumbBean.setThumb("");
                                }
                                Tlist.add(thumbBean);
                                sb.setThumb(Tlist);
                                buyShoppingList.add(sb);
                            }
                            //算数量的总个数
                            int dcount = 0;
                            double mone = 0.0;
                            for (int f = 0; f < buyShoppingList.size(); f++) {
                                mone += Double.parseDouble(buyShoppingList.get(f).getPrice()) * Integer.parseInt(buyShoppingList.get(f).getShop_Count());
                                dcount += Integer.parseInt(buyShoppingList.get(f).getShop_Count());
                                Log.i("URL", "购物车里面的数量：：：" + dcount);
                            }
                            Log.i("URL", "购物车里面的数量：：：" + dcount);
                            if (buyShoppingList.size() > 0) {
                                myCheckstandGray(true);
                                showOrderList.setVisibility(View.VISIBLE);
                            } else {
                                myCheckstandGray(false);
                                showOrderList.setVisibility(View.INVISIBLE);
                            }
                            shopCount.setText("" + dcount);
                            selectedgoodsmoney.setText(DoubleUtils.setSSWRDouble(mone) + "");
                            rcpAdapter.setCheckPosition(cposition);
                            rcpAdapter.notifyDataSetChanged();
                            plAdapter.notifyDataSetChanged();
                        } else if (status == 0) {
                            Toast.makeText(OrderListActvity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 110:
                    Intent intent01 = new Intent(OrderListActvity.this, MakeSureTheOrderActivity.class);
                    if (buycartshoplist.size() > 0 && buyShoppingList.size() > 0) {
                        intent01.putExtra("listobj", (Serializable) buycartshoplist);
                        startActivity(intent01);
                    }
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                    plAdapter.notifyDataSetChanged();
                    break;
                case 120:
                    Intent inten = new Intent(OrderListActvity.this, MakeSureTheOrderActivity.class);
                    if (buycartshoplist.size() > 0) {
                        inten.putExtra("listobj", (Serializable) buycartshoplist);
                        startActivity(inten);
                    }
                    plAdapter.notifyDataSetChanged();
                    break;
                case 200:
                    //popwindow 里 添加按钮  步骤如下：
                    //1、商品添加 2个购物车的数量也要添加  要做到一致
                    //这个是选中的商品  数量要加一  这里不存在没有的商品 所以 保存购物车的集合是有数据的  数量++就好了
                    int addIndex = (int) msg.obj;
                    //plist中改变数据
                    for (int j = 0; j < clist.size(); j++) {
                        if (buycartshoplist.get(addIndex).getCatPosition() == j) {
                            Map<String, List<ProductBean>> map1 = clist.get(j).getMap();
                            List<ProductBean> list = map1.get(buycartshoplist.get(addIndex).getCategory_id());
                            for (int x = 0; x < list.size(); x++) {
                                if (Integer.parseInt(buycartshoplist.get(addIndex).getSp_id()) == list.get(x).getId()) {
                                    int productCount1 = list.get(x).getProductCount();
                                    productCount1++;
                                    list.get(x).setProductCount(productCount1);
                                    map1.put(buycartshoplist.get(addIndex).getCategory_id(), list);
                                    clist.get(j).setMap(map1);

                                    Log.i("URL", clist.toString());
                                    break;
                                }
                            }
                        }
                    }

                    for (int i = 0; i < buycartshoplist.size(); i++) {
                        if (buycartshoplist.get(addIndex).getSp_id().equals(buycartshoplist.get(i).getSp_id())) {
                            int acount = Integer.parseInt(buycartshoplist.get(i).getSp_count());
                            acount++;
                            buycartshoplist.get(i).setSp_count(acount + "");
                            break;
                        }
                    }


                    for (int l = 0; l < buyShoppingList.size(); l++) {
                        if (buycartshoplist.get(addIndex).getSp_id().equals(buyShoppingList.get(l).getId() + "")) {
                            int acoun = Integer.parseInt(buyShoppingList.get(l).getShop_Count());
                            acoun++;
                            buyShoppingList.get(l).setShop_Count(acoun + "");
                            break;
                        }
                    }
                    int mcount = 0;
                    double mprice = 0.0;
                    for (int m = 0; m < buycartshoplist.size(); m++) {
                        mcount += Integer.parseInt(buycartshoplist.get(m).getSp_count());
                        mprice += Double.parseDouble(buycartshoplist.get(m).getSp_price()) * Integer.parseInt(buycartshoplist.get(m).getSp_count());
                    }
                    shopCount.setText("" + mcount);
                    checkCount.setText("已选" + buycartshoplist.size() + "种商品");
                    selectedgoodsmoney.setText(DoubleUtils.setSSWRDouble(mprice));
                    pop_price.setText(DoubleUtils.setSSWRDouble(mprice));
                    plAdapter.notifyDataSetChanged();
                    listAdapter.notifyDataSetChanged();
                    rcpAdapter.notifyDataSetChanged();
                    break;
                case 201:
                    //popwindow 里 减少按钮
                    int lessenIndex = (int) msg.obj;
                    for (int j = 0; j < clist.size(); j++) {
                        if (buycartshoplist.get(lessenIndex).getCatPosition() == j) {
                            Map<String, List<ProductBean>> map2 = clist.get(j).getMap();
                            List<ProductBean> list = map2.get(buycartshoplist.get(lessenIndex).getCategory_id());
                            for (int x = 0; x < list.size(); x++) {
                                if (Integer.parseInt(buycartshoplist.get(lessenIndex).getSp_id()) == list.get(x).getId()) {
                                    int productCou = list.get(x).getProductCount();
                                    productCou--;
                                    if (productCou < 0) {
                                        list.get(x).setProductCount(0);
                                    } else {
                                        list.get(x).setProductCount(productCou);
                                    }
                                    map2.put(buycartshoplist.get(lessenIndex).getCategory_id(), list);
                                    clist.get(j).setMap(map2);
                                    break;
                                }
                            }
                        }
                    }
                    //第一种做法
                    for (int i = 0; i < buyShoppingList.size(); i++) {
                        if (buycartshoplist.get(lessenIndex).getSp_id().equals(buyShoppingList.get(i).getId() + "")) {
                            int acount = Integer.parseInt(buyShoppingList.get(i).getShop_Count());
                            if (acount > 0) {
                                acount--;
                                buyShoppingList.get(i).setShop_Count(acount + "");
                                if (acount == 0) {
                                    buyShoppingList.remove(i);
                                }
                            }
                            break;
                        }
                    }
                    int lcount = Integer.parseInt(buycartshoplist.get(lessenIndex).getSp_count());
                    if (lcount > 0) {
                        lcount--;
                        buycartshoplist.get(lessenIndex).setSp_count(lcount + "");
                        if (lcount == 0) {
                            buycartshoplist.remove(lessenIndex);
                        }
                    }
                    double oldmoney = 0;
                    for (int m = 0; m < buycartshoplist.size(); m++) {
                        oldmoney += Double.parseDouble(buycartshoplist.get(m).getSp_price()) * Integer.parseInt(buycartshoplist.get(m).getSp_count());
                    }
                    selectedgoodsmoney.setText(DoubleUtils.setSSWRDouble(oldmoney));
                    pop_price.setText(DoubleUtils.setSSWRDouble(oldmoney));
                    int mcoun = 0;
                    for (int m = 0; m < buycartshoplist.size(); m++) {
                        mcoun += Integer.parseInt(buycartshoplist.get(m).getSp_count());
                    }
                    shopCount.setText(mcoun + "");
                    if (buycartshoplist != null && buycartshoplist.size() > 0) {
                        checkCount.setText("商品" + buycartshoplist.size() + "种");
                    } else {
                        checkCount.setText("请选择商品");
                    }
                    if (buyShoppingList.size() == 0) {
                        showOrderList.setVisibility(View.GONE);
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                        }
                    }
                    plAdapter.notifyDataSetChanged();
                    listAdapter.notifyDataSetChanged();
                    break;
                case 202:
                    int cinex = (int) msg.obj;
                    if (buycartshoplist.get(cinex).isSp_check()) {
                        buycartshoplist.get(cinex).setSp_check(false);
                    } else {
                        buycartshoplist.get(cinex).setSp_check(true);
                    }
                    listAdapter.notifyDataSetChanged();
                    break;
                case 511:
                    //Toast.makeText(OrderListActvity.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    String error = (String) msg.obj;
                    Log.i("URL", "error=====================" + error);
                    loading_dailog.dismiss();
                    break;
                case 100000:
                    //================2018-05-10有问题=========
                    for (int i = 0; i < clist.size(); i++) {
                        Map<String, List<ProductBean>> map1 = clist.get(i).getMap();
                        List<ProductBean> p = map1.get(clist.get(i).getId() + "");
                        for (int j = 0; j < p.size(); j++) {
                            p.get(j).setProductCount(0);
                        }
                    }
                    buycartshoplist.clear();
                    buyShoppingList.clear();
                    showOrderList.setVisibility(View.INVISIBLE);
                    shopCount.setText("0");
                    selectedgoodsmoney.setText("0.00");
                    settlement_.setTextColor(Color.parseColor("#cecece"));
                    myCheckstandGray(false);
                    initGetData();
                    rcpAdapter.notifyDataSetChanged();
                    plAdapter.notifyDataSetChanged();
                    break;

                case 2000:
                    //添加
                    plist.clear();
                    ShopMessageBean shopMessageBean = (ShopMessageBean) msg.obj;
                    ProductBean productBean = schangeClist(Integer.parseInt(shopMessageBean.getCategory_id()), Integer.parseInt(shopMessageBean.getSp_id()));
                    for (int i = 0; i < buyShoppingList.size(); i++) {
                        int id = buyShoppingList.get(i).getId();
                        if (productBean.getId() == id) {
                            int count = Integer.parseInt(buyShoppingList.get(i).getShop_Count());
                            count++;
                            buyShoppingList.get(i).setShop_Count(count + "");
                            break;
                        }
                    }
                    setPlist(productBean.getCategory_pos(), productBean.getCategory_id());
                    double smoney = 0;
                    int snum = 0;
                    for (int j = 0; j < buyShoppingList.size(); j++) {
                        smoney += Integer.parseInt(buyShoppingList.get(j).getShop_Count()) * Double.parseDouble(buyShoppingList.get(j).getPrice());
                        snum += Integer.parseInt(buyShoppingList.get(j).getShop_Count());
                    }
                    shopCount.setText(snum + "");
                    selectedgoodsmoney.setText(DoubleUtils.setDouble(smoney));
                    rcpAdapter.setCheckPosition(productBean.getCategory_pos());

                    plAdapter.notifyDataSetChanged();
                    break;
                case 2001:
                    //减少
                    ShopMessageBean shopMessageBean1 = (ShopMessageBean) msg.obj;
                    plist.clear();
                    ProductBean productBean1 = dchangeClist(Integer.parseInt(shopMessageBean1.getCategory_id()), Integer.parseInt(shopMessageBean1.getSp_id()));
                    for (int i = 0; i < buyShoppingList.size(); i++) {
                        int id = buyShoppingList.get(i).getId();
                        if (productBean1.getId() == id) {
                            int count = Integer.parseInt(buyShoppingList.get(i).getShop_Count());
                            count--;
                            buyShoppingList.get(i).setShop_Count(count + "");
                            if (count == 0) {
                                buyShoppingList.remove(i);
                            }
                            break;
                        }
                    }
                    setPlist(productBean1.getCategory_pos(), productBean1.getCategory_id());
                    double smone = 0;
                    int snu = 0;
                    for (int j = 0; j < buyShoppingList.size(); j++) {
                        smone += Integer.parseInt(buyShoppingList.get(j).getShop_Count()) * Double.parseDouble(buyShoppingList.get(j).getPrice());
                        snu += Integer.parseInt(buyShoppingList.get(j).getShop_Count());
                    }
                    if (buyShoppingList.size() > 0) {
                        showOrderList.setVisibility(View.VISIBLE);
                    } else {
                        showOrderList.setVisibility(View.GONE);
                    }
                    shopCount.setText(snu + "");
                    selectedgoodsmoney.setText(DoubleUtils.setDouble(smone));
                    rcpAdapter.setCheckPosition(productBean1.getCategory_pos());
                    plAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private int shopBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        //注册广播接受器
        EventBus.getDefault().register(this);
        mContent = OrderListActvity.this;
        buycartshoplist = new ArrayList<>();
        initView();
        initGetUserInfo();
        initGetData();
        action();
    }

    private void initRecycleView() {
        //默认选中第一个
        //先要清空数据 因为从服务器中获取的数据已经保存在了 clist中 所以为了不使商品重复及要把plist给清空
        plist.clear();
        if (clist.size() > 0 && clist != null) {
            Log.i("URL", "clist:::" + clist.toString());
            Map<String, List<ProductBean>> map = clist.get(0).getMap();
            List<ProductBean> list = map.get(clist.get(0).getId() + "");
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    ProductBean productBean = list.get(i);
                    productBean.setCategory_pos(0);
                    plist.add(productBean);
                }
            }
        }
        rcpAdapter = new RecycleProductCategoryListAdapter(clist, OrderListActvity.this);
        mGoodsCateGoryList.setLayoutManager(new LinearLayoutManager(OrderListActvity.this));
        mGoodsCateGoryList.setAdapter(rcpAdapter);
        rcpAdapter.setCheckPosition(0);
        mLinearLayoutManager = new LinearLayoutManager(OrderListActvity.this, LinearLayoutManager.VERTICAL, false);
        myLayoutManage = new MyLayoutManage(OrderListActvity.this, LinearLayoutManager.VERTICAL, false);
        mGoodsInfoRecyclerView.setLayoutManager(mLinearLayoutManager);
        plAdapter = new ProductListAdapter(OrderListActvity.this, plist, handler);
        mGoodsInfoRecyclerView.setAdapter(plAdapter);
        //分类的点击事件
        rcpAdapter.setOnItemClickListener(new RecycleProductCategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //切换选中的颜色改变
                rcpAdapter.setCheckPosition(position);
                Log.i("URL", "点击时的数据：：：" + clist);
                plist.clear();
                CatingBean catingBean = clist.get(position);
                Map<String, List<ProductBean>> map = catingBean.getMap();
                List<ProductBean> li = map.get(clist.get(position).getId() + "");
                if (li != null && li.size() > 0) {
                    for (int i = 0; i < li.size(); i++) {
                        ProductBean productBean = li.get(i);
                        productBean.setCategory_pos(position);
                        plist.add(productBean);
                    }
                }
                plAdapter.notifyDataSetChanged();
            }
        });


    }

    private void action() {
        showOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把商品放入到购物车的集合中
                if (buyShoppingList.size() > 0) {
                    setData();
                    setPopWindow();
                } else {
                    Toast.makeText(OrderListActvity.this, "没有商品，请选择", Toast.LENGTH_SHORT).show();
                }
            }
        });
        f_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopWindowMenu();
            }
        });


        ordermenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderListActvity.this, TheOrderListActivity.class));
                mPopupWindowMenu.dismiss();
            }
        });
        systemset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderListActvity.this, SystemSettingsActivity.class));
                mPopupWindowMenu.dismiss();
            }
        });
        quxiaoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindowMenu.dismiss();
            }
        });
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //启动扫码
                Intent intent = new Intent("com.summi.scan");
                intent.setPackage("com.sunmi.sunmiqrcodescanner");
                intent.putExtra("CURRENT_PPI", 0X0003);//当前分辨率
                intent.putExtra("PLAY_SOUND", true);// 扫描完成声音提示  默认true
                intent.putExtra("PLAY_VIBRATE", true);
                //扫描完成震动,默认false，目前M1硬件支持震动可用该配置，V1不支持
                intent.putExtra("IDENTIFY_INVERSE_QR_CODE", true);// 识别反色二维码，默认true
                intent.putExtra("IDENTIFY_MORE_CODE", false);// 识别画面中多个二维码，默认false
                intent.putExtra("IS_SHOW_SETTING", false);// 是否显示右上角设置按钮，默认true
                intent.putExtra("IS_SHOW_ALBUM", false);// 是否显示从相册选择图片按钮，默认true
                startActivityForResult(intent, 520);

            }
        });
        search_head_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                Message message = new Message();
                message.what = 33;
                handler.sendMessage(message);
            }
        });

        jiesaun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCarData();
                Message message = new Message();
                message.what = 120;
                handler.sendMessage(message);
            }
        });

        if (refresh_layout != null) {
            refresh_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (userInfo == null) {
                        return;
                    }
                    REFRESH_CODE = 1;
                    String timestamp = System.currentTimeMillis() + "";
                    String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
                    Map<String, String> loginMap = new HashMap<String, String>();
                    loginMap.put("organization_id", userInfo.getOrganization_id());
                    loginMap.put("account_id", userInfo.getAccount_id());
                    Log.i("URL", "token" + token);
                    loginMap.put("token", token);
                    loginMap.put("timestamp", timestamp);
                    Log.i("URL", "timestamp" + timestamp);
                    if (!NetworkUtil.isNetworkAvailable(mContent)) {
                        Toast.makeText(OrderListActvity.this, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loading_dailog = LoadingUtils.getDailog(mContent, Color.RED, "刷新数据中。。。。");
                    loading_dailog.show();
                    NetUtils.netWorkByMethodPost(mContent, loginMap, IpConfig.URL_GOODSLIST, handler, 0);
                }
            });
        }
    }

    private void setCarData() {
        buycartshoplist.clear();
        for (int i = 0; i < buyShoppingList.size(); i++) {
            ShopMessageBean smb = new ShopMessageBean();
            smb.setSp_check(true);
            smb.setSp_Detail(buyShoppingList.get(i).getDetails());
            smb.setSp_price(buyShoppingList.get(i).getPrice());
            smb.setSp_count(buyShoppingList.get(i).getShop_Count());
            int scount = Integer.parseInt(buyShoppingList.get(i).getShop_Count());
            double lmoney = Double.parseDouble(buyShoppingList.get(i).getPrice());
            if (buyShoppingList.get(i).getThumb() != null && buyShoppingList.get(i).getThumb().size() > 0) {
                smb.setSp_picture_url(IpConfig.URL_GETPICTURE + buyShoppingList.get(i).getThumb().get(0).getThumb());
            }
            smb.setCategory_id(buyShoppingList.get(i).getCategory_id() + "");
            smb.setSp_name(buyShoppingList.get(i).getName());
            smb.setSp_id(buyShoppingList.get(i).getId() + "");
            buycartshoplist.add(smb);
        }
    }

    /**
     * 数据的倒换
     */
    private void setData() {
        //先清展示购物车的数据
        buycartshoplist.clear();
        double smoney = 0.00;
        for (int i = 0; i < buyShoppingList.size(); i++) {
            ShopMessageBean smb = new ShopMessageBean();
            smb.setSp_check(true);
            smb.setSp_Detail(buyShoppingList.get(i).getDetails());
            smb.setSp_price(buyShoppingList.get(i).getPrice());
            smb.setSp_count(buyShoppingList.get(i).getShop_Count());
            int scount = Integer.parseInt(buyShoppingList.get(i).getShop_Count());
            double lmoney = Double.parseDouble(buyShoppingList.get(i).getPrice());
            smoney += scount * lmoney;
            if (buyShoppingList.get(i).getThumb().size() > 0) {
                smb.setSp_picture_url(IpConfig.URL_GETPICTURE + buyShoppingList.get(i).getThumb().get(0).getThumb());
            } else {
                smb.setSp_picture_url("");
            }
            smb.setCategory_id(buyShoppingList.get(i).getCategory_id() + "");
            smb.setSp_name(buyShoppingList.get(i).getName());
            smb.setSp_id(buyShoppingList.get(i).getId() + "");
            smb.setCatPosition(buyShoppingList.get(i).getCatPosition());
            buycartshoplist.add(smb);
        }
        //popwindow-》商品列表
        if (shopview == null) {
            shopview = LayoutInflater.from(OrderListActvity.this).inflate(R.layout.activity_shopping_cart_pop_, null);
        }
//        allcheck = (CheckBox) shopview.findViewById(R.id.allCheckGoods);
        checkCount = shopview.findViewById(R.id.checkCount);
        if (buycartshoplist.size() > 0) {
            checkCount.setText(buycartshoplist.size() + "种商品");
        }
        clear_cart = shopview.findViewById(R.id.clear_cart);
        shoplistview = shopview.findViewById(R.id.shoplistview);
        pop_price = shopview.findViewById(R.id.pop_price);
        cart_logo = shopview.findViewById(R.id.cart_logo);
        //结算
        settlement = shopview.findViewById(R.id.settlement);

        pop_price.setText("￥" + DoubleUtils.setSSWRDouble(smoney));
        if (buycartshoplist != null) {
            listAdapter = new ListGoodsDetails_Adapter(OrderListActvity.this, buycartshoplist, handler);
            shoplistview.setAdapter(listAdapter);
        }

        clear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);

            }
        });
        cart_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        settlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 110;
                handler.sendMessage(message);

            }
        });
    }

    private void initGetData() {
        if (userInfo == null) {
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> loginMap = new HashMap<String, String>();
        loginMap.put("organization_id", userInfo.getOrganization_id());
        loginMap.put("account_id", userInfo.getAccount_id());
        Log.i("URL", "token" + token);
        loginMap.put("token", token);
        loginMap.put("timestamp", timestamp);
        Log.i("URL", "timestamp" + timestamp);
        if (!NetworkUtil.isNetworkAvailable(mContent)) {
            Toast.makeText(OrderListActvity.this, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContent, Color.RED, "获取数据中。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(mContent, loginMap, IpConfig.URL_GOODSLIST, handler, 0);

    }

    /**
     * 获取用户信息
     */
    private void initGetUserInfo() {
        UserInfoImpl userInfoImpl = new UserInfoImpl(OrderListActvity.this);
        try {
            userInfo = userInfoImpl.getUserInfo("10");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * view的初始化
     */
    private void initView() {

        search_head_btn = (LinearLayout) findViewById(R.id.search_head_btn);
        jiesaun = (LinearLayout) findViewById(R.id.jiesaun);
        scan_btn = (LinearLayout) findViewById(R.id.scan_btn);
        showOrderList = (RelativeLayout) findViewById(R.id.showOrderList);
        parentView = (LinearLayout) findViewById(R.id.shoppingcart);
        f_menu = (LinearLayout) findViewById(R.id.f_menu);
        shopCount = (TextView) findViewById(R.id.goodsCount);
        mGoodsCateGoryList = (RecyclerView) findViewById(R.id.goods_category_list);
        mGoodsInfoRecyclerView = (RecyclerView) findViewById(R.id.goods_recycleView);
        selectedgoodsmoney = (TextView) findViewById(R.id.selectedgoodsmoney);
        settlement_ = (TextView) findViewById(R.id.settlement_);
        cashier = (TextView) findViewById(R.id.cashier);

        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
        //popwindow
        //menuViewPopwindow
        if (menuView == null) {
            menuView = LayoutInflater.from(OrderListActvity.this).inflate(R.layout.activity_seting_menu, null);
        }
        refresh_layout = menuView.findViewById(R.id.refresh_layout);
        systemset = menuView.findViewById(R.id.systemset);
        ordermenu = menuView.findViewById(R.id.ordermenu);
        quxiaoMenu = menuView.findViewById(R.id.quxiao);
        //menuViewPopwindow


    }

    //这个是有用的需要改动
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initData(List<GoodsCategroyListBean.DataBean.CategorylistBean> list) {
        mGoodsCategoryListAdapter = new RecycleGoodsCategoryListAdapter(catelist, OrderListActvity.this);
        mGoodsCateGoryList.setLayoutManager(new LinearLayoutManager(OrderListActvity.this));
        mGoodsCateGoryList.setAdapter(mGoodsCategoryListAdapter);
        mGoodsCategoryListAdapter.setOnItemClickListener(new RecycleGoodsCategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mGoodsInfoRecyclerView.scrollToPosition(titlePois.get(position));
                mGoodsCategoryListAdapter.setCheckPosition(position);
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(OrderListActvity.this, LinearLayoutManager.VERTICAL, false);
        myLayoutManage = new MyLayoutManage(OrderListActvity.this, LinearLayoutManager.VERTICAL, false);
        mGoodsInfoRecyclerView.setLayoutManager(mLinearLayoutManager);
        personAdapter = new PersonAdapter(OrderListActvity.this, goodsitemlist, catelist, handler);
        personAdapter.setmActivity(OrderListActvity.this);
        mGoodsInfoRecyclerView.setAdapter(personAdapter);
        mGoodsInfoRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                for (int i = 0; i < titlePois.size(); i++) {
                    if (mLinearLayoutManager.findFirstVisibleItemPosition() >= titlePois.get(i)) {
                        mGoodsCategoryListAdapter.setCheckPosition(i);
                    }
                }
            }
        });
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
        loading_dailog = LoadingUtils.getDailog(OrderListActvity.this, Color.RED, "获取数据中。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(OrderListActvity.this, loginMap, IpConfig.URL_CATEGORY, handler, 10);

    }

    public void setPopWindow() {

        mPopupWindow = new PopupWindow(shopview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    public void setPopWindowMenu() {
        mPopupWindowMenu = new PopupWindow(menuView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindowMenu.setTouchable(true);
        mPopupWindowMenu.setOutsideTouchable(false);
        mPopupWindowMenu.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindowMenu.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (requestCode == 520 && data != null) {
            Bundle bundle = data.getExtras();
            ArrayList<HashMap<String, String>> result = (ArrayList<HashMap<String, String>>) bundle.getSerializable("data");
            Iterator<HashMap<String, String>> it = result.iterator();
            while (it.hasNext()) {
                HashMap<String, String> hashMap = it.next();
                Log.w("URL", "扫描结果：：：" + hashMap.get("VALUE"));
                intoSearchGoods(hashMap.get("VALUE"));
            }
        }
    }

    /**
     * 这个是扫码后的结果
     *
     * @param result
     */
    private void intoSearchGoods(String result) {
        Map<String, String> map = MapUtilsSetParam.getMap(OrderListActvity.this);
        Log.i("URL", "扫码的值：：：" + result);
        map.put("scan_code", result);
        loading_dailog = LoadingUtils.getDailog(OrderListActvity.this, Color.RED, "搜索中。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(OrderListActvity.this, map, IpConfig.URL_SERACH, handler, 12);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                customDialog();
                break;
        }
        return true;
    }

    /**
     * 自定义对话框
     */
    private void customDialog() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_out_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListActvity.this.finish();
                System.exit(0);
            }
        });
        dialog.show();
    }

    /**
     * 接收广播发送过来的信息
     *
     * @param refreshBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(RefreshBean refreshBean) {
        //接收到清空购车的信息了
        if (refreshBean.getRefreshCode() == ContantData.REFRESH_ONE) {
            Message message = new Message();
            message.what = 100000;
            Log.i("BBBB", "接受广播的信息：：：" + refreshBean.getRefreshName());
            message.obj = refreshBean.getRefreshName();
            handler.sendMessage(message);

        }
        refresh_layout = menuView.findViewById(R.id.refresh_layout);
        systemset = menuView.findViewById(R.id.systemset);
        ordermenu = menuView.findViewById(R.id.ordermenu);
        quxiaoMenu = menuView.findViewById(R.id.quxiao);
        myCheckstandGray(false);
    }

    /**
     * 接收到搜索框里的加减按钮
     *
     * @param searchBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setSearchBean(SearchBean searchBean) {
        int code = searchBean.getCode();
        Message message = new Message();
        if (code == 200) {
            //增加
            message.what = 2000;
            message.obj = searchBean.getShopMessageBean();
        } else if (code == 201) {
            //减少
            message.what = 2001;
            message.obj = searchBean.getShopMessageBean();
        }
        handler.sendMessage(message);
    }


    /**
     * 接收搜索框的添加数据的商品信息
     *
     * @param goodsOrder
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void freshBuy(SetDataGoodsOrder goodsOrder) {
        //接收到清空购车的信息了
        if (goodsOrder.getCode() == 0) {
            Message message = new Message();
            message.what = 207;
            message.obj = goodsOrder.getShopBean();
            handler.sendMessage(message);
        }
    }


    /**
     * 改变收银台颜色
     *
     * @param selecteColor true为灰，false为正常色
     */
    public void myCheckstandGray(boolean selecteColor) {
        if (!selecteColor) {
            settlement_.setTextColor(Color.parseColor("#cecece"));
            cashier.setTextColor(Color.parseColor("#cecece"));
        } else {
            settlement_.setTextColor(Color.parseColor("#000000"));
            cashier.setTextColor(Color.parseColor("#383638"));
        }
    }

    /**
     * 页面摧毁时关闭广播注册
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消广播的注册
        EventBus.getDefault().unregister(this);
    }

    public ShopBean setShopBean(int pos) {
        ShopBean sb = new ShopBean();
        sb.setShop_Count(plist.get(pos).getProductCount() + "");
        sb.setName(plist.get(pos).getName());
        sb.setCategory_id(plist.get(pos).getCategory_id());
        sb.setCategory_name(plist.get(pos).getCategory_name());
        sb.setDetails(plist.get(pos).getDetails());
        sb.setId(plist.get(pos).getId());
        sb.setPrice(plist.get(pos).getPrice());
        sb.setStock(plist.get(pos).getStock());
        sb.setCatPosition(plist.get(pos).getCategory_pos());
        List<ProductBean.ThumbBean> thumb = plist.get(pos).getThumb();
        List<ShopBean.ThumbBean> tlist = new ArrayList<>();
        for (int i = 0; i < thumb.size(); i++) {
            ShopBean.ThumbBean tb = new ShopBean.ThumbBean();
            tb.setThumb(thumb.get(i).getThumb());
            tlist.add(tb);
        }
        sb.setThumb(tlist);
        return sb;
    }

    public int getShopBean() {
        return shopBean;
    }

    public int changeClist(int category_id, int id) {
        for (int i = 0; i < clist.size(); i++) {
            if (category_id == clist.get(i).getId()) {
                //这个我string是分类id
                Map<String, List<ProductBean>> map1 = clist.get(i).getMap();
                List<ProductBean> pblist = map1.get(category_id + "");
                for (int x = 0; x < pblist.size(); x++) {
                    if (id == pblist.get(x).getId()) {
                        int pcount = pblist.get(x).getProductCount();
                        Log.i("URL", "pcount" + pcount);
                        pcount++;
                        pblist.get(x).setProductCount(pcount);
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    public ProductBean schangeClist(int category_id, int id) {
        for (int i = 0; i < clist.size(); i++) {
            if (category_id == clist.get(i).getId()) {
                //这个我string是分类id
                Map<String, List<ProductBean>> map1 = clist.get(i).getMap();
                List<ProductBean> pblist = map1.get(category_id + "");
                for (int x = 0; x < pblist.size(); x++) {
                    if (id == pblist.get(x).getId()) {
                        int pcount = pblist.get(x).getProductCount();
                        Log.i("URL", "pcount" + pcount);
                        pcount++;
                        pblist.get(x).setProductCount(pcount);
                        return pblist.get(x);
                    }
                }
            }
        }
        return null;
    }


    /**
     * 减少商品
     * @param category_id
     * @param id
     * @return
     */
    public ProductBean dchangeClist(int category_id, int id) {
        for (int i = 0; i < clist.size(); i++) {
            if (category_id == clist.get(i).getId()) {
                //这个我string是分类id
                Map<String, List<ProductBean>> map1 = clist.get(i).getMap();
                List<ProductBean> pblist = map1.get(category_id + "");
                for (int x = 0; x < pblist.size(); x++) {
                    if (id == pblist.get(x).getId()) {
                        int pcount = pblist.get(x).getProductCount();
                        Log.i("URL", "pcount" + pcount);
                        pcount--;
                        pblist.get(x).setProductCount(pcount);
                        return pblist.get(x);
                    }
                }
            }
        }
        return null;
    }

    public void setPlist(int cposition, int category_id) {
        List<ProductBean> pBeen = clist.get(cposition).getMap().get(category_id + "");
        if (pBeen != null) {
            for (int j = 0; j < pBeen.size(); j++) {
                plist.add(pBeen.get(j));
            }
        }
    }

    /**
     * 处理滑动 是两个ListView联动
     * 需要改动
     */
    private class MyOnGoodsScrollListener extends RecyclerView.OnScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (!(lastTitlePoi == goodsitemlist.get(firstVisibleItem).getId())) {
                lastTitlePoi = goodsitemlist.get(firstVisibleItem).getId();
                mGoodsCategoryListAdapter.setCheckPosition(goodsitemlist.get(firstVisibleItem).getId());
                mGoodsCategoryListAdapter.notifyDataSetChanged();
            }
        }
    }


    //封装

    class MyLayoutManage extends LinearLayoutManager {

        public MyLayoutManage(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            if (getChildCount() > 0) {
                View view = recycler.getViewForPosition(0);
                if (view != null) {
                    measureChild(view, widthSpec, heightSpec);
                    int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                    int measuredHeight = view.getMeasuredHeight();
                    setMeasuredDimension(measuredWidth, measuredHeight);
                }
            } else {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        }
    }
}
