package com.aiqing.newssdk.news;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiqing.newssdk.R;
import com.aiqing.newssdk.view.NewsItemView;
import com.aiqing.newssdk.view.NewsOneItemView;
import com.aiqing.newssdk.view.NewsThreeItemView;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NewsListAdapter extends BaseRecyclerAdapter {

    private Context mContext;

    public NewsListAdapter(Fragment fragment) {
        mContext = fragment.getContext();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        prepare();
    }

    SparseArray<BaseVH> vhArray = new SparseArray<>();

    public void prepare() {
        vhArray.clear();
        vhArray.put(RecyclerItemType.TYPE_NORMAL.getiNum(), new ViewHolder(new NewsItemView(mContext)));
        vhArray.put(RecyclerItemType.TYPE_ONE.getiNum(), new OneViewHolder(new NewsOneItemView(mContext)));
        vhArray.put(RecyclerItemType.TYPE_THREE.getiNum(), new ThreeViewHolder(new NewsThreeItemView(mContext)));
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        BaseVH vh = null;
        BaseVH viewHolder = vhArray.get(viewType);
        Class vhcls = viewHolder.getClass();
        Class viewcls = viewHolder.itemView.getClass();
        try {
            Constructor viewConstructor = viewcls.getDeclaredConstructor(Context.class);
            Constructor vhConstructor = vhcls.getDeclaredConstructor(View.class);
            vh = (BaseVH) vhConstructor.newInstance(viewConstructor.newInstance(mContext));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (vh == null) ? new ViewHolder(new NewsItemView(mContext)) : vh;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, int RealPosition, BaseItem data) {
        if (holder instanceof BaseVH) {
            BaseVH vh = (BaseVH) holder;
            vh.bind(data);
        }
    }

    static abstract class BaseVH<T> extends RecyclerView.ViewHolder {
        protected int widthPx;
        protected int heighPx;

        public BaseVH(View itemView) {
            super(itemView);
            float width = itemView.getResources().getDimension(R.dimen.news_image_width);
            widthPx = (int) width;
            heighPx = widthPx * 3 / 4;
        }

        public abstract void bind(T data);
    }

    static class ViewHolder extends BaseVH<BaseItem> {
        ImageView mNewsImage;
        TextView mNewsTitle;
        TextView mSource;

        ViewHolder(View view) {
            super(view);
            mNewsImage = (ImageView) view.findViewById(R.id.news_image);
            mNewsTitle = (TextView) view.findViewById(R.id.news_title);
            mSource = (TextView) view.findViewById(R.id.source);
        }

        @Override
        public void bind(BaseItem data) {
            SDKNewsList.DataBean topNews = (SDKNewsList.DataBean) data.getData();
            mNewsTitle.setText(topNews.getTitle());
            mSource.setText(topNews.getMedia_name());
            String image = null;//避免null引起Picasso崩溃
            if (!TextUtils.isEmpty(image)) {
                Picasso.with(mNewsTitle.getContext())
                        .load(image)
                        .resize(widthPx, heighPx)
                        .into(mNewsImage);
            }
        }
    }

    static class OneViewHolder extends BaseVH<BaseItem> {
        ImageView mNewsImage;
        TextView mNewsTitle;
        TextView mSource;

        OneViewHolder(View view) {
            super(view);
            mNewsImage = (ImageView) view.findViewById(R.id.news_image);
            mNewsTitle = (TextView) view.findViewById(R.id.news_title);
            mSource = (TextView) view.findViewById(R.id.source);
        }

        @Override
        public void bind(BaseItem data) {
            SDKNewsList.DataBean topNews = (SDKNewsList.DataBean) data.getData();
            mNewsTitle.setText(topNews.getTitle());
            mSource.setText(topNews.getMedia_name());
            String image = null;//避免null引起Picasso崩溃
            if (!TextUtils.isEmpty(image)) {
                Picasso.with(mNewsTitle.getContext())
                        .load(image)
                        .resize(widthPx, heighPx)
                        .into(mNewsImage);
            }
        }
    }

    static class ThreeViewHolder extends BaseVH<BaseItem> {
        ImageView mNewsImage;
        TextView mNewsTitle;
        TextView mSource;

        ThreeViewHolder(View view) {
            super(view);
            mNewsImage = (ImageView) view.findViewById(R.id.news_image);
            mNewsTitle = (TextView) view.findViewById(R.id.news_title);
            mSource = (TextView) view.findViewById(R.id.source);
        }

        @Override
        public void bind(BaseItem data) {
            SDKNewsList.DataBean topNews = (SDKNewsList.DataBean) data.getData();
            mNewsTitle.setText(topNews.getTitle());
            mSource.setText(topNews.getMedia_name());
            String image = null;//避免null引起Picasso崩溃
            if (!TextUtils.isEmpty(image)) {
                Picasso.with(mNewsTitle.getContext())
                        .load(image)
                        .resize(widthPx, heighPx)
                        .into(mNewsImage);
            }
        }
    }
}
