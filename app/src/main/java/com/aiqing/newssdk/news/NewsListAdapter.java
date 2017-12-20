package com.aiqing.newssdk.news;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiqing.newssdk.R;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by PandaQ on 2016/9/22.
 * email : 767807368@qq.com
 */

public class NewsListAdapter extends BaseRecyclerAdapter {

    private Context mContext;
    private int widthPx;
    private int heighPx;

    public NewsListAdapter(Fragment fragment) {
        mContext = fragment.getContext();
        float width = mContext.getResources().getDimension(R.dimen.news_image_width);
        widthPx = (int) width;
        heighPx = widthPx * 3 / 4;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.topnews_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, int RealPosition, BaseItem data) {
        if (holder instanceof ViewHolder) {
            NewsBean topNews = (NewsBean) data.getData();
            ((ViewHolder) holder).mNewsTitle.setText(topNews.getTitle());
            ((ViewHolder) holder).mSource.setText(topNews.getSource());
            String image = topNews.getImgsrc();//避免null引起Picasso崩溃
            if (!TextUtils.isEmpty(image)) {
                Picasso.with(mContext)
                        .load(image)
                        .resize(widthPx, heighPx)
                        .into(((ViewHolder) holder).mNewsImage);
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mNewsImage;
        TextView mNewsTitle;
        TextView mSource;

        ViewHolder(View view) {
            super(view);
            mNewsImage = (ImageView) view.findViewById(R.id.news_image);
            mNewsTitle = (TextView) view.findViewById(R.id.news_title);
            mSource = (TextView) view.findViewById(R.id.source);
        }
    }
}
