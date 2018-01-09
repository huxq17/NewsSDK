package com.aiqing.newssdk.view;

import android.content.Context;

public class NewsImageView extends android.support.v7.widget.AppCompatImageView {
    public NewsImageView(Context context) {
        super(context);
        setScaleType(ScaleType.CENTER_CROP);
    }
}

