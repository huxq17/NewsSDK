package com.aiqing.newssdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.huxq17.handygridview.scrollrunner.ICarrier;
import com.huxq17.handygridview.scrollrunner.ScrollRunner;


public class RotateImageView extends android.support.v7.widget.AppCompatImageView implements ICarrier {
    private ScrollRunner mRunner;

    public RotateImageView(Context context) {
        this(context, null);
    }


    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRunner = new ScrollRunner(this);
    }

    private int rotate;

    private void rotate(int rotate) {
        this.rotate = this.rotate + rotate;
        invalidate();
    }

    public void startRotate(int rotate, int duration) {
        mRunner.start(rotate - this.rotate, 0,duration);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.rotate(rotate, width / 2, height / 2);
        super.onDraw(canvas);

    }

    @Override
    public void onMove(int lastX, int lastY, int curX, int curY) {
        rotate(curX - lastX);
    }

    @Override
    public void onDone() {

    }
}
