package com.aiqing.client;

import com.aiyou.toolkit.common.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    public ScanFileManager(File file) {
        rootFile = file;
        LogUtils.e("cpuCore=" + cpuCore);
        directories.add(file);
    }

    private Object lock2 = new Object();

    public void scan() {
        final long start = System.currentTimeMillis();
        runNum = 0;
        while (!directories.isEmpty()) {
            File dir = directories.get(0);
            if (dir == null) {
                continue;
            }
            File files[] = dir.listFiles();
            LogUtils.e("length=" + files.length);
            for (final File file : files) {
                runNum++;
                mExecutor.execute(new ScanRunnable(file));
            }
            final long end = System.currentTimeMillis();
            LogUtils.e(String.format("wait 文件夹大小: %dMB%n", total.get() / (1024 * 1024)) + ";文件数量=" + count);
            LogUtils.e(String.format("wait 所用时间: %.3fs%n", (end - start) / 1.0e3));
            try {
                synchronized (lock2) {
                    LogUtils.e("wait");
                    lock2.wait();
                    LogUtils.e("wake up");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        final long end = System.currentTimeMillis();
        LogUtils.e(String.format("文件夹大小: %dMB%n", total.get() / (1024 * 1024)) + ";文件数量=" + count);
        LogUtils.e(String.format("所用时间: %.3fs%n", (end - start) / 1.0e3));
    }

    private void onEnd() {
        LogUtils.e("onEnd");
        synchronized (lock2) {
            lock2.notify();
        }
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
                synchronized (lock2) {
                    lock2.notifyAll();
                }
                LogUtils.e("run end");
            }
            runNum--;
//            if (runNum == 0) {
//                onEnd();
//            }
        }
    }
}
