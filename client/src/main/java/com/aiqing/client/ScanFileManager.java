package com.aiqing.client;

import com.aiyou.toolkit.common.LogUtils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/1/13.
 */

public class ScanFileManager {
    private final int cpuCore = Runtime.getRuntime().availableProcessors();
    private final int poolSize = cpuCore + 1;
    private ExecutorService mExecutor = Executors.newFixedThreadPool(poolSize);
    private File rootFile;

    public ScanFileManager(File file) {
        rootFile = file;
        LogUtils.e("cpuCore="+cpuCore);
    }
    public void scan(){

    }
}
