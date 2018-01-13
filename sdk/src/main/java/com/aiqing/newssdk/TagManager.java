package com.aiqing.newssdk;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;

import com.aiqing.newssdk.news.Category;
import com.aiqing.newssdk.news.adapter.TagViewAdapter;
import com.aiqing.newssdk.view.RotateImageView;
import com.aiyou.toolkit.common.DensityUtil;
import com.aiyou.toolkit.common.Utils;
import com.aiyou.toolkit.tractor.listener.LoadListener;
import com.huxq17.handygridview.HandyGridView;

public class TagManager implements View.OnClickListener, BackManager.OnBackListener {
    private View tagLayout;
    private RotateImageView ivCollapse;
    private HandyGridView mTagGridView;
    private TagViewAdapter adapter;
    private Activity mActivity;
    private int mScreenHeight;
    private int mDuration = 300;
    private int mTablayoutHeight;
    private int mSelectedPosition;
    private ViewGroup mParent;
    private boolean isAnimation;

    public TagManager(Activity activity, TagViewAdapter adapter, ViewGroup parent) {
        mActivity = activity;
        this.adapter = adapter;
        mScreenHeight = Utils.getScreenHeight(activity);
        mParent = parent;
        mTablayoutHeight = activity.getResources().getDimensionPixelSize(R.dimen.tablayout_height) + DensityUtil.dip2px(activity, 20);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    private int mToolbarHeight;

    public void expandTagLayout(int toolbarHeight) {
        if (isAnimation) return;
        if (mToolbarHeight == 0) {
            mToolbarHeight = toolbarHeight;
            mTablayoutHeight += mToolbarHeight;
        }
        BackManager.INSTANCE.addBackListener(this);
        if (tagLayout == null) {
            tagLayout = LayoutInflater.from(mActivity).inflate(R.layout.tag_layout, mParent, false);
            ivCollapse = tagLayout.findViewById(R.id.iv_collapse_taglayout);
            ivCollapse.setOnClickListener(this);
            mParent.addView(tagLayout);
        } else {
            mParent.removeView(tagLayout);
            mParent.addView(tagLayout);
        }
        ivCollapse.startRotate(45, mDuration);
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
//            mTagGridView.setMode(HandyGridView.MODE.LONG_PRESS);
            mTagGridView.setSelectorEnabled(false);
            mTagGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mSelectedPosition = position;
                    mListener.onItemClick(null, null, mSelectedPosition, 0);
                }
            });
        } else {
            adapter.setData(Category.values(), mSelectedPosition);
        }
        TranslateAnimation outAnimal = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        outAnimal.setDuration(mDuration);
        isAnimation = true;
        outAnimal.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTagGridView.startAnimation(outAnimal);
    }

    public void collapseTagLayout(final LoadListener listener) {
        if (isAnimation) return;
        BackManager.INSTANCE.removeBackListener(this);
        TranslateAnimation outAnimal = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        outAnimal.setDuration(mDuration);
        mTagGridView.startAnimation(outAnimal);
        ivCollapse.startRotate(0, mDuration);
        isAnimation = true;
        outAnimal.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mParent.removeView(tagLayout);
                isAnimation = false;
                if (listener != null)
                    listener.onSuccess(mSelectedPosition);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void collapseTagLayout() {
        collapseTagLayout(null);
    }

    @Override
    public void onClick(View v) {
        if (v == ivCollapse) {
            collapseTagLayout();
        }
    }

    @Override
    public boolean onBack() {
        if (tagLayout != null && tagLayout.getParent() != null) {
            collapseTagLayout();
            return true;
        }
        return false;
    }
}
