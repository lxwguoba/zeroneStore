package com.zerone_catering.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.domain.payorderlistbean.OrderCashierListBean;
import com.zerone_catering.utils.JavaUtilsNormal;

import java.util.List;


/**
 * Created by dalong on 2017/1/13.
 */

public class Print_Order_Item_ListAdapter extends RecyclerView.Adapter<Print_Order_Item_ListAdapter.ViewHolder> {

    public Context mContext;
    OnItemClickListener mOnItemClickListener;
    //当前选中的位置
    private int selectPosition;
    private List<OrderCashierListBean> clist;
    private Handler handler;

    public Print_Order_Item_ListAdapter(List<OrderCashierListBean> clist, Context context, Handler handler) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.print_item_order_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.order_number.setText(clist.get(position).getOrdersn());
        holder.orderTime.setText(JavaUtilsNormal.getTime(Long.parseLong(clist.get(position).getCreated_at())));
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
        holder.lookover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 1;
                message.obj = clist.get(position);
                handler.sendMessage(message);
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
        public final View root;
        public final TextView order_money;
        public final TextView lookover;

        public ViewHolder(View root) {
            super(root);
            order_number = root.findViewById(R.id.order_number);
            number = root.findViewById(R.id.number);
            orderTime = root.findViewById(R.id.create_ordertime);
            order_money = root.findViewById(R.id.order_money);
            lookover = root.findViewById(R.id.lookover);
            this.root = root;
        }
    }
}
