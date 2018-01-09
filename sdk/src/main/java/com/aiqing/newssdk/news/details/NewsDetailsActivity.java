package com.aiqing.newssdk.news.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.aiqing.newssdk.R;
import com.aiqing.newssdk.base.BaseActivity;
import com.aiqing.newssdk.config.Extras;

public class NewsDetailsActivity extends BaseActivity {
    private WebView mWebView;
    private String content;
    private Toolbar toolbar;

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
        toolbar = findViewById(R.id.details_toolbar);
        toolbar.setTitle("头条");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().hide();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        content = intent.getStringExtra(Extras.DETAILS_CONTENT);
        if (!content.startsWith("http")) {
//            StringBuilder modifiedHtml = new StringBuilder();
//            modifiedHtml.append("<html>");
//            content = (content.split("<html>"))[1];
//            modifiedHtml.append("<head>"
//                  +  "<script type=\"text/javascript\">"
//                    + "var tables = document.getElementsByTagName('table');" + // 找到table标签
//                    "for(var i = 0; i<tables.length; i++){" +  // 逐个改变
//                    "tables[i].style.width = '100%';" +  // 宽度改为100%
//                    "tables[i].style.height = 'auto';" +
//                    "}" +
//                    "</script>"
//                    + "</head>");
//            modifiedHtml.append(content);
//            LogUtils.e("modifiedHtml = " + modifiedHtml);
//            mWebView.loadDataWithBaseURL(null, modifiedHtml.toString(), "text/html", "utf-8", null);
            mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        } else {

            mWebView.loadUrl(content);
        }
    }
}
