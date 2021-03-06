package com.zerone_catering.adapter.cart_list;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zerone_catering.R;
import com.zerone_catering.domain.shoplistbean.ShopMessageBean;

import java.util.List;

/**
 * Created by on 2017/12/29 0029 15 44.
 * Author  LiuXingWen
 */

public class ListGoodsDetails_Adapter extends BaseAdapter {
    private Context mContext;
    private List<ShopMessageBean> list;
    private LayoutInflater inflate;
    private Handler handler;
    private OnClickAddGoods mOnClickAddGoods;//声明接口
    private OnClickLessenGoods mOnClickLessenGoods;//声明接口

    public ListGoodsDetails_Adapter(Context context, List<ShopMessageBean> list, Handler handler) {
        this.mContext = context;
        this.list = list;
        this.inflate = LayoutInflater.from(context);
        this.handler = handler;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (list != null) {
            return position;
        }
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflate.inflate(R.layout.goodslistitem_, null);
//            holder.checkshop = (CheckBox) convertView.findViewById(R.id.checkshop);
            holder.shop_img = convertView.findViewById(R.id.picture);
            holder.shop_name = convertView.findViewById(R.id.shopname);
            holder.shopdiscount = convertView.findViewById(R.id.shopdiscount);
            holder.shop_price = convertView.findViewById(R.id.shopprice);
            holder.shop_count = convertView.findViewById(R.id.shopCount);
            holder.decrease_shop = convertView.findViewById(R.id.decrease_shop);
            holder.add_shop = convertView.findViewById(R.id.add_shop);
            convertView.setTag(holder);
        } else {
            //直接通过holder获取下面三个子控件，不必使用findviewbyid，加快了 UI 的响应速度
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.checkshop.setChecked(list.get(position).isSp_check());
        holder.shop_name.setText(list.get(position).getSp_name());
        holder.shopdiscount.setText(list.get(position).getSp_Detail());
        holder.shop_price.setText(list.get(position).getSp_price());
        holder.shop_count.setText(list.get(position).getSp_count());
        String url = list.get(position).getSp_picture_url();
//        Log.i("URL", "搜索：：：" + url);
        Glide.with(mContext).load(url).centerCrop().placeholder(R.mipmap.app_logo).crossFade().into(holder.shop_img);

        /**
         *
         * 减少按钮的点击
         *
         */
        holder.decrease_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 201;
                message.obj = position;
                handler.sendMessage(message);
            }
        });

        /**
         *
         * 添加按钮
         *
         */
        holder.add_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message message = new Message();
                message.what = 200;
                message.obj = position;
                handler.sendMessage(message);
            }
        });
        return convertView;
    }

    /**
     * 对外开放减少商品的接口
     *
     * @param OnClickAddGoods
     */
    public void setOnClickAddGoods(OnClickAddGoods OnClickAddGoods) {
        OnClickAddGoods = OnClickAddGoods;
    }

    /**
     * 对外开放减少商品的接口
     *
     * @param mOnClickLessenGoods
     */
    public void setOnClickLessenGoods(OnClickLessenGoods mOnClickLessenGoods) {
        mOnClickLessenGoods = mOnClickLessenGoods;
    }


    /**
     * 添加的按钮
     */
    public interface OnClickAddGoods {
        void OnClickAddGoods(View view, int position);
    }

    /**
     * 减少的按钮
     */
    public interface OnClickLessenGoods {
        void OnClickAddGoods(View view, int position);
    }

    static class ViewHolder {
        //        CheckBox checkshop;
        TextView shop_name;
        ImageView shop_img;
        TextView shop_count;
        TextView shopdiscount;
        TextView shop_price;
        LinearLayout decrease_shop;
        LinearLayout add_shop;
    }
}
