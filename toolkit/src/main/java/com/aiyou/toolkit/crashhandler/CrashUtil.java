package com.aiyou.toolkit.crashhandler;

import android.content.Context;

import com.aiyou.toolkit.crashhandler.capture.CrashCapturer;
import com.aiyou.toolkit.crashhandler.upload.UploadLogManager;
import com.aiyou.toolkit.crashhandler.utils.LogCollectorUtility;


public class CrashUtil {
    private static Context mContext;
    private static boolean isInit = false;


    private CrashUtil() {
    }

    public static void init(Context c, CrashUploader uploader) {
        if (c != null) {
            if (!isInit) {
                mContext = c.getApplicationContext();
                CrashCapturer capturer = new CrashCapturer(c);
                capturer.init();
                isInit = true;
            }
            upload(false, uploader);
        }
    }

    private static void upload(boolean isWifiOnly, CrashUploader uploader) {
        if (LogCollectorUtility.isNetworkConnected(mContext)) {
            boolean isWifiMode = LogCollectorUtility.isWifiConnected(mContext);
            if (!isWifiOnly || isWifiMode) {
                UploadLogManager.getInstance(mContext).uploadLogFile(uploader);
            }
        }
    }
}
