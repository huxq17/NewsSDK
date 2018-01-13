package com.aiqing.client;

import com.aiyou.toolkit.common.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

    public long getFileSize(File file) throws Exception {
        final long start = System.currentTimeMillis();
        final int cpuCore = Runtime.getRuntime().availableProcessors();
        final int poolSize = cpuCore + 1;
        ExecutorService service = Executors.newFixedThreadPool(poolSize);
        long total = 0;
        List<File> directories = new ArrayList<File>();
        directories.add(file);
        SubDirsAndSize subDirsAndSize = null;
        try {
            while (!directories.isEmpty()) {
                List<Future<SubDirsAndSize>> partialResults = new ArrayList<>();
                for (final File directory : directories) {
//                    partialResults.add(service.submit(new Callable<SubDirsAndSize>() {
//                        @Override
//                        public SubDirsAndSize call() throws Exception {
//                            return getSubDirsAndSize(directory);
//                        }
//                    }));
                    Future<SubDirsAndSize> result = service.submit(new Callable<SubDirsAndSize>() {
                        @Override
                        public SubDirsAndSize call() throws Exception {
                            return getSubDirsAndSize(directory);
                        }
                    });
                    result.get()
                }
                directories.clear();
                for (Future<SubDirsAndSize> partialResultFuture : partialResults) {
                    subDirsAndSize = partialResultFuture.get(100, TimeUnit.SECONDS);
                    total += subDirsAndSize.size;
                    directories.addAll(subDirsAndSize.subDirs);
                }
            }
            final long end = System.currentTimeMillis();
            LogUtils.e(String.format("文件夹大小: %dMB%n", total / (1024 * 1024)));
            LogUtils.e(String.format("所用时间: %.3fs%n", (end - start) / 1.0e3));
            return total;
        } finally {
            service.shutdown();
        }
    }
}