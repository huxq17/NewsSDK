package com.aiqing.newssdk.news;

import android.text.TextUtils;

import com.aiqing.newssdk.CustomApplication;
import com.aiqing.newssdk.api.ApiManager;
import com.aiqing.newssdk.base.BasePresenter;
import com.aiqing.newssdk.base.ImpBaseView;
import com.aiqing.newssdk.config.Constants;
import com.aiqing.newssdk.disklrucache.DiskCacheManager;
import com.aiqing.newssdk.news.details.NewsDetailsActivity;
import com.aiqing.newssdk.utils.LogWritter;
import com.aiyou.toolkit.common.LogUtils;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by PandaQ on 2016/9/22.
 * email : 767807368@qq.com
 */

class HeadLinePresenter extends BasePresenter implements NewsContract.Presenter {

    private NewsContract.View mView;
    private long timeStamp;

    @Override
    public void refreshNews() {
        mView.showRefreshBar();
        timeStamp = 0;
        final Category category = mView.getCategory();
        if (category != null) {
            ApiManager.getInstence().getSDKNewsList().getNewsList(category.toString())
                    .map(new Function<NewsList, List<NewsList.DataBean>>() {

                        @Override
                        public List<NewsList.DataBean> apply(NewsList topNewsList) throws Exception {
                            int rc = topNewsList.getRc();
                            String msg = topNewsList.getMsg();
                            int count = topNewsList.getCount();
                            List<NewsList.DataBean> datas = topNewsList.getData();
                            timeStamp = datas.get(datas.size() - 1).getPublish_time();
                            return datas;
                        }
                    })
                    .concatMap(new Function<List<NewsList.DataBean>, ObservableSource<NewsList.DataBean>>() {
                        @Override
                        public ObservableSource<NewsList.DataBean> apply(List<NewsList.DataBean> dataBeans) throws Exception {
                            return Observable.fromIterable(dataBeans);
                        }
                    })
                    .map(new Function<NewsList.DataBean, BaseItem>() {
                        @Override
                        public BaseItem apply(NewsList.DataBean newsBean) throws Exception {
                            BaseItem<NewsList.DataBean> baseItem = new BaseItem<>();
                            boolean hasImage = newsBean.isHas_image();
                            if (!hasImage) {
                                baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_NORMAL);
                            } else {
                                NewsList.DataBean.MiddleImageBean middleImageBean = newsBean.getMiddle_image();
                                List<NewsBean> list = middleImageBean.getUrl_list();
                                if (list.size() == 1) {
                                    baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_ONE);
                                } else if (list.size() == 3) {
                                    baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_THREE);
                                } else {
                                    baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_NORMAL);
                                }
                            }
                            baseItem.setData(newsBean);
                            return baseItem;
                        }
                    })
                    .toList()
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
                            manager.put(mView.getCacheKey(), value);
//                            timeStamp += 20;
                            mView.hideRefreshBar();
                            mView.refreshNewsSuccessed(value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogWritter.LogStr(e.getMessage() + "------------->errMsg");
                            mView.hideRefreshBar();
                            mView.refreshNewsFail(e.getMessage());
                        }
                    });
        }
    }

    private boolean requestViewDetails = false;

    public void viewNewsDetails(final NewsList.DataBean dataBean) {
        if (!requestViewDetails) {
            requestViewDetails = true;
            Observable.create(new ObservableOnSubscribe<NewsList.DataBean>() {
                @Override
                public void subscribe(ObservableEmitter<NewsList.DataBean> e) throws Exception {
                    e.onNext(dataBean);
                }
            })
                    .map(new Function<NewsList.DataBean, String>() {
                        @Override
                        public String apply(NewsList.DataBean dataBean) throws Exception {
                            return dataBean.getUrl();
                        }
                    })
                    .flatMap(new Function<String, ObservableSource<NewsHtmlBean>>() {
                        @Override
                        public ObservableSource<NewsHtmlBean> apply(String s) throws Exception {
                            return ApiManager.getInstence().getSDKNewsList().getHtml(s);
                        }
                    })
                    .map(new Function<NewsHtmlBean, String>() {
                        @Override
                        public String apply(NewsHtmlBean newsHtmlBean) throws Exception {
                            List<NewsHtmlBean.DataBean> datas = newsHtmlBean.getData();
                            if (datas != null && datas.size() > 0) {
                                NewsHtmlBean.DataBean dataBean = datas.get(0);
                                if (dataBean != null) {
                                    String html = dataBean.getContent();
                                    String url = dataBean.getUrl();
                                    if (!TextUtils.isEmpty(html)) {
                                        return html;
                                    } else {
                                        return url;
                                    }
                                }
                            }
                            return "";
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String content) throws Exception {
                            if (TextUtils.isEmpty(content)) return;
                            NewsDetailsActivity.start(mView.getContext(), content);
                            requestViewDetails = false;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            requestViewDetails = false;
                            LogUtils.e("throwable msg="+throwable.getMessage());
                        }
                    });
        }

    }

    //两个方法没区别,只是刷新会重新赋值
    @Override
    public void loadMore() {
        final Category category = mView.getCategory();
        if (category != null) {
            ApiManager.getInstence().getSDKNewsList().getMore(category.toString(), timeStamp)
                    .map(new Function<NewsList, List<NewsList.DataBean>>() {

                        @Override
                        public List<NewsList.DataBean> apply(NewsList topNewsList) throws Exception {
                            List<NewsList.DataBean> datas = topNewsList.getData();
                            timeStamp = datas.get(datas.size() - 1).getPublish_time();
                            return datas;
                        }
                    })
                    .concatMap(new Function<List<NewsList.DataBean>, ObservableSource<NewsList.DataBean>>() {
                        @Override
                        public ObservableSource<NewsList.DataBean> apply(List<NewsList.DataBean> dataBeans) throws Exception {
                            return Observable.fromIterable(dataBeans);
                        }
                    })
                    .skipWhile(new Predicate<NewsList.DataBean>() {
                        @Override
                        public boolean test(NewsList.DataBean dataBean) throws Exception {
                            return false;
                        }
                    })
                    .map(new Function<NewsList.DataBean, BaseItem>() {
                        @Override
                        public BaseItem apply(NewsList.DataBean newsBean) throws Exception {
                            BaseItem<NewsList.DataBean> baseItem = new BaseItem<>();
                            boolean hasImage = newsBean.isHas_image();
                            if (!hasImage) {
                                baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_NORMAL);
                            } else {
                                NewsList.DataBean.MiddleImageBean middleImageBean = newsBean.getMiddle_image();
                                List<NewsBean> list = middleImageBean.getUrl_list();
                                if (list.size() == 1) {
                                    baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_ONE);
                                } else if (list.size() == 3) {
                                    baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_THREE);
                                } else {
                                    baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_NORMAL);
                                }
                            }
                            baseItem.setData(newsBean);
                            return baseItem;
                        }
                    })
                    .toList()
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
//                            DiskCacheManager manager = new DiskCacheManager(CustomApplication.getContext(), Constants.CACHE_NEWS_FILE);
//                            manager.put(mView.getCacheKey(), value);
                            if (value != null && value.size() > 0) {
                                mView.loadMoreSuccess(value);
                            } else {
                                mView.loadAll();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogWritter.LogStr(e.getMessage() + "------------->errMsg");
                            mView.loadMoreFail(e.getMessage());
                        }
                    });

        }
    }

    /**
     * 读取缓存
     */
    @Override
    public void loadCache() {
        DiskCacheManager manager = new DiskCacheManager(CustomApplication.getContext(), Constants.CACHE_NEWS_FILE);
        ArrayList<BaseItem> topNews = manager.getSerializable(mView.getCacheKey());
        if (topNews != null) {
            NewsList.DataBean dataBean = (NewsList.DataBean) topNews.get(topNews.size() - 1).getData();
            timeStamp = dataBean.getPublish_time();
            mView.refreshNewsSuccessed(topNews);
        } else {
            refreshNews();
        }
    }

    @Override
    public void bindView(ImpBaseView view) {
        mView = (NewsContract.View) view;
    }

    @Override
    public void unbindView() {
        dispose();
    }

    @Override
    public void onDestory() {
        mView = null;
    }
}
