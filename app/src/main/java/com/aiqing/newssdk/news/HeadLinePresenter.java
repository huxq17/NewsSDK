package com.aiqing.newssdk.news;

import com.aiqing.newssdk.CustomApplication;
import com.aiqing.newssdk.api.ApiManager;
import com.aiqing.newssdk.base.BasePresenter;
import com.aiqing.newssdk.base.ImpBaseView;
import com.aiqing.newssdk.config.Constants;
import com.aiqing.newssdk.disklrucache.DiskCacheManager;
import com.aiqing.newssdk.utils.LogWritter;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by PandaQ on 2016/9/22.
 * email : 767807368@qq.com
 */

class HeadLinePresenter extends BasePresenter implements NewsContract.Presenter {

    private NewsContract.View mNewsListFrag;
    private long timeStamp;

    @Override
    public void refreshNews() {
        mNewsListFrag.showRefreshBar();
        timeStamp = 0;
        final Category category = mNewsListFrag.getCategory();
        if (category != null) {
            ApiManager.getInstence().getSDKNewsList().getNewsList(category.toString())
                    .map(new Function<SDKNewsList, List<SDKNewsList.DataBean>>() {

                        @Override
                        public List<SDKNewsList.DataBean> apply(SDKNewsList topNewsList) throws Exception {
                            int rc = topNewsList.getRc();
                            String msg = topNewsList.getMsg();
                            int count = topNewsList.getCount();
                            List<SDKNewsList.DataBean> datas = topNewsList.getData();
                            timeStamp = datas.get(datas.size() - 1).getPublish_time();
                            return datas;
                        }
                    })
                    .flatMap(new Function<List<SDKNewsList.DataBean>, ObservableSource<SDKNewsList.DataBean>>() {
                        @Override
                        public ObservableSource<SDKNewsList.DataBean> apply(List<SDKNewsList.DataBean> dataBeans) throws Exception {
                            return Observable.fromIterable(dataBeans);
                        }
                    })
                    .map(new Function<SDKNewsList.DataBean, BaseItem>() {
                        @Override
                        public BaseItem apply(SDKNewsList.DataBean newsBean) throws Exception {
                            BaseItem<SDKNewsList.DataBean> baseItem = new BaseItem<>();
                            boolean hasImage = newsBean.isHas_image();
                            if (!hasImage) {
                                baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_NORMAL);
                            } else {
                                SDKNewsList.DataBean.MiddleImageBean middleImageBean = newsBean.getMiddle_image();
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
                            manager.put(Constants.CACHE_HEADLINE_NEWS, value);
//                            timeStamp += 20;
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
    }

    //两个方法没区别,只是刷新会重新赋值
    @Override
    public void loadMore() {
        final Category category = mNewsListFrag.getCategory();
        if (category != null) {
            ApiManager.getInstence().getSDKNewsList().getMore(category.toString(), timeStamp)
                    .map(new Function<SDKNewsList, List<SDKNewsList.DataBean>>() {

                        @Override
                        public List<SDKNewsList.DataBean> apply(SDKNewsList topNewsList) throws Exception {
                            List<SDKNewsList.DataBean> datas = topNewsList.getData();
                            timeStamp = datas.get(datas.size() - 1).getPublish_time();
                            return datas;
                        }
                    })
                    .flatMap(new Function<List<SDKNewsList.DataBean>, ObservableSource<SDKNewsList.DataBean>>() {
                        @Override
                        public ObservableSource<SDKNewsList.DataBean> apply(List<SDKNewsList.DataBean> dataBeans) throws Exception {
                            return Observable.fromIterable(dataBeans);
                        }
                    })
                    .map(new Function<SDKNewsList.DataBean, BaseItem>() {
                        @Override
                        public BaseItem apply(SDKNewsList.DataBean newsBean) throws Exception {
                            BaseItem<SDKNewsList.DataBean> baseItem = new BaseItem<>();
                            boolean hasImage = newsBean.isHas_image();
                            if (!hasImage) {
                                baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_NORMAL);
                            } else {
                                SDKNewsList.DataBean.MiddleImageBean middleImageBean = newsBean.getMiddle_image();
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
                            manager.put(Constants.CACHE_HEADLINE_NEWS, value);
                            if (value != null && value.size() > 0) {
                                mNewsListFrag.loadMoreSuccessed(value);
                            } else {
                                mNewsListFrag.loadAll();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogWritter.LogStr(e.getMessage() + "------------->errMsg");
                            mNewsListFrag.loadMoreFail(e.getMessage());
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
        ArrayList<BaseItem> topNews = manager.getSerializable(Constants.CACHE_HEADLINE_NEWS);
        if (topNews != null) {
            mNewsListFrag.refreshNewsSuccessed(topNews);
        } else {
        }
        refreshNews();
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
