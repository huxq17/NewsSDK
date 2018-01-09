package com.aiqing.newssdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by PandaQ on 2016/12/20.
 * email : 767807368@qq.com
 */

public class CustomApplication {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取应用的版本号
     *
     * @return 应用版本号，默认返回1
     */
    public static int getAppVersionCode() {
        Context context = getContext();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
