package com.aiqing.newssdk.news;

import com.aiqing.newssdk.CustomApplication;
import com.aiqing.newssdk.api.ApiManager;
import com.aiqing.newssdk.base.BasePresenter;
import com.aiqing.newssdk.base.ImpBaseView;
import com.aiqing.newssdk.config.Constants;
import com.aiqing.newssdk.disklrucache.DiskCacheManager;
import com.aiqing.newssdk.utils.LogWritter;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by PandaQ on 2016/9/22.
 * email : 767807368@qq.com
 */

class HeadLinePresenter extends BasePresenter implements NewsContract.Presenter {

    private NewsContract.View mNewsListFrag;
    private int currentIndex;

    @Override
    public void refreshNews() {
        mNewsListFrag.showRefreshBar();
        currentIndex = 0;
        final Category category = mNewsListFrag.getCategory();
        if(category!=null){
            ApiManager.getInstence().getSDKNewsList().getNewsList(category.toString());
        }
        ApiManager.getInstence().getTopNewsServie()
                .getTopNews(currentIndex + "")
                .map(new Function<TopNewsList, ArrayList<NewsBean>>() {
                    @Override
                    public ArrayList<NewsBean> apply(TopNewsList topNewsList) {
                        return topNewsList.getTopNewsArrayList();
                    }
                })
                .flatMap(new Function<ArrayList<NewsBean>, Observable<NewsBean>>() {
                    @Override
                    public Observable<NewsBean> apply(ArrayList<NewsBean> topNewses) throws Exception {
                        return Observable.fromIterable(topNewses);
                    }
                })
                .filter(new Predicate<NewsBean>() {
                    @Override
                    public boolean test(NewsBean topNews) throws Exception {
                        return topNews.getUrl() != null;
                    }
                })
                .map(new Function<NewsBean, BaseItem>() {
                    @Override
                    public BaseItem apply(NewsBean topNews) {
                        BaseItem<NewsBean> baseItem = new BaseItem<>();
                        baseItem.setData(topNews);
                        return baseItem;
                    }
                })
                .toList()
                //将 List 转为ArrayList 缓存存储 ArrayList Serializable对象
                .map(new Function<List<BaseItem>, ArrayList<BaseItem>>() {
                    @Override
                    public ArrayList<BaseItem> apply(List<BaseItem> baseItems) {
                        ArrayList<BaseItem> items = new ArrayList<>();
                        items.addAll(baseItems);
                        return items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<BaseItem>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<BaseItem> value) {
                        DiskCacheManager manager = new DiskCacheManager(CustomApplication.getContext(), Constants.CACHE_NEWS_FILE);
                        manager.put(Constants.CACHE_HEADLINE_NEWS, value);
                        currentIndex += 20;
                        mNewsListFrag.hideRefreshBar();
                        mNewsListFrag.refreshNewsSuccessed(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogWritter.LogStr(e.getMessage() + "------------->errMsg");
                        mNewsListFrag.hideRefreshBar();
                        mNewsListFrag.refreshNewsFail(e.getMessage());
                    }

                });
    }

    //两个方法没区别,只是刷新会重新赋值
    @Override
    public void loadMore() {
        ApiManager.getInstence().getTopNewsServie()
                .getTopNews(currentIndex + "")
                .map(new Function<TopNewsList, ArrayList<NewsBean>>() {
                    @Override
                    public ArrayList<NewsBean> apply(TopNewsList topNewsList) {
                        return topNewsList.getTopNewsArrayList();
                    }
                })
                .flatMap(new Function<ArrayList<NewsBean>, Observable<NewsBean>>() {
                    @Override
                    public Observable<NewsBean> apply(ArrayList<NewsBean> topNewses) {
                        return Observable.fromIterable(topNewses);
                    }
                })
                .filter(new Predicate<NewsBean>() {
                    @Override
                    public boolean test(NewsBean topNews) {
                        return topNews.getUrl() != null;
                    }
                })
                .map(new Function<NewsBean, BaseItem>() {
                    @Override
                    public BaseItem apply(NewsBean topNews) {
                        BaseItem<NewsBean> baseItem = new BaseItem<>();
                        baseItem.setData(topNews);
                        return baseItem;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BaseItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<BaseItem> value) {
                        if (value != null && value.size() > 0) {
                            //每刷新成功一次多加载20条
                            currentIndex += 20;
                            mNewsListFrag.loadMoreSuccessed((ArrayList<BaseItem>) value);
                        } else {
                            mNewsListFrag.loadAll();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogWritter.LogStr(e.getMessage());
                        mNewsListFrag.loadMoreFail(e.getMessage());
                    }

                });

    }

    /**
     * 读取缓存
     */
    @Override
    public void loadCache() {
        DiskCacheManager manager = new DiskCacheManager(CustomApplication.getContext(), Constants.CACHE_NEWS_FILE);
        ArrayList<BaseItem> topNews = manager.getSerializable(Constants.CACHE_HEADLINE_NEWS);
        if (topNews != null) {
            mNewsListFrag.refreshNewsSuccessed(topNews);
        }
    }

    @Override
    public void bindView(ImpBaseView view) {
        mNewsListFrag = (NewsContract.View) view;
    }

    @Override
    public void unbindView() {
        dispose();
    }

    @Override
    public void onDestory() {
        mNewsListFrag = null;
    }
}
