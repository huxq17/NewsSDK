package com.aiyou.toolkit.crashhandler.capture;

import android.content.Context;

import com.aiyou.toolkit.common.Utils;
import com.aiyou.toolkit.crashhandler.utils.LogCollectorUtility;
import com.aiyou.toolkit.tractor.utils.Util;

import java.io.File;
import java.io.FileOutputStream;

public class LogFileStorage {
    private static final String TAG = LogFileStorage.class.getName();
    public static final String LOG_SUFFIX = ".log";
    private static final String CHARSET = "UTF-8";
    private static LogFileStorage sInstance;
    private Context mContext;

    private LogFileStorage(Context ctx) {
        this.mContext = ctx.getApplicationContext();
    }

    public static synchronized LogFileStorage getInstance(Context ctx) {
        if (ctx == null) {
            return null;
        } else {
            if (sInstance == null) {
                sInstance = new LogFileStorage(ctx);
            }

            return sInstance;
        }
    }

    public File getUploadInternalLogFile() {
        File dir = this.mContext.getFilesDir();
        File logFile = new File(dir, LogCollectorUtility.getMid(this.mContext) + ".log");
        return logFile.exists() ? logFile : null;
    }

    public File getUploadExternalLogFile() {
        File dir = LogCollectorUtility.getExternalDir(this.mContext, "Log");
        File logFile = new File(dir, LogCollectorUtility.getMid(this.mContext) + ".log");
        return logFile.exists() ? logFile : null;
    }

    public boolean deleteUploadLogFile() {
        File internalLogFile = LogFileStorage.getInstance(this.mContext).getUploadInternalLogFile();
        File externalLogFile = LogFileStorage.getInstance(this.mContext).getUploadExternalLogFile();
        Utils.deleteFileSafely(externalLogFile);
        Utils.deleteFileSafely(internalLogFile);
        return true;
    }

    public boolean saveLogFile2Internal(String logString) {
        FileOutputStream fos = null;
        try {
            File e = this.mContext.getFilesDir();
            if (!e.exists()) {
                e.mkdirs();
            }
            File logFile = new File(e, LogCollectorUtility.getMid(this.mContext) + ".log");
            fos = new FileOutputStream(logFile, true);
            fos.write(logString.getBytes("UTF-8"));
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        } finally {
            Util.closeQuietly(fos);
        }
    }

    public boolean saveLogFile2SDcard(String logString, boolean isAppend) {
        FileOutputStream fos = null;
        if (!LogCollectorUtility.isSDcardExsit()) {
            return false;
        } else {
            try {
                File e = this.getExternalLogDir();
                if (!e.exists()) {
                    e.mkdirs();
                }

                File logFile = new File(e, LogCollectorUtility.getMid(this.mContext) + ".log");
                fos = new FileOutputStream(logFile, isAppend);
                fos.write(logString.getBytes("UTF-8"));
                fos.close();
                return true;
            } catch (Exception var6) {
                var6.printStackTrace();
                return false;
            } finally {
                Util.closeQuietly(fos);
            }
        }
    }

    private File getExternalLogDir() {
        File logDir = LogCollectorUtility.getExternalDir(this.mContext, "Log");
        return logDir;
    }
}
