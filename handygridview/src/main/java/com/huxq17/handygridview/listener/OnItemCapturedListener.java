package com.huxq17.handygridview.listener;

import android.view.View;

public interface OnItemCapturedListener {
    /**
     * Called when user selected a view to drag.
     *
     * @param v captured view
     * @param position position of view
     */
    void onItemCaptured(View v,int position);

    /**
     * Called when user released the drag view.
     *
     * @param v released view
     * @param position position of view
     */
    void onItemReleased(View v,int position);
}
