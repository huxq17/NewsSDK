package com.aiqing.newssdk;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.aiqing.newssdk.base.BaseActivity;
import com.aiqing.newssdk.news.Category;
import com.aiqing.newssdk.news.HeadLineFragment;
import com.aiqing.newssdk.news.adapter.TagViewAdapter;
import com.aiqing.newssdk.view.RotateImageView;
import com.aiyou.toolkit.tractor.listener.impl.LoadListenerImpl;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ArrayList<Fragment> mFragmentArrayList = new ArrayList<>();
    ViewPager mVpNewsList;
    TabLayout mTlNewsTabs;
    private RotateImageView ivExpand;
    private TagManager tagManager;
    private Toolbar toolbar;
    private ViewGroup parent;

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
        parent = f(R.id.fl_content_parent);
        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("头条");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().hide();
        initView();
        Observable.interval(1, TimeUnit.SECONDS).take(100)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
//                        LogUtils.e("accept " + aLong);
                    }
                });
    }

    <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>)schedulersTransformer;
    }

    final ObservableTransformer schedulersTransformer =new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private void initView() {
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
        tagManager = new TagManager(this, new TagViewAdapter(this, Category.values()), parent);
        tagManager.setTagClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tagManager.collapseTagLayout(new LoadListenerImpl() {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        int position = (int) result;
                        mVpNewsList.setCurrentItem(position);
                    }
                });
            }
        });
//        try {
//            Field field = mVpNewsList.getClass().getDeclaredField("MAX_SETTLE_DURATION");
//            field.setAccessible(true);
//            int duration = field.getInt(mVpNewsList);
//            field.set(mVpNewsList, 3000);
//            LogUtils.e("old value=" + duration + ";now value =" + field.getInt(mVpNewsList));
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
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
        tagManager.setSelectedPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v == ivExpand) {
            tagManager.expandTagLayout(toolbar.getHeight());
        }
    }


    @Override
    public void onBackPressed() {
        if (!BackManager.INSTANCE.back()) {
            super.onBackPressed();
        }
    }
}
