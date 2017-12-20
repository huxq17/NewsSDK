package com.aiqing.newssdk;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.aiqing.newssdk.base.BaseActivity;
import com.aiqing.newssdk.news.HeadLineFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    // Used to load the 'native-lib' library on application startup.
    private ArrayList<Fragment> mFragmentArrayList;
    ViewPager mVpNewsList;
    TabLayout mTlNewsTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVpNewsList = f(R.id.vp_news_list);
        mTlNewsTabs = f(R.id.tl_news_tabs);
        initView();
    }

    String[] newsTitle = new String[]{
            "要闻",
            "科技",
            "体育",
            "健康",
            "轻松一刻",
            "军事",
            "旅游"
    };

    private void initView() {
        mFragmentArrayList = new ArrayList<>();
        mFragmentArrayList.add(new HeadLineFragment());
        mFragmentArrayList.add(new HeadLineFragment());
        mFragmentArrayList.add(new HeadLineFragment());
        mFragmentArrayList.add(new HeadLineFragment());
        mFragmentArrayList.add(new HeadLineFragment());
        mFragmentArrayList.add(new HeadLineFragment());
        mFragmentArrayList.add(new HeadLineFragment());
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
                return newsTitle[position];
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

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public native String stringFromJNI();

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
