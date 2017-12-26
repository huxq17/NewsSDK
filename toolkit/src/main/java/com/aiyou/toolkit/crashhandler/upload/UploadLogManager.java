package com.aiyou.toolkit.crashhandler.upload;

import android.content.Context;
import android.text.TextUtils;

import com.aiyou.toolkit.common.Utils;
import com.aiyou.toolkit.crashhandler.CrashUploader;
import com.aiyou.toolkit.crashhandler.capture.LogFileStorage;
import com.aiyou.toolkit.tractor.listener.impl.LoadListenerImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class UploadLogManager {
    private static final String TAG = UploadLogManager.class.getName();
    private static UploadLogManager sInstance;
    private Context mContext;

    private UploadLogManager(Context c) {
        this.mContext = c.getApplicationContext();
    }

    public static synchronized UploadLogManager getInstance(Context c) {
        if (sInstance == null) {
            sInstance = new UploadLogManager(c);
        }

        return sInstance;
    }

    private boolean uploadInternalFile(CrashUploader uploader) {
        File logFile = LogFileStorage.getInstance(UploadLogManager.this.mContext).getUploadInternalLogFile();
        if (logFile != null) {
            uploadLogFile(logFile, uploader);
            return true;
        } else {
            return false;
        }
    }

    private boolean uploadExternalFile(CrashUploader uploader) {
        File logFile = LogFileStorage.getInstance(UploadLogManager.this.mContext).getUploadExternalLogFile();
        if (logFile != null) {
            uploadLogFile(logFile, uploader);
            return true;
        } else {
            return false;
        }
    }

    private void uploadLogFile(File logFile, CrashUploader uploader) {
        FileInputStream logFileInputStream = null;
        InputStreamReader reader = null;
        try {
            logFileInputStream = new FileInputStream(logFile);
//                BufferedInputStream bufferedInputStream = new BufferedInputStream(logFileInputStream);
//                bufferedInputStream.re
            reader = new InputStreamReader(logFileInputStream);
            char[] bytes = new char[8192];
            StringBuilder stringBuilder = new StringBuilder();
            int length = -1;
            while ((length = reader.read(bytes)) != -1) {
                stringBuilder.append(bytes, 0, length);
            }
            String logInfo = stringBuilder.toString();
            if (!TextUtils.isEmpty(logInfo)) {
                uploader.uploadCrash(logInfo, new LoadListenerImpl() {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        LogFileStorage.getInstance(UploadLogManager.this.mContext).deleteUploadLogFile();
                    }
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.close(reader);
            Utils.close(logFileInputStream);
        }
    }

    public void uploadLogFile(CrashUploader uploader) {
        if (!uploadInternalFile(uploader)) {
            uploadExternalFile(uploader);
        } else {
            LogFileStorage.getInstance(UploadLogManager.this.mContext).deleteUploadLogFile();
        }

    }
}
