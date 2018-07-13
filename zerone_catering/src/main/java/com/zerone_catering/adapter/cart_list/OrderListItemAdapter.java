package com.zerone_catering.adapter.cart_list;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.avtivity.manageorderpage.orderlist.TheOrderListBean;
import com.zerone_catering.utils.JavaUtilsNormal;

import java.util.List;

/**
 * Created by on 2017/12/29 0029 15 44.
 * Author  LiuXingWen
 */

public class OrderListItemAdapter extends BaseAdapter {
    private Context context;
    private List<TheOrderListBean.DataBean.OrderListBean> list;
    private LayoutInflater inflate;
    private Handler handler;

    public OrderListItemAdapter(Context context, List<TheOrderListBean.DataBean.OrderListBean> list, Handler handler) {
        this.context = context;
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflate.inflate(R.layout.orderlist_item, null);
            holder.shop_img = convertView.findViewById(R.id.logo_order);
            holder.orderNumber = convertView.findViewById(R.id.ordernumber);
            holder.orderTime = convertView.findViewById(R.id.ordertime);
            holder.orderMoney = convertView.findViewById(R.id.ordermoney);
            holder.orderStates = convertView.findViewById(R.id.orderstates);
            holder.lookoverorder = convertView.findViewById(R.id.lookover);
            holder.discountmoney = convertView.findViewById(R.id.discountmoney);
            holder.paymoney = convertView.findViewById(R.id.paymoney);
            convertView.setTag(holder);
        } else {
            //直接通过holder获取下面三个子控件，不必使用findviewbyid，加快了 UI 的响应速度
            holder = (ViewHolder) convertView.getTag();
        }
        holder.orderNumber.setText(list.get(position).getOrdersn());
        holder.orderTime.setText(JavaUtilsNormal.getTime(Long.parseLong(list.get(position).getCreated_at())));
        holder.orderMoney.setText(list.get(position).getOrder_price());
        holder.discountmoney.setText(list.get(position).getDiscount_price());
        //这个地方需要修改 根据不同的id去判断是什么订单
        //holder.orderStates.setText();

        if ("0".equals(list.get(position).getStatus())) {
            //待付款
            holder.orderStates.setText("待付款");
            holder.paymoney.setText("尚未付款");
            holder.paymoney.setTextColor(Color.parseColor("#ff0000"));
            holder.orderStates.setTextColor(Color.parseColor("#f3454c"));
        } else if ("1".equals(list.get(position).getStatus())) {
            //已付款
            holder.orderStates.setText("已付款");
            holder.orderStates.setTextColor(Color.parseColor("#000000"));
            holder.paymoney.setText("￥" + list.get(position).getPayment_price());
        } else if ("-1".equals(list.get(position).getStatus())) {
            //已取消
            holder.orderStates.setText("已取消");
            holder.orderStates.setTextColor(Color.parseColor("#000000"));
            holder.paymoney.setText("无实收金额");
        }
        holder.lookoverorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //待付款
                Message message = new Message();
                message.what = 20000;
                message.obj = position;
                handler.sendMessage(message);

            }
        });

        return convertView;
    }

    static class ViewHolder {

        TextView orderNumber;
        ImageView shop_img;
        TextView orderTime;
        TextView orderMoney;
        TextView orderStates;
        TextView lookoverorder;
        TextView paymoney;
        TextView discountmoney;

    }
}
