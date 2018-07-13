package com.zerone_catering.adapter.cart_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zerone_catering.R;
import com.zerone_catering.domain.ForThePaymentBean;

import java.util.List;

/**
 * Created by on 2017/12/29 0029 15 44.
 * Author  LiuXingWen
 */

public class Order_Detials_ForThePayment_ListItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<ForThePaymentBean.DataBean.OrdergoodsBean> ordergoods;
    private LayoutInflater inflate;

    public Order_Detials_ForThePayment_ListItemAdapter(Context context, List<ForThePaymentBean.DataBean.OrdergoodsBean> ordergoods) {
        this.mContext = context;
        this.ordergoods = ordergoods;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (ordergoods != null) {
            return ordergoods.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (ordergoods != null) {
            return ordergoods.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (ordergoods != null) {
            return position;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflate.inflate(R.layout.acticity_expandable_sun, null);
            holder.title = convertView.findViewById(R.id.title);
            holder.total = convertView.findViewById(R.id.total);
            holder.price = convertView.findViewById(R.id.price);
            holder.iconImg = convertView.findViewById(R.id.iconImg);
            convertView.setTag(holder);
        } else {
            //直接通过holder获取下面三个子控件，不必使用findviewbyid，加快了 UI 的响应速度
            holder = (ViewHolder) convertView.getTag();
        }
        holder.price.setText("￥" + ordergoods.get(position).getPrice());
        holder.total.setText("X" + ordergoods.get(position).getTotal());
        holder.title.setText(ordergoods.get(position).getTitle());
        Glide.with(mContext).load(ordergoods.get(position).getThumb()).into(holder.iconImg);
        return convertView;
    }

    static class ViewHolder {
        public ImageView iconImg;
        public TextView title;
        public TextView total;
        public TextView price;
    }
}
