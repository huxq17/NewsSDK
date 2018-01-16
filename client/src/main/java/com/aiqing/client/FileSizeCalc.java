package com.aiqing.client;

import com.aiyou.toolkit.common.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileSizeCalc {

    static class SubDirsAndSize {
        public final long size;
        public final List<File> subDirs;
        public final int count;

        public SubDirsAndSize(int count, long size, List<File> subDirs) {
            this.size = size;
            this.count = count;
            this.subDirs = Collections.unmodifiableList(subDirs);
        }
    }

    private SubDirsAndSize getSubDirsAndSize(File file) {
        long total = 0;
        int count = 0;
        List<File> subDirs = new ArrayList<>();
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                if (child.isFile()) {
                    total += child.length();
                    count++;
                } else {
                    subDirs.add(child);
                }
            }
        }
        return new SubDirsAndSize(count, total, subDirs);
    }


    public void getFileSize(File rootDir) {
        final long start = System.currentTimeMillis();
        final int cpuCore = Runtime.getRuntime().availableProcessors();
        final int poolSize = cpuCore + 1;
        ExecutorService service = Executors.newFixedThreadPool(poolSize);
        long total = 0;
        int count = 0;
        ArrayList<File> directories = new ArrayList<>();
        directories.add(rootDir);
        SubDirsAndSize subDirsAndSize = null;
        try {
            while (!directories.isEmpty()) {
                List<Future<SubDirsAndSize>> partialResults = new ArrayList<>();
//                Iterator<File> iterator = directories.iterator();
//                while (iterator.hasNext()) {
//                    final File file= iterator.next();
//                    iterator.remove();
//                    partialResults.add(service.submit(new Callable<SubDirsAndSize>() {
//                        @Override
//                        public SubDirsAndSize call() throws Exception {
//                            return getSubDirsAndSize(file);
//                        }
//                    }));
//                }
                for (int i = directories.size() - 1; i >= 0; i--) {
                    final File directory = directories.remove(i);
                    partialResults.add(service.submit(new Callable<SubDirsAndSize>() {
                        @Override
                        public SubDirsAndSize call() throws Exception {
                            return getSubDirsAndSize(directory);
                        }
                    }));
                }
//                for (final File directory : directories) {
//                        partialResults.add(service.submit(new Callable<SubDirsAndSize>() {
//                            @Override
//                            public SubDirsAndSize call() throws Exception {
//                                return getSubDirsAndSize(directory);
//                            }
//                        }));
//                }
//                directories.clear();

                for (Future<SubDirsAndSize> partialResultFuture : partialResults) {
                    subDirsAndSize = partialResultFuture.get();
                    total += subDirsAndSize.size;
                    directories.addAll(subDirsAndSize.subDirs);
                    count += subDirsAndSize.count;
                }
            }
            final long end = System.currentTimeMillis();
            LogUtils.e(String.format("文件夹大小: %dMB%n", total / (1024 * 1024)) + ";count=" + count);
            LogUtils.e(String.format("所用时间: %.3fs%n", (end - start) / 1.0e3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }
    }
}