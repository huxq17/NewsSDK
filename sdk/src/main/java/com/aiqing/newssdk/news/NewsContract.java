package com.aiqing.newssdk.news;

import android.content.Context;

import com.aiqing.newssdk.base.ImpBasePresenter;
import com.aiqing.newssdk.base.ImpBaseView;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;

import java.util.ArrayList;

/**
 * Created by PandaQ on 2017/4/12.
 * 767807368@qq.com
 */

public interface NewsContract {
    interface View extends ImpBaseView {
        void showRefreshBar();

        Category getCategory();

        void hideRefreshBar();

        void refreshNews();

        void refreshNewsFail(String errorMsg);

        void refreshNewsSuccessed(ArrayList<BaseItem> topNews);

        void loadMoreNews();

        void loadMoreFail(String errorMsg);

        void loadMoreSuccess(ArrayList<BaseItem> topNewses);

        void loadAll();

        String getCacheKey();

        Context getContext();
    }

    interface Presenter extends ImpBasePresenter {
        void refreshNews();

        void loadMore();

        void loadCache();

        void viewNewsDetails(NewsList.DataBean dataBean);
    }
}
