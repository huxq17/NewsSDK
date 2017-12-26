package com.aiyou.toolkit.crashhandler.capture;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;

import com.aiyou.toolkit.crashhandler.utils.LogCollectorUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class CrashCapturer implements UncaughtExceptionHandler {
    private static final String TAG = CrashCapturer.class.getName();
    private static final String CHARSET = "UTF-8";
    private Context mContext;
    private UncaughtExceptionHandler mDefaultCrashHandler;

    public CrashCapturer(Context c) {
        this.mContext = c.getApplicationContext();
    }

    public void init() {
        if (mContext != null) {
            mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        this.handleException(ex);
        if (this.mDefaultCrashHandler != null) {
            this.mDefaultCrashHandler.uncaughtException(thread, ex);
        }
    }

    private void handleException(Throwable ex) {
        String s = this.fomatCrashInfo(ex);
        LogFileStorage.getInstance(this.mContext).saveLogFile2Internal(s);
        boolean var3 = LogFileStorage.getInstance(this.mContext).saveLogFile2SDcard(s, true);
    }

    private String fomatCrashInfo(Throwable ex) {
        StringWriter info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        String dump = info.toString();
        JSONObject jb = new JSONObject();

        try {
            jb.put("logTime", LogCollectorUtility.getCurrentTime());
            jb.put("appVerName", LogCollectorUtility.getVerName(this.mContext));
            jb.put("appVerCode", LogCollectorUtility.getVerCode(this.mContext));
            jb.put("OsVer", VERSION.RELEASE);
            jb.put("vendor", Build.MANUFACTURER);
            jb.put("model", Build.MODEL);
            jb.put("mid", LogCollectorUtility.getMid(this.mContext));
            jb.put("exception", ex.toString());
            jb.put("crashMD5", LogCollectorUtility.getMD5Str(dump));
            jb.put("crashDump", dump);
        } catch (JSONException var7) {
            var7.printStackTrace();
        }
        return jb.toString();
    }
}
