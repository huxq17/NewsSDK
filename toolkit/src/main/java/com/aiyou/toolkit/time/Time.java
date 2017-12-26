package com.aiyou.toolkit.time;


public class Time {
    private long netTime;
    private long nanoTime;

    public Time(long netTime) {
        this.netTime = netTime;
        this.nanoTime = System.nanoTime();
    }

    public long getNetTime() {
        long estimatedTime = System.nanoTime() - nanoTime;
        netTime += Math.round(estimatedTime * 1E-6);
        nanoTime = System.nanoTime();
        return netTime/1000;
    }
}
