package com.aiqing.newssdk.view;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiqing.newssdk.R;
import com.aiyou.toolkit.common.DensityUtil;

public class NewsItemView extends FrameLayout {

    public NewsItemView(Context context) {
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
        int margin = getDimen(R.dimen.margin_8dp);

        RelativeLayout.LayoutParams textImagelp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textImagelp.setMargins(margin, margin, margin, 0);
        TextView newsText = new TextView(context);
        newsText.setId(R.id.news_title);
        newsText.setTextColor(getColor(R.color.black_000000));
        newsText.setTextSize(getXmlDef(context, R.dimen.text_size_mid));
        relativeLayout.addView(newsText, textImagelp);

        RelativeLayout.LayoutParams sourcelp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        sourcelp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        sourcelp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        sourcelp.addRule(RelativeLayout.BELOW, R.id.news_title);
        sourcelp.setMargins(margin, margin, margin, margin);
        TextView sourceText = new TextView(context);
        sourceText.setGravity(Gravity.CENTER_VERTICAL);
        sourceText.setId(R.id.source);
        sourceText.setCompoundDrawablePadding(DensityUtil.dip2px(context, 10));
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
