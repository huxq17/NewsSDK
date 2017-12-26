package com.aiyou.toolkit.crashhandler;

import com.aiyou.toolkit.tractor.listener.LoadListener;

public interface CrashUploader {
    void uploadCrash(String log, LoadListener listener);
}
