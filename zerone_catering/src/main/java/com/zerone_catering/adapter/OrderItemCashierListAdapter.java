package com.zerone_catering.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.avtivity.details.OrderDetailsDFKActivity;
import com.zerone_catering.domain.payorderlistbean.OrderCashierListBean;
import com.zerone_catering.utils.UtilsTime;

import java.util.List;


/**
 * Created by dalong on 2017/1/13.
 */

public class OrderItemCashierListAdapter extends RecyclerView.Adapter<OrderItemCashierListAdapter.ViewHolder> {

    public Context mContext;
    OnItemClickListener mOnItemClickListener;
    //当前选中的位置
    private int selectPosition;
    private List<OrderCashierListBean> clist;
    private Handler handler;

    public OrderItemCashierListAdapter(List<OrderCashierListBean> clist, Context context, Handler handler) {
        this.clist = clist;
        this.mContext = context;
        this.handler = handler;
    }

    /**
     * 数据发生改变的方法
     *
     * @param clist 发生改变的集合类
     */
    public void changeData(List<OrderCashierListBean> clist) {
        this.clist = clist;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.order_number.setText(clist.get(position).getOrdersn());
        holder.ordercheck.setChecked(clist.get(position).isOchecklean());
        holder.orderTime.setText(UtilsTime.getTime(Long.parseLong(clist.get(position).getCreated_at())));
        holder.order_money.setText("￥：" + clist.get(position).getOrder_price());
        /**
         * 对外开放点击接口
         */
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            }
        });

        holder.ordercheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clist.get(position).setOchecklean(!clist.get(position).isOchecklean());
                Message message = new Message();
                message.what = 1;
                message.obj = position;
                handler.sendMessage(message);
            }
        });
        holder.lookover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailsDFKActivity.class);
                intent.putExtra("orderInfo", clist.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return clist.size();
    }

    /**
     * 设置选中index
     *
     * @param position 选中分类的下标
     */
    public void setCheckPosition(int position) {
        this.selectPosition = position;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView number;
        public final TextView orderTime;
        public final TextView order_number;
        public final CheckBox ordercheck;
        public final View root;
        public final TextView order_money;
        public final TextView lookover;

        public ViewHolder(View root) {
            super(root);
            order_number = root.findViewById(R.id.order_number);
            number = root.findViewById(R.id.number);
            ordercheck = root.findViewById(R.id.ordercheck);
            orderTime = root.findViewById(R.id.create_ordertime);
            order_money = root.findViewById(R.id.order_money);
            lookover = root.findViewById(R.id.lookover);
            this.root = root;
        }
    }
}
