package com.zerone.store.shopingtimetest.Adapter.shopplistadapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zerone.store.shopingtimetest.Bean.ShopBean;
import com.zerone.store.shopingtimetest.Bean.shoplistbean.GoodsCategroyListBean;
import com.zerone.store.shopingtimetest.Contants.IpConfig;
import com.zerone.store.shopingtimetest.R;

import java.util.List;

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.ViewHolder> {
    //从新封装数据
    private List<ShopBean> dataList;
    private Context mContext;
    private List<GoodsCategroyListBean.DataBean.CategorylistBean> goodscatrgoryEntities;
    private Handler handler;

    /**
     * @param context
     * @param items   商品详情的集合
     */
    public GoodsListAdapter(Context context, List<ShopBean> items, Handler handler) {
        this.mContext = context;
        this.dataList = items;
        this.handler = handler;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shoplistitem, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置名
        holder.shop_name.setText(dataList.get(position).getName());
        //设置说明
        holder.shop_shopdiscount.setText(dataList.get(position).getName());
        //设置价格
        holder.shopprice.setText(dataList.get(position).getPrice());
        //商品数量
        //decrease_shop 减少商品
        if ("0".equals(dataList.get(position).getShop_Count())) {
            holder.decrease_shop.setVisibility(View.INVISIBLE);
            holder.shopCount.setVisibility(View.INVISIBLE);
            holder.showTextCount.setVisibility(View.INVISIBLE);
        } else {
            holder.decrease_shop.setVisibility(View.VISIBLE);
            holder.shopCount.setVisibility(View.VISIBLE);
            holder.showTextCount.setVisibility(View.VISIBLE);
            holder.shopCount.setText(dataList.get(position).getShop_Count());
        }
        //设置商品图片
        String s = IpConfig.URL_GETPICTURE + dataList.get(position).getThumb().get(0).getThumb();
        Log.i("URL", "picture::=" + s);
        Glide.with(mContext).load(IpConfig.URL_GETPICTURE + dataList.get(position).getThumb().get(0).getThumb()).centerCrop().placeholder(R.mipmap.app_logo).crossFade().into(holder.shop_picture);
        //通过判别对应位置的数量是否大于0来显示隐藏数量
        //加号按钮点击
        holder.add_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 1000;
                message.obj = position;
                handler.sendMessage(message);
            }
        });
        //减号点击按钮点击
        holder.decrease_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 1110;
                message.obj = position;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView shop_picture;
        public final TextView shop_name;
        public final TextView shop_shopdiscount;
        public final TextView shopprice;
        public final TextView decrease_shop;
        public final TextView shopCount;
        public final TextView add_shop;
        public final LinearLayout showTextCount;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            shop_picture = root.findViewById(R.id.picture);
            shop_name = root.findViewById(R.id.shopname);
            shop_shopdiscount = root.findViewById(R.id.shopdiscount);
            shopprice = root.findViewById(R.id.shopprice);
            decrease_shop = root.findViewById(R.id.decrease_shop);
            shopCount = root.findViewById(R.id.shopCount);
            add_shop = root.findViewById(R.id.add_shop);
            showTextCount = root.findViewById(R.id.showTextCount);
            this.root = root;
        }
    }
}
