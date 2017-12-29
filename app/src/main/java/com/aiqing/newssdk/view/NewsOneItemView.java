package com.aiqing.newssdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiqing.newssdk.R;
import com.aiqing.newssdk.utils.NewsTimeDrawer;

public class NewsOneItemView extends FrameLayout {
    int margin;
    private NewsTimeDrawer drawer;

    public NewsOneItemView(Context context) {
        super(context);
        setId(R.id.item_cardview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTransitionName(getResources().getString(R.string.top_news_parent));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setForeground(context.getResources().getDrawable(R.drawable.ripple_item));
        }
        margin = getDimen(R.dimen.margin_8dp);
        LayoutParams relativeLp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setPadding(0, margin, margin, margin);
        relativeLayout.setBackgroundColor(getColor(R.color.transparent_00ffffff));
        addView(relativeLayout, relativeLp);

        RelativeLayout.LayoutParams newsImagelp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        newsImagelp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        newsImagelp.addRule(RelativeLayout.CENTER_VERTICAL);
        newsImagelp.setMargins(margin, margin, margin, margin);
        NewsImageView newsImage = new NewsImageView(context);
        newsImage.setId(R.id.news_image);
        relativeLayout.addView(newsImage, newsImagelp);

        RelativeLayout.LayoutParams textImagelp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textImagelp.setMargins(margin, margin, 0, 0);
        textImagelp.addRule(RelativeLayout.LEFT_OF, R.id.news_image);
        TextView newsText = new TextView(context);
        newsText.setId(R.id.news_title);
        newsText.setTextColor(getColor(R.color.black_000000));
        newsText.setTextSize(getXmlDef(context, R.dimen.text_size_mid));
        relativeLayout.addView(newsText, textImagelp);

        RelativeLayout.LayoutParams sourcelp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        sourcelp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        sourcelp.addRule(RelativeLayout.BELOW, R.id.news_title);
        sourcelp.setMargins(margin, margin, margin, 0);
        sourceText = new TextView(context);
        sourceText.setCompoundDrawablePadding(margin);
        sourceText.setId(R.id.source);
        relativeLayout.addView(sourceText, sourcelp);
        drawer = new NewsTimeDrawer(sourceText.getPaint());
    }

    private int getDimen(int id) {
        return getResources().getDimensionPixelOffset(id);
    }

    private int getColor(int color) {
        return getResources().getColor(color);
    }

    private static TypedValue mTmpValue = new TypedValue();


    public static int getXmlDef(Context context, int id) {
        synchronized (mTmpValue) {
            TypedValue value = mTmpValue;
            context.getResources().getValue(id, value, true);
            return (int) TypedValue.complexToFloat(value.data);
        }
    }

    private TextView sourceText;
    private String time;

    public void setTime(String time) {
        this.time = time;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawer.draw(time, sourceText.getWidth() + margin + margin, getHeight() -margin-sourceText.getHeight()/2, canvas);
    }
}
