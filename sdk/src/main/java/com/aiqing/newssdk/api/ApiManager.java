package com.aiqing.newssdk.api;

import com.aiqing.newssdk.config.Config;
import com.pandaq.pandaqlib.okhttp.CustomInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private NetEasyNewsApi mNewsApi;
    private NewsApi mSDKNewsApi;
    private static ApiManager sApiManager;

    private static OkHttpClient mClient = new OkHttpClient.Builder()
            .addInterceptor(new CustomInterceptor())
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    private ApiManager() {

    }

    public static ApiManager getInstence() {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager();
                }
            }
        }
        return sApiManager;
    }

    /**
     * 封装网易新闻API
     */
    public NetEasyNewsApi getTopNewsServie() {
        if (mNewsApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.NETEASY_NEWS_API)
                    .client(mClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mNewsApi = retrofit.create(NetEasyNewsApi.class);
        }
        return mNewsApi;
    }

    public NewsApi getSDKNewsList() {
        if (mSDKNewsApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.SDK_API_URL)
                    .client(mClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mSDKNewsApi = retrofit.create(NewsApi.class);
        }
        return mSDKNewsApi;
    }
}
