package com.zerone_catering.adapter.shopplistadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.avtivity.product_details.Activity_Product_Details;
import com.zerone_catering.domain.main.ProductBean;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    //从新封装数据
    private List<ProductBean> plist;
    private Context mContext;
    private Handler handler;
    private OnItemClickListener mOnItemClickListener;//声明接口

    /**
     * @param context
     * @param plist   商品详情的集合
     */
    public ProductListAdapter(Context context, List<ProductBean> plist, Handler handler) {
        this.mContext = context;
        this.plist = plist;
        this.handler = handler;
        setHasStableIds(true);
    }


    /**
     * 判断商品是否有添加到购物车中
     *
     * @param i  条目下标
     * @param vh ViewHolder
     */
    private void isSelected(int i, ViewHolder vh) {
        if (i == 0) {
//            vh.tvGoodsSelectNum.setVisibility(View.GONE);
//            vh.ivGoodsMinus.setVisibility(View.GONE);
        } else {
//            vh.tvGoodsSelectNum.setVisibility(View.VISIBLE);
//            vh.tvGoodsSelectNum.setText(i + "");
//            vh.ivGoodsMinus.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 存放每个分组的第一条的ID
     *
     * @return
     */
    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        int lastFirstPoi = -1;
        for (int i = 0; i < plist.size(); i++) {
            if (plist.get(i).getId() != lastFirstPoi) {
                lastFirstPoi = plist.get(i).getId();
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shoplistitem, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return plist.get(position).hashCode();
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置名
        holder.shop_name.setText(plist.get(position).getName());
        //设置说明textView.setText(Html.fromHtml(str));

//        holder.shop_shopdiscount.setText(Html.fromHtml(plist.get(position).getDetails()));
        //设置价格
        holder.shopprice.setText(plist.get(position).getPrice());
        //商品数量
        //decrease_shop 减少商品
        if ("0".equals(plist.get(position).getProductCount() + "")) {
            holder.decrease_shop.setVisibility(View.INVISIBLE);
            holder.shopCount.setVisibility(View.INVISIBLE);
            holder.showTextCount.setVisibility(View.INVISIBLE);
        } else {
            holder.decrease_shop.setVisibility(View.VISIBLE);
            holder.shopCount.setVisibility(View.VISIBLE);
            holder.showTextCount.setVisibility(View.VISIBLE);
            holder.shopCount.setText(plist.get(position).getProductCount() + "");
        }
        //设置商品图片
        String s = IpConfig.URL_GETPICTURE + plist.get(position).getThumb().get(0).getThumb();
        Log.i("URL", "picture::=" + s);
        Glide.with(mContext).load(IpConfig.URL_GETPICTURE + plist.get(position).getThumb().get(0).getThumb()).centerCrop().placeholder(R.mipmap.app_logo).crossFade().into(holder.shop_picture);
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
        holder.shop_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Activity_Product_Details.class);
                intent.putExtra("ordreinfo", plist.get(position));
                mContext.startActivity(intent);
            }
        });
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.root, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plist.size();
    }

    /**
     * 显示减号的动画
     *
     * @return
     */
    private Animation getShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 2f
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    /**
     * 隐藏减号的动画
     *
     * @return
     */
    private Animation getHiddenAnimation() {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 4f
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView shop_picture;
        public final TextView shop_name;
        //        public final TextView shop_shopdiscount;
        public final TextView shopprice;
        public final LinearLayout decrease_shop;
        public final TextView shopCount;
        public final LinearLayout add_shop;
        public final LinearLayout showTextCount;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            shop_picture = root.findViewById(R.id.picture);
            shop_name = root.findViewById(R.id.shopname);
//            shop_shopdiscount = root.findViewById(R.id.shopdiscount);
            shopprice = root.findViewById(R.id.shopprice);
            decrease_shop = root.findViewById(R.id.decrease_shop);
            shopCount = root.findViewById(R.id.shopCount);
            add_shop = root.findViewById(R.id.add_shop);
            showTextCount = root.findViewById(R.id.showTextCount);
            this.root = root;
        }
    }
}
