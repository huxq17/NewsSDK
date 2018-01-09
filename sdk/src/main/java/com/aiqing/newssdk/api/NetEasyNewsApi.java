package com.aiqing.newssdk.api;


import com.aiqing.newssdk.config.Constants;
import com.aiqing.newssdk.news.TopNewsList;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetEasyNewsApi {
    @GET("list/"+ Constants.NETEASY_NEWS_HEADLINE + "/{index}-20.html")
    Observable<TopNewsList> getTopNews(@Path("index") String index);

    @GET("{id}/full.html")
    Observable<ResponseBody> getNewsContent(@Path("id") String id);
}
