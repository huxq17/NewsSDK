package com.aiqing.client;

import com.aiyou.toolkit.common.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2018/1/13.
 */

public class ScanFileManager {
    private final int cpuCore = Runtime.getRuntime().availableProcessors();
    private final int poolSize = cpuCore + 1;
    private ExecutorService mExecutor = Executors.newFixedThreadPool(poolSize);
    private File rootFile;
    private ArrayList<File> directories = new ArrayList<>();
    private volatile int runNum;
    private volatile boolean wait;
    Lock mLock = new ReentrantLock();
    Condition condition = mLock.newCondition();
    private AtomicLong total = new AtomicLong();
    private AtomicInteger count = new AtomicInteger();

    Semaphore semaphore = new Semaphore(poolSize);

    public ScanFileManager(File file) {
        rootFile = file;
        LogUtils.e("cpuCore=" + cpuCore);
        directories.add(file);
    }

    public void scan() {
        final long start = System.currentTimeMillis();
        runNum = 0;
        while (!directories.isEmpty()||runNum>0) {
            if(directories.size()==0){
                LogUtils.e("runNum="+runNum);
                break;
            };
            File dir = directories.remove(0);
            if (dir == null) {
                continue;
            }
            File files[] = dir.listFiles();
            LogUtils.e("length=" + files.length);
            for (final File file : files) {
                runNum++;
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mExecutor.execute(new ScanRunnable(file));
            }
        }
        final long end = System.currentTimeMillis();
        LogUtils.e(String.format("文件夹大小: %dMB%n", total.get() / (1024 * 1024)) + ";文件数量=" + count);
        LogUtils.e(String.format("所用时间: %.3fs%n", (end - start) / 1.0e3));
    }

    public class ScanRunnable implements Runnable {
        File file;

        public ScanRunnable(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            if (file.isFile()) {
                total.addAndGet(file.length());
                count.incrementAndGet();
            } else {
                directories.add(file);
//                if (wait)
            }
            semaphore.release();
            runNum--;
            if (runNum == 0) {
                onEnd();
            }
        }
    }

    private void onEnd() {
        LogUtils.e("onEnd");
    }
}
