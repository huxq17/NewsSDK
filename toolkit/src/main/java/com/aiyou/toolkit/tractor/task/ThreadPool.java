package com.aiyou.toolkit.tractor.task;


public interface ThreadPool {
    /**
     * 默认的线程数目为5个
     */
    static final int DEFALUT_THREAD_NUM = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 执行任务，具体的实现可根据自己的需求去实现
     *
     * @param command
     */
    void execute(Runnable command);
}
