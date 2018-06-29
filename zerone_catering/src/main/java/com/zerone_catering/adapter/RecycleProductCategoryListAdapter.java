package com.zerone_catering.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.domain.tablefinal.TableCatBeanFinal;

import java.util.List;


/**
 * Created by dalong on 2017/1/13.
 */

public class RecycleProductCategoryListAdapter extends RecyclerView.Adapter<RecycleProductCategoryListAdapter.ViewHolder> {

    public Context mContext;
    OnItemClickListener mOnItemClickListener;
    //当前选中的位置
    private int selectPosition;
    private List<TableCatBeanFinal> clist;

    public RecycleProductCategoryListAdapter(List<TableCatBeanFinal> clist, Context context) {
        this.clist = clist;
        this.mContext = context;
    }

    /**
     * 数据发生改变的方法
     *
     * @param clist 发生改变的集合类
     */
    public void changeData(List<TableCatBeanFinal> clist) {
        this.clist = clist;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_category_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.goodsCategoryName.setText(clist.get(position).getRoom_name());
        if (selectPosition != -1) {
            if (selectPosition == position) {
                //被选中
                holder.checkcategory.setBackgroundColor(Color.parseColor("#fedb43"));
                holder.goodsCategoryName.setTextColor(Color.parseColor("#000000"));
            } else {
                // 没有被选中808080
                holder.goodsCategoryName.setTextColor(Color.parseColor("#808080"));
                holder.checkcategory.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        } else {
            holder.goodsCategoryName.setTextColor(Color.parseColor("#808080"));
            holder.checkcategory.setBackgroundColor(Color.parseColor("#ffffff"));
        }
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
        public final TextView goodsCategoryName;
        public final View root;
        public final ImageView checkcategory;

        public ViewHolder(View root) {
            super(root);
            goodsCategoryName = root.findViewById(R.id.catName);
            checkcategory = root.findViewById(R.id.checkcategory);
            this.root = root;
        }
    }
}
