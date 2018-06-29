package com.zerone_catering.adapter.cart_list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.domain.order.Order_Cashier_Details_Bean;

import java.util.List;

/**
 * Created by on 2017/12/29 0029 15 44.
 * Author  LiuXingWen
 */

public class CheckCancel_OrderListItemAdapter extends BaseAdapter {
    private Context context;
    private List<Order_Cashier_Details_Bean> list;
    private LayoutInflater inflate;

    public CheckCancel_OrderListItemAdapter(Context context, List<Order_Cashier_Details_Bean> list) {
        this.context = context;
        this.list = list;
        this.inflate = LayoutInflater.from(context);
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
            convertView = inflate.inflate(R.layout.activity_orderdetailslistitem, null);
            holder.goodsCount = convertView.findViewById(R.id.goodscount);
            holder.goodsName = convertView.findViewById(R.id.goodsname);
            holder.goodsMoney = convertView.findViewById(R.id.goodsmoney);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            holder.root = convertView.findViewById(R.id.root);
            convertView.setTag(holder);
        } else {
            //直接通过holder获取下面三个子控件，不必使用findviewbyid，加快了 UI 的响应速度
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goodsCount.setText("x" + list.get(position).getTotal());
        holder.goodsName.setText(list.get(position).getTitle());
        holder.goodsMoney.setText("￥" + list.get(position).getPrice());
        holder.checkBox.setChecked(list.get(position).getGoods_ckeck());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("URL", holder.checkBox.isChecked() + "");
                list.get(position).setGoods_ckeck(holder.checkBox.isChecked());
            }
        });
        return convertView;
    }

    static class ViewHolder {
        LinearLayout root;
        TextView goodsCount;
        TextView goodsName;
        TextView goodsMoney;
        CheckBox checkBox;
    }
}
