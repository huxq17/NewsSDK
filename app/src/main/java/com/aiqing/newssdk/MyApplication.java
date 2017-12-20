package com.aiqing.newssdk;

import android.app.Application;

/**
 * Created by PandaQ on 2016/12/20.
 * email : 767807368@qq.com
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        CustomApplication.init(this);
    }
}
