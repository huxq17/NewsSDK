package com.aiqing.newssdk.news;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiqing.newssdk.R;
import com.aiqing.newssdk.base.BaseFragment;
import com.aiqing.newssdk.decoration.RecyclerViewDivider;
import com.aiqing.newssdk.rxbus.RxBus;
import com.aiqing.newssdk.rxbus.RxConstants;
import com.aiyou.toolkit.common.DensityUtil;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;
import com.pandaq.pandaqlib.magicrecyclerView.MagicRecyclerView;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by PandaQ on 2017/3/28.
 * 网易头条 fragment
 */

public class HeadLineFragment extends BaseFragment implements NewsContract.View, SwipeRefreshLayout.OnRefreshListener, BaseRecyclerAdapter.OnItemClickListener {

    MagicRecyclerView mNewsRecycler;
    SwipeRefreshLayout mRefresh;
    TextView mEmptyMsg;
    private NewsContract.Presenter mPresenter;
    private NewsListAdapter mAdapter;
    private boolean loading = false;
    private Disposable mDisposable;
    private LinearLayoutManager mLinearLayoutManager;
    private Category mCategory;
    private static final String KEY_CATEGORY = "category";

    public static HeadLineFragment create(Category category) {
        HeadLineFragment fragment = new HeadLineFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getCacheKey() {
        return mCategory.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mCategory = (Category) args.getSerializable(KEY_CATEGORY);
        }
        View view = getActivity().getLayoutInflater().inflate(R.layout.headline_newslist_fragment, null, false);
        mRefresh = view.findViewById(R.id.refresh);
        mEmptyMsg = view.findViewById(R.id.empty_msg);
        mNewsRecycler = view.findViewById(R.id.newsRecycler);
        bindPresenter();

        mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        mNewsRecycler.setLayoutManager(mLinearLayoutManager);

        mNewsRecycler.addItemDecoration(new RecyclerViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, DensityUtil.dip2px(getContext(),1), Color.parseColor("#EEEEEE")));
        //屏蔽掉默认的动画，防止刷新时图片闪烁
        mNewsRecycler.getItemAnimator().setChangeDuration(0);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRefresh.setRefreshing(false);
        unbindPresenter();
        onHiddenChanged(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destoryPresenter();
        mAdapter = null;
    }

    private void initView() {
        mNewsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mNewsRecycler.refreshAble()) {
                    mRefresh.setEnabled(true);
                }
                if (mNewsRecycler.loadAble()) {
                    loadMoreNews();
                }
            }
        });
        mRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.white_FFFFFF));
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
//        mRefresh.setRefreshing(true);
//        refreshNews();
        mPresenter.loadCache();
        View footer = mNewsRecycler.getFooterView();
        if (footer != null) {
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreNews();
                }
            });
        }
    }

    @Override
    public void showRefreshBar() {
        mRefresh.setRefreshing(true);
    }

    @Override
    public Category getCategory() {
        return mCategory;
    }

    @Override
    public void hideRefreshBar() {
        mRefresh.setRefreshing(false);
    }

    @Override
    public void refreshNews() {
        mPresenter.refreshNews();
    }

    @Override
    public void refreshNewsFail(String errorMsg) {
        if (mAdapter == null) {
            mEmptyMsg.setVisibility(View.VISIBLE);
            mNewsRecycler.setVisibility(View.INVISIBLE);
            mRefresh.requestFocus();
        }
    }

    @Override
    public void refreshNewsSuccessed(ArrayList<BaseItem> topNews) {
        if (topNews == null || topNews.size() <= 0) {
            mEmptyMsg.setVisibility(View.VISIBLE);
            mNewsRecycler.setVisibility(View.INVISIBLE);
            mRefresh.requestFocus();
        } else {
            mEmptyMsg.setVisibility(View.GONE);
            mNewsRecycler.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new NewsListAdapter(this);
            mAdapter.setBaseDatas(topNews);
            mNewsRecycler.setAdapter(mAdapter);
            //实质是是对 adapter 设置点击事件所以需要在设置 adapter 之后调用
            mNewsRecycler.addOnItemClickListener(this);
        } else {
            mAdapter.setBaseDatas(topNews);
        }
        mNewsRecycler.showFooter();
    }

    @Override
    public void loadMoreNews() {
        if (!loading) {
            mPresenter.loadMore();
            loading = true;
        }
    }

    @Override
    public void loadMoreFail(String errorMsg) {
        loading = false;
    }

    @Override
    public void loadMoreSuccess(ArrayList<BaseItem> topNewses) {
        loading = false;
        mAdapter.addBaseDatas(topNewses);
    }

    @Override
    public void loadAll() {
        mNewsRecycler.hideFooter();
    }

    @Override
    public void onRefresh() {
        refreshNews();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden && mRefresh.isRefreshing()) { // 隐藏的时候停止 SwipeRefreshLayout 转动
            mRefresh.setRefreshing(false);
        }
        if (!hidden) {
            subscribeEvent();
        } else {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
    }

    @Override
    public void onItemClick(int position, BaseItem data, View view) {
        mPresenter.viewNewsDetails((NewsList.DataBean) data.getData());

        //跳转到其他界面
//        NewsBean topNews = (NewsBean) data.getData();
//        Bundle bundle = new Bundle();
//        Intent intent = new Intent(HeadLineFragment.this.getActivity(), NewsDetailActivity.class);
//        bundle.putString(Constants.BUNDLE_KEY_TITLE, topNews.getTitle());
//        bundle.putString(Constants.BUNDLE_KEY_ID, topNews.getDocid());
//        bundle.putString(Constants.BUNDLE_KEY_IMG_URL, topNews.getImgsrc());
//        bundle.putString(Constants.BUNDLE_KEY_HTML_URL, topNews.getUrl());
//        intent.putExtras(bundle);
//        String transitionName = getString(R.string.top_news_img);
//        Pair pairImg = new Pair<>(view.findViewById(R.id.news_image), transitionName);
//        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairImg);
//        startActivity(intent, transitionActivityOptions.toBundle());
    }

    @Override
    public void bindPresenter() {
        if (mPresenter == null) {
            mPresenter = new HeadLinePresenter();
        }
        mPresenter.bindView(this);
    }

    @Override
    public void unbindPresenter() {
        mPresenter.unbindView();
    }

    @Override
    public void destoryPresenter() {
        mPresenter.onDestory();
    }

    private void subscribeEvent() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        RxBus.getDefault()
                .toObservableWithCode(RxConstants.BACK_PRESSED_CODE, String.class)
                .subscribeWith(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(String value) {
                        if (value.equals(RxConstants.BACK_PRESSED_DATA) && mNewsRecycler != null) {
                            //滚动到顶部
                            mLinearLayoutManager.smoothScrollToPosition(mNewsRecycler, null, 0);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        // 发生异常时重新订阅事件
                        subscribeEvent();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
