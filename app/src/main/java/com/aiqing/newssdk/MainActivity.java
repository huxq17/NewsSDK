package com.aiqing.newssdk;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;

import com.aiqing.newssdk.base.BaseActivity;
import com.aiqing.newssdk.news.Category;
import com.aiqing.newssdk.news.HeadLineFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ArrayList<Fragment> mFragmentArrayList;
    ViewPager mVpNewsList;
    TabLayout mTlNewsTabs;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVpNewsList = f(R.id.vp_news_list);
        mTlNewsTabs = f(R.id.tl_news_tabs);
        initView();
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
}
