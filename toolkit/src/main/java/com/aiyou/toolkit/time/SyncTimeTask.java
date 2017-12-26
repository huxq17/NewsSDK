package com.aiyou.toolkit.time;

import com.aiyou.toolkit.tractor.listener.LoadListener;
import com.aiyou.toolkit.tractor.listener.impl.LoadListenerImpl;
import com.aiyou.toolkit.tractor.task.Task;
import com.aiyou.toolkit.tractor.task.TaskPool;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CopyOnWriteArrayList;


public class SyncTimeTask extends Task {
    private CopyOnWriteArrayList<LoadListener> listeners = new CopyOnWriteArrayList<>();
    private static SyncTimeTask instance = new SyncTimeTask();
    private LoadListener mListener = new LoadListenerImpl() {
        @Override
        public void onSuccess(Object result) {
            super.onSuccess(result);
            notifyTimeSuccess(result);
        }

        @Override
        public void onFail(Object result) {
            super.onFail(result);
            notifyTimeFail(result);
        }
    };

    private SyncTimeTask(Object... tag) {
        setTaskName(getClass().getSimpleName());
        if (tag.length > 0) {
            setTag(tag[0]);
        }
        setListener(mListener);
    }

    public void setListener() {
        super.setListener(mListener);
    }

    public static void execute(LoadListener listener) {
        instance.addListener(listener);
        if (!instance.isRunning()) {
            instance.setListener();
            TaskPool.getInstance().execute(instance);
        }
    }

    public void addListener(LoadListener listener) {
        if (listener != null)
            listeners.add(listener);
    }

    public void removeListener(LoadListener listener) {
        listeners.remove(listener);
    }

    public void clearListener() {
        listeners.clear();
    }

    public void notifyTimeSuccess(Object time) {
        for (LoadListener listener : listeners) {
            listener.onSuccess(time);
        }
        clearListener();
    }

    public void notifyTimeFail(Object time) {
        for (LoadListener listener : listeners) {
            listener.onFail(time);
        }
        clearListener();
    }

    @Override
    public void onRun() {
        long time = syncTime();
        if (time != 0) {
            notifySuccess(time);
        } else {
            notifyFail(null);
        }
    }

    @Override
    public void cancelTask() {

    }

    public static long syncTime() {
        try {
            URL url = new URL(TimeSyncUtils.SYNC_TIME_URL);
            URLConnection uc = url.openConnection();
            uc.connect();
            return uc.getDate();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
