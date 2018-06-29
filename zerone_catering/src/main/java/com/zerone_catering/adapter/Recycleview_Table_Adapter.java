package com.zerone_catering.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.domain.tablefinal.TableInfoBeanFianl;

import java.util.List;


/**
 * Created by dalong on 2017/1/13.
 */

public class Recycleview_Table_Adapter extends RecyclerView.Adapter<Recycleview_Table_Adapter.ViewHolder> {

    public Context mContext;
    OnItemClickListener mOnItemClickListener;
    //当前选中的位置
    private int selectPosition;
    private List<TableInfoBeanFianl> list;

    public Recycleview_Table_Adapter(List<TableInfoBeanFianl> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    /**
     * 数据发生改变的方法
     *
     * @param clist 发生改变的集合类
     */
    public void changeData(List<TableInfoBeanFianl> clist) {
        this.list = clist;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_det_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.table_name.setText(list.get(position).getTable_name());

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
        public final RelativeLayout relativeLayout;

        public ViewHolder(View root) {
            super(root);
            table_name = root.findViewById(R.id.table_name);
            relativeLayout = root.findViewById(R.id.check_item_talbe);
            this.root = root;
        }
    }
}
