package com.aiqing.newssdk;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.aiqing.newssdk.news.Category;
import com.aiqing.newssdk.news.adapter.TagViewAdapter;
import com.aiqing.newssdk.view.RotateImageView;
import com.huxq17.handygridview.HandyGridView;

public class TagManager implements View.OnClickListener ,BackManager.OnBackListener {
    private View tagLayout;
    private RotateImageView ivCollapse;
    private HandyGridView mTagGridView;
    private TagViewAdapter adapter;
    private Activity mActivity;

    public TagManager(Activity activity, TagViewAdapter adapter) {
        mActivity = activity;
        this.adapter = adapter;
    }

    public void expandTagLayout() {
        BackManager.INSTANCE.addBackListener(this);
        ViewGroup parent = mActivity.findViewById(android.R.id.content);
        if (tagLayout == null) {
            tagLayout = LayoutInflater.from(mActivity).inflate(R.layout.tag_layout, parent, false);
            ivCollapse = tagLayout.findViewById(R.id.iv_collapse_taglayout);
            ivCollapse.setOnClickListener(this);
            parent.addView(tagLayout);
        } else {
            parent.removeView(tagLayout);
            parent.addView(tagLayout);
        }
        ivCollapse.setRotate(45);
        setupTagGridView();
    }

    AdapterView.OnItemClickListener mListener;

    public void setTagClickListener(AdapterView.OnItemClickListener listener) {
        mListener = listener;
    }

    private void setupTagGridView() {
        if (mTagGridView == null) {
            mTagGridView = tagLayout.findViewById(R.id.hgv_tag);
            mTagGridView.setAdapter(adapter);
            mTagGridView.setSelectorEnabled(true);
            mTagGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mListener.onItemClick(parent, view, position, id);
                }
            });
        } else {
            adapter.setData(Category.values());
        }
    }

    public void collapseTagLayout() {
        BackManager.INSTANCE.removeBackListener(this);
        ViewGroup parent = mActivity.findViewById(android.R.id.content);
        parent.removeView(tagLayout);
    }

    @Override
    public void onClick(View v) {
        if (v == ivCollapse) {
            collapseTagLayout();
        }
    }

    @Override
    public boolean onBack() {
        if(tagLayout != null && tagLayout.getParent() != null){
            collapseTagLayout();
            return true;
        }
        return false;
    }
}
