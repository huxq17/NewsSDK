package com.aiyou.toolkit.time;


import com.aiyou.toolkit.tractor.listener.LoadListener;
import com.aiyou.toolkit.tractor.listener.impl.LoadListenerImpl;

public class TimeSyncUtils {
    public static final String SYNC_TIME_URL = "http://www.baidu.com";
    private static Time mCurrentTime;


    public static synchronized void syncTime(LoadListener listener) {
        if (mCurrentTime != null) {
            listener.onSuccess(mCurrentTime.getNetTime());
        } else {
            execute(listener);
        }
    }

    public static synchronized void syncTime(LoadListener listener, boolean sync) {
        if (sync) {
            if (mCurrentTime != null) {
                listener.onSuccess(mCurrentTime.getNetTime());
            } else {
                long time = SyncTimeTask.syncTime();
                mCurrentTime = new Time(time);
            }
        } else {
            syncTime(listener);
        }
    }

    public static synchronized void execute() {
        execute(null);
    }

    public static synchronized void execute(final LoadListener listener) {
        SyncTimeTask.execute(new LoadListenerImpl() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                mCurrentTime = new Time((Long) result);
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }

            @Override
            public void onFail(Object result) {
                super.onFail(result);
                if (listener != null) {
                    listener.onFail(result);
                }
            }
        });
    }

    public static long getNetTime() {
        if (mCurrentTime != null) {
            return mCurrentTime.getNetTime();
        }
        return 0;
    }

}
