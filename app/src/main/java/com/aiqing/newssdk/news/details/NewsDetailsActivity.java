package com.aiqing.newssdk.news.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.aiqing.newssdk.R;
import com.aiqing.newssdk.base.BaseActivity;
import com.aiqing.newssdk.config.Extras;

public class NewsDetailsActivity extends BaseActivity {
    private WebView mWebView;
    private String content;

    public static void start(Context context, String content) {
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra(Extras.DETAILS_CONTENT, content);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        mWebView = findViewById(R.id.wv_news_details);

        Intent intent = getIntent();
        content = intent.getStringExtra(Extras.DETAILS_CONTENT);
        if (!content.startsWith("http")) {
            mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        } else {
            mWebView.loadUrl(content);
        }
    }
}
