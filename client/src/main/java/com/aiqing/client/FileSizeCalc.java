package com.aiqing.client;

import com.aiyou.toolkit.common.LogUtils;
import com.aiyou.toolkit.tractor.task.Task;
import com.aiyou.toolkit.tractor.task.TaskPool;
import com.aiyou.toolkit.tractor.task.threadpool.FixedThreadPool;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileSizeCalc {

    static class SubDirsAndSize {
        public final long size;
        public final List<File> subDirs;

        public SubDirsAndSize(long size, List<File> subDirs) {
            this.size = size;
            this.subDirs = Collections.unmodifiableList(subDirs);
        }
    }

    private SubDirsAndSize getSubDirsAndSize(File file) {
        long total = 0;
        List<File> subDirs = new ArrayList<File>();
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isFile())
                        total += child.length();
                    else
                        subDirs.add(child);
                }
            }
        }
        return new SubDirsAndSize(total, subDirs);
    }

    ConcurrentLinkedQueue<File> directories = new ConcurrentLinkedQueue<>();
    long total = 0;
    int putSize = 0;
    int count = 0;
    ExecutorService service = Executors.newCachedThreadPool();
    final int cpuCore = Runtime.getRuntime().availableProcessors();
    final int poolSize = cpuCore + 1;

    public class ScanRunnable extends Task {
        File file;

        public ScanRunnable(File file) {
            this.file = file;
        }

        @Override
        public void onRun() {
            if (file.isFile()) {
                total += file.length();
                count++;
            } else {
                directories.offer(file);
            }
            if (TaskPool.getInstance().getTaskSize() >= poolSize) {
                condition.signal();
            }
//            if(TaskPool.getInstance().getTaskSize()==1){
//                condition2.signal();
//            }
        }

        @Override
        public void cancelTask() {

        }
    }

    Lock mLock = new ReentrantLock();
    Condition condition = mLock.newCondition();
    Condition condition2 = mLock.newCondition();

    public long getFileNum(File rootDir) {
        final long start = System.currentTimeMillis();
        directories.add(rootDir);
        TaskPool taskPool = TaskPool.getInstance();
        taskPool.setExecutorService(new FixedThreadPool(poolSize));
        while (!directories.isEmpty() || taskPool.getTaskSize() > 0) {
//            if(directories.isEmpty()){
//                try {
//                    condition2.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            File dir = directories.poll();
            if (dir == null) {
                continue;
            }
            File files[] = dir.listFiles();
            for (final File file : files) {
                taskPool.execute(new ScanRunnable(file));
                if (taskPool.getTaskSize() >= poolSize) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        final long end = System.currentTimeMillis();
        LogUtils.e(String.format("文件夹大小: %dMB%n", total / (1024 * 1024)) + ";文件数量=" + count);
        LogUtils.e(String.format("所用时间: %.3fs%n", (end - start) / 1.0e3));
        return total;
    }

    public long getFileSize(File rootDir) throws Exception {
        final long start = System.currentTimeMillis();
        directories.add(rootDir);
        SubDirsAndSize subDirsAndSize = null;
        TaskPool taskPool = TaskPool.getInstance();
        taskPool.setExecutorService(new FixedThreadPool(poolSize));
        try {
            while (!directories.isEmpty() || taskPool.getTaskSize() > 0) {
                File dir = directories.poll();
                if (dir == null) {
                    continue;
                }
                File files[] = dir.listFiles();
//                List<Future<SubDirsAndSize>> partialResults = new ArrayList<>();
                putSize = 0;
                for (final File file : files) {
                    putSize++;
                    taskPool.execute(new ScanRunnable(file));
                }
            }
            final long end = System.currentTimeMillis();
            LogUtils.e(String.format("文件夹大小: %dMB%n", total / (1024 * 1024)) + ";文件数量=" + count);
            LogUtils.e(String.format("所用时间: %.3fs%n", (end - start) / 1.0e3));
            return total;
        } finally {
            service.shutdown();
        }
    }
}