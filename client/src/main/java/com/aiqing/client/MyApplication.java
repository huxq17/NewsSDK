package com.aiqing.client;

import android.app.Application;

import com.aiqing.newssdk.SDKHelper;

/**
 * Created by PandaQ on 2016/12/20.
 * email : 767807368@qq.com
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SDKHelper.init(this);
    }
}
