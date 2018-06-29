package com.zerone_catering.avtivity.openorderpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.domain.main.CatingBean;
import com.zerone_catering.domain.main.ProductBean;
import com.zerone_catering.domain.shoplistbean.ShopBean;
import com.zerone_catering.domain.shoplistbean.ShopMessageBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/5/31 0031 10 30.
 * Author  LiuXingWen
 */

public class OrderPublicMethod {
    /**
     * 减少商品
     *
     * @param category_id 分类id
     * @param id          商品id
     * @param clist       分类列表集合
     * @return
     */
    public static ProductBean dchangeClist(int category_id, int id, List<CatingBean> clist) {
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

    /**
     * @param pos   位置
     * @param plist 商品列表
     * @return
     */
    public static ShopBean setShopBean(int pos, List<ProductBean> plist) {
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
        if (tlist != null) {
            for (int i = 0; i < thumb.size(); i++) {
                ShopBean.ThumbBean tb = new ShopBean.ThumbBean();
                tb.setThumb(thumb.get(i).getThumb());
                tlist.add(tb);
            }
            sb.setThumb(tlist);
        }
        return sb;
    }

    /**
     * @param cposition   在分类中的位置
     * @param category_id 分类id
     * @param clist       分类列表
     * @param plist       商品列表
     */
    public static void setPlist(int cposition, int category_id, List<CatingBean> clist, List<ProductBean> plist) {
        List<ProductBean> pBeen = clist.get(cposition).getMap().get(category_id + "");
        if (pBeen != null) {
            for (int j = 0; j < pBeen.size(); j++) {
                plist.add(pBeen.get(j));
            }
        }
    }


    /**
     * @param category_id
     * @param id
     * @param clist
     * @return
     */
    public static int changeClist(int category_id, int id, List<CatingBean> clist) {
        for (int i = 0; i < clist.size(); i++) {
            if (category_id == clist.get(i).getId()) {
                //这个我string是分类id
                Map<String, List<ProductBean>> map1 = clist.get(i).getMap();
                List<ProductBean> pblist = map1.get(category_id + "");
                for (int x = 0; x < pblist.size(); x++) {
                    if (id == pblist.get(x).getId()) {
                        int pcount = pblist.get(x).getProductCount();
                        pcount++;
                        pblist.get(x).setProductCount(pcount);
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * @param category_id
     * @param id
     * @return
     */
    public static ProductBean schangeClist(int category_id, int id, List<CatingBean> clist) {
        for (int i = 0; i < clist.size(); i++) {
            if (category_id == clist.get(i).getId()) {
                //这个我string是分类id
                Map<String, List<ProductBean>> map1 = clist.get(i).getMap();
                List<ProductBean> pblist = map1.get(category_id + "");
                for (int x = 0; x < pblist.size(); x++) {
                    if (id == pblist.get(x).getId()) {
                        int pcount = pblist.get(x).getProductCount();
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
     * 改变收银台颜色
     *
     * @param selecteColor    true为灰，false为正常色
     * @param settlement_name
     * @param cashier
     */
    public static void myCheckstandGray(boolean selecteColor, TextView settlement_name, TextView cashier) {
        if (!selecteColor) {
            settlement_name.setTextColor(Color.parseColor("#cecece"));
            cashier.setTextColor(Color.parseColor("#cecece"));
        } else {
            settlement_name.setTextColor(Color.parseColor("#000000"));
            cashier.setTextColor(Color.parseColor("#383638"));
        }
    }


    /**
     * @param context    上下文
     * @param shopview   popwindo的view
     * @param parentView
     * @return
     */
    public static PopupWindow setPopWindow(Context context, View shopview, View parentView) {
        PopupWindow mPopupWindow = new PopupWindow(shopview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        mPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        return mPopupWindow;
    }

    /**
     * 这个是按钮的设置view
     *
     * @param context
     * @param menuView
     * @param parentView
     * @return
     */
    public static PopupWindow setPopWindowMenu(Context context, View menuView, View parentView) {
        PopupWindow mPopupWindowMenu = new PopupWindow(menuView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindowMenu.setTouchable(true);
        mPopupWindowMenu.setOutsideTouchable(false);
        mPopupWindowMenu.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        mPopupWindowMenu.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        return mPopupWindowMenu;
    }

    /**
     * 设置购物车
     *
     * @param buycartshoplist
     * @param buyShoppingList
     */
    public static void setCarData(List<ShopMessageBean> buycartshoplist, List<ShopBean> buyShoppingList) {
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

}
