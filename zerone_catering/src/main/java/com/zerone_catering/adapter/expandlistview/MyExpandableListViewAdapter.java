package com.zerone_catering.adapter.expandlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zerone_catering.R;
import com.zerone_catering.domain.twolist.CashierDetailsListBean;

import java.util.List;

/**
 * Created by on 2018/6/29 0029 15 50.
 * Author  LiuXingWen
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext = null;
    private List<CashierDetailsListBean> mGroupList = null;
    private List<List<String>> mItemList = null;

    public MyExpandableListViewAdapter(Context context, List<CashierDetailsListBean> groupList) {
        this.mContext = context;
        this.mGroupList = groupList;
    }

    /**
     * 获取组的个数
     *
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroupList.get(groupPosition).getGoodsdata().size();
    }

    /**
     * 获取指定组中的数据
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public CashierDetailsListBean getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    /**
     * 获取指定组中的指定子元素数据。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public CashierDetailsListBean.GoodsdataBean getChild(int groupPosition, int childPosition) {
        return mGroupList.get(groupPosition).getGoodsdata().get(childPosition);
    }

    /**
     * 获取指定组的ID，这个组ID必须是唯一的
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取指定组中的指定子元素ID
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.acticity_expandable_parent, null);
            groupHolder = new GroupHolder();
            groupHolder.groupNameTv = convertView.findViewById(R.id.orderNumber);

            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.groupNameTv.setText(mGroupList.get(groupPosition).getOrdersn());
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.acticity_expandable_sun, null);
            itemHolder = new ItemHolder();
            itemHolder.title = convertView.findViewById(R.id.title);
            itemHolder.total = convertView.findViewById(R.id.total);
            itemHolder.price = convertView.findViewById(R.id.price);
            itemHolder.iconImg = convertView.findViewById(R.id.iconImg);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.title.setText(mGroupList.get(groupPosition).getGoodsdata().get(childPosition).getTitle());
        itemHolder.total.setText("X " + mGroupList.get(groupPosition).getGoodsdata().get(childPosition).getTotal());
        itemHolder.price.setText("￥" + mGroupList.get(groupPosition).getGoodsdata().get(childPosition).getPrice());
        Glide.with(mContext).load(mGroupList.get(groupPosition).getGoodsdata().get(childPosition).getThumb()).into(itemHolder.iconImg);
        return convertView;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupHolder {
        public TextView groupNameTv;
    }

    class ItemHolder {
        public ImageView iconImg;
        public TextView title;
        public TextView total;
        public TextView price;

    }
}
