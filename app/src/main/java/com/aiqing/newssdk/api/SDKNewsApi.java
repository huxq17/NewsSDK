package com.aiqing.newssdk.api;


import com.aiqing.newssdk.news.SDKNewsList;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by PandaQ on 2016/9/20.
 * email : 767807368@qq.com
 * 网易新闻Api 都默认一次加载20条
 */
public interface SDKNewsApi {
    @GET("v1/news/list/")
    Observable<SDKNewsList> getNewsList(@Query("category") String category);

    @GET("{id}/full.html")
    Observable<ResponseBody> getNewsContent(@Path("id") String id);
}
