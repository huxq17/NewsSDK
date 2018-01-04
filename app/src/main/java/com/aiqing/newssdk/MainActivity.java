package com.aiqing.newssdk;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;

import com.aiqing.newssdk.base.BaseActivity;
import com.aiqing.newssdk.news.Category;
import com.aiqing.newssdk.news.HeadLineFragment;
import com.aiqing.newssdk.news.adapter.TagViewAdapter;
import com.aiqing.newssdk.view.RotateImageView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ArrayList<Fragment> mFragmentArrayList;
    ViewPager mVpNewsList;
    TabLayout mTlNewsTabs;
    private RotateImageView ivExpand;
    private TagManager tagManager;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVpNewsList = f(R.id.vp_news_list);
        mTlNewsTabs = f(R.id.tl_news_tabs);
        ivExpand = f(R.id.iv_expand_taglayout);
        initView();
        Observable.interval(1, TimeUnit.SECONDS).take(100)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
//                        LogUtils.e("accept " + aLong);
                    }
                });
    }

    private void initView() {
        mFragmentArrayList = new ArrayList<>();
        for (Category category : Category.values()) {
            mFragmentArrayList.add(HeadLineFragment.create(category));
        }
        mVpNewsList.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentArrayList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentArrayList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return Category.get(position).getTitle();
            }
        });
        mVpNewsList.addOnPageChangeListener(this);
        mTlNewsTabs.setupWithViewPager(mVpNewsList);
        mTlNewsTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        ivExpand.setOnClickListener(this);
        tagManager = new TagManager(this, new TagViewAdapter(this, Category.values()));
        tagManager.setTagClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mVpNewsList.setCurrentItem(position);
                tagManager.collapseTagLayout();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v == ivExpand) {
            tagManager.expandTagLayout();
        }
    }


    @Override
    public void onBackPressed() {
        if (!BackManager.INSTANCE.back()) {
            super.onBackPressed();
        }
    }
}
