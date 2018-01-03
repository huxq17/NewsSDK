package com.aiqing.newssdk.news.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aiqing.newssdk.news.Category;
import com.aiqing.newssdk.view.TagView;
import com.aiyou.toolkit.common.DensityUtil;
import com.huxq17.handygridview.scrollrunner.OnItemMovedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagViewAdapter extends BaseAdapter implements OnItemMovedListener, TagView.OnTagDeleteListener {
    private Context context;
    private List<Category> mDatas = new ArrayList<>();

    public TagViewAdapter(Context context, Category[] dataList) {
        this.context = context;
        Collections.addAll(mDatas, dataList);
    }

    private boolean inEditMode = false;

    public void setData(Category[] dataList) {
        this.mDatas.clear();
        Collections.addAll(mDatas, dataList);
        notifyDataSetChanged();
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Category getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TagView textView;
        if (convertView == null) {
            textView = new TagView(context);
            convertView = textView;
            textView.setMaxLines(1);
            textView.setHeight(DensityUtil.dip2px(context, 40));
            int id = context.getResources().getIdentifier("s_grid_item", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            textView.setBackgroundDrawable(drawable);
            textView.setGravity(Gravity.CENTER);
        } else {
            textView = (TagView) convertView;
        }
        if (!isFixed(position)) {
            textView.showDeleteIcon(inEditMode);
        } else {
            textView.showDeleteIcon(false);
        }
        textView.setText(getItem(position).getTitle());
        textView.setOnTagDeleteListener(position, this);
        return convertView;
    }

    @Override
    public void onItemMoved(int from, int to) {
        Category s = mDatas.remove(from);
        mDatas.add(to, s);
    }

    @Override
    public boolean isFixed(int position) {
        //When postion==0,the item can not be dragged.
//        if (position == 0) {
//            return true;
//        }
        return false;
    }

    @Override
    public void onDelete(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }
}