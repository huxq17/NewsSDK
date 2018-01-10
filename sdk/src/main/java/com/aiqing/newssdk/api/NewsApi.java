package com.aiqing.newssdk.api;


import com.aiqing.newssdk.news.NewsHtmlBean;
import com.aiqing.newssdk.news.NewsList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("v1/news/list/")
    Observable<NewsList> getNewsList(@Query("category") String category);

    @GET("v1/news/list/")
    Observable<NewsList> getMore(@Query("category") String category, @Query("timestamp") long timestamp);

    @GET("v1/news/urlhtml/")
    Observable<NewsHtmlBean> getHtml(@Query("url") String url);
}
