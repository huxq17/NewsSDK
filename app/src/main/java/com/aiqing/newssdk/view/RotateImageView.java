package com.aiqing.newssdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class RotateImageView extends android.support.v7.widget.AppCompatImageView {
    public RotateImageView(Context context) {
        super(context);
    }

    private int rotate;

    public void setRotate(int rotate) {
        this.rotate = rotate;
        invalidate();
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.rotate(rotate,width/2,height/2);
        super.onDraw(canvas);

    }
}
