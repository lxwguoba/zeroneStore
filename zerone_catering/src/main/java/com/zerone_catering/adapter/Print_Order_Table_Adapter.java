package com.zerone_catering.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.domain.tablefinal.cashiertable.TableListInfoCashierFinal;

import java.util.List;


/**
 * Created by dalong on 2017/1/13.
 */

public class Print_Order_Table_Adapter extends RecyclerView.Adapter<Print_Order_Table_Adapter.ViewHolder> {

    public Context mContext;
    OnItemClickListener mOnItemClickListener;
    //当前选中的位置
    private int selectPosition;
    private List<TableListInfoCashierFinal> list;

    public Print_Order_Table_Adapter(List<TableListInfoCashierFinal> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    /**
     * 数据发生改变的方法
     *
     * @param clist 发生改变的集合类
     */
    public void changeData(List<TableListInfoCashierFinal> clist) {
        this.list = clist;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.print_order_table_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.table_name.setText(list.get(position).getTable_name());
        holder.onopaynum.setText(list.get(position).getOrder_unpaid() + "单");
        holder.onum.setText(list.get(position).getOrder_num() + "单");

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            }
        });

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
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
        public final TextView table_name;
        public final View root;
        private final TextView onum;
        private final TextView onopaynum;

        public ViewHolder(View root) {
            super(root);
            table_name = root.findViewById(R.id.table_name);
            onum = root.findViewById(R.id.orderNumber);
            onopaynum = root.findViewById(R.id.ordernopay);
            this.root = root;
        }
    }
}
