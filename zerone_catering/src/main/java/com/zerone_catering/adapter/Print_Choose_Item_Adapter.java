package com.zerone_catering.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zerone_catering.R;
import com.zerone_catering.domain.PrinterMachine;

import java.util.List;


/**
 * Created by dalong on 2017/1/13.
 */

public class Print_Choose_Item_Adapter extends RecyclerView.Adapter<Print_Choose_Item_Adapter.ViewHolder> {

    public Context mContext;
    OnItemClickListener mOnItemClickListener;
    //当前选中的位置
    private int selectPosition;
    private List<PrinterMachine> list;

    public Print_Choose_Item_Adapter(List<PrinterMachine> list, Context context) {
        this.list = list;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.printer_machine,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.machineName.setText(list.get(position).getMaName());
        holder.checkBox.setChecked(list.get(position).isChblen());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
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
        public final TextView machineName;
        public final CheckBox checkBox;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            machineName = root.findViewById(R.id.machineName);
            checkBox = root.findViewById(R.id.choice_lean);
            this.root = root;
        }
    }
}
