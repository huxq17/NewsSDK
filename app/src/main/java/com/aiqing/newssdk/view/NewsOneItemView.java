package com.aiqing.newssdk.view;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiqing.newssdk.R;

public class NewsOneItemView extends CardView {

    public NewsOneItemView(Context context) {
        super(context);
        setId(R.id.item_cardview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTransitionName(getResources().getString(R.string.top_news_parent));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setForeground(context.getResources().getDrawable(R.drawable.ripple_item));
        }
        LayoutParams relativeLp = new LayoutParams(LayoutParams.MATCH_PARENT, getDimen(R.dimen.news_item_height));
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setBackgroundColor(getColor(R.color.transparent_00ffffff));
        addView(relativeLayout, relativeLp);

        RelativeLayout.LayoutParams newsImagelp = new RelativeLayout.LayoutParams(getDimen(R.dimen.news_image_width), getDimen(R.dimen.news_image_height));
        int margin = getDimen(R.dimen.margin_8dp);
        newsImagelp.setMargins(margin, margin, margin, margin);
        ImageView newsImage = new ImageView(context);
        newsImage.setId(R.id.news_image);
        newsImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        relativeLayout.addView(newsImage, newsImagelp);

        RelativeLayout.LayoutParams textImagelp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textImagelp.setMargins(margin, margin, margin, 0);
        textImagelp.addRule(RelativeLayout.RIGHT_OF, R.id.news_image);
        TextView newsText = new TextView(context);
        newsText.setId(R.id.news_title);
        newsText.setTextColor(getColor(R.color.black_000000));
        newsText.setTextSize(getXmlDef(context, R.dimen.text_size_mid));
        relativeLayout.addView(newsText, textImagelp);

        RelativeLayout.LayoutParams sourcelp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        sourcelp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        sourcelp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        sourcelp.setMargins(margin, margin, margin, margin);
        TextView sourceText = new TextView(context);
        sourceText.setId(R.id.source);
        relativeLayout.addView(sourceText, sourcelp);

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
}
