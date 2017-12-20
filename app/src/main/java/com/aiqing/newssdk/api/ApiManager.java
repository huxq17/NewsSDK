package com.aiqing.newssdk.api;

import com.aiqing.newssdk.config.Config;
import com.pandaq.pandaqlib.okhttp.CustomInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PandaQ on 2016/9/8.
 * email : 767807368@qq.com
 * 集中处理Api相关配置的Manager类
 */
public class ApiManager {

    private NetEasyNewsApi mNewsApi;
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
}
