package com.aiqing.newssdk.api;


import com.aiqing.newssdk.news.NewsHtmlBean;
import com.aiqing.newssdk.news.SDKNewsList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SDKNewsApi {
    @GET("v1/news/list/")
    Observable<SDKNewsList> getNewsList(@Query("category") String category);

    @GET("v1/news/list/")
    Observable<SDKNewsList> getMore(@Query("category") String category, @Query("timestamp") long timestamp);

    @GET("v1/news/urlhtml/")
    Observable<NewsHtmlBean> getHtml(@Query("url") String url);
}
