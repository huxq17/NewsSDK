package com.aiqing.newssdk.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

public class NewsTimeDrawer {
    private TextPaint mTextPaint;
    StaticLayout tipsLayout;

    public NewsTimeDrawer(TextPaint textPaint) {
        this.mTextPaint = textPaint;
    }

    public void draw(String text, int left, int top, Canvas canvas) {
        int textWidth = 0;
        int textHeight = 0;
        int offset = 0;
        if (!TextUtils.isEmpty(text)) {
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            textHeight = (int) (fontMetrics.descent - fontMetrics.ascent);
            textWidth = (int) mTextPaint.measureText(text) + 1;
            offset = (int) ( fontMetrics.descent*2-fontMetrics.bottom);
        }
//            tipsLayout = new StaticLayout(text, mTextPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.5f, 0f, false);
        top = top + textHeight / 2;
//        canvas.save();
//        canvas.translate(left, top);
        top -=offset;
        canvas.drawText(text,left,top,mTextPaint);
//        tipsLayout.draw(canvas);
//        canvas.restore();
//        canvas.drawLine(0,top,left+200,top,new Paint());
    }
}
