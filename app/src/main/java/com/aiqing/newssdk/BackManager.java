package com.aiqing.newssdk;


import java.util.Stack;

public enum BackManager {
    INSTANCE;
    private Stack<OnBackListener> backStack = new Stack<>();

    public interface OnBackListener {
        boolean onBack();
    }

    private OnBackListener listener;

    public boolean back() {
        boolean result = listener == null ? false : listener.onBack();
        if (result) {
            listener = null;
        }
        return result;
    }

    public void addBackListener(OnBackListener listener) {
        this.listener = listener;
    }

    public void removeBackListener(OnBackListener listener) {
        listener = null;
    }
}
