package com.aiqing.newssdk.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.aiqing.newssdk.utils.DateUtils;
import com.aiqing.newssdk.view.NewsItemView;
import com.aiqing.newssdk.view.NewsOneItemView;
import com.aiqing.newssdk.view.NewsThreeItemView;
import com.aiyou.toolkit.common.DensityUtil;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            vh.bind(RealPosition, data);
        }
    }

    static abstract class BaseVH<T> extends RecyclerView.ViewHolder {
        protected int widthPx;
        protected int heighPx;
        protected final Map<Object, Target> targetMap = new HashMap<>();
        protected Context mContext;

        public BaseVH(View itemView) {
            super(itemView);
            float width = itemView.getResources().getDimension(R.dimen.news_image_width);
            widthPx = (int) width;
            heighPx = widthPx * 3 / 4;
            mContext = itemView.getContext();
        }

        public void enqueue(String url, Object key, Target target) {
            if (target != null && targetMap.get(key) != target) {
                Picasso.with(mContext).cancelRequest(target);
                targetMap.put(target, target);
            }
            int width = DensityUtil.dip2px(mContext, 30);
            int height = DensityUtil.dip2px(mContext, 25);
            Picasso.with(mContext)
                    .load(url)
                    .resize(width, height)
                    .into(target);
        }

        public abstract void bind(int position, T data);
    }

    static class ViewHolder extends BaseVH<BaseItem> {
        TextView mNewsTitle;
        TextView mSource;

        ViewHolder(View view) {
            super(view);
            mNewsTitle = (TextView) view.findViewById(R.id.news_title);
            mSource = (TextView) view.findViewById(R.id.source);
        }

        @Override
        public void bind(int position, BaseItem data) {
            NewsList.DataBean topNews = (NewsList.DataBean) data.getData();
            mNewsTitle.setText(topNews.getTitle());
            mSource.setText(topNews.getMedia_name());
            long time  = topNews.getPublish_time();
            NewsItemView threeItemView  = (NewsItemView) itemView;
            threeItemView.setTime(DateUtils.convertTimeToFormat(time));
            final String avatar = topNews.getMedia_avatar_url();
            if (!TextUtils.isEmpty(avatar)) {
                enqueue(avatar, mSource, new DrawableCallBack(mContext, avatar) {
                    public void onDrawableLoaded(Drawable drawable, String url) {
//                      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                      mNewsTitle.setCompoundDrawables(drawable,null,null,null);
                        mSource.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                });
            } else {
                mSource.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
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
        public void bind(int position, BaseItem data) {
            NewsList.DataBean topNews = (NewsList.DataBean) data.getData();
            mNewsTitle.setText(topNews.getTitle());
            mSource.setText(topNews.getMedia_name());
            final String avatar = topNews.getMedia_avatar_url();
            if (!TextUtils.isEmpty(avatar)) {
                enqueue(avatar, mSource, new DrawableCallBack(mContext, avatar) {
                    public void onDrawableLoaded(Drawable drawable, String url) {
//                      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                      mNewsTitle.setCompoundDrawables(drawable,null,null,null);
                        mSource.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                });
            } else {
                mSource.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            long time  = topNews.getPublish_time();
            NewsOneItemView threeItemView  = (NewsOneItemView) itemView;
            threeItemView.setTime(DateUtils.convertTimeToFormat(time));
            try {
                String url = topNews.getMiddle_image().getUrl_list().get(0).getUrl();
                if (!TextUtils.isEmpty(url)) {
                    Picasso.with(mContext).load(url)
                            .into(mNewsImage);
                }
            } catch (Exception e) {
            }
        }
    }

    static class ThreeViewHolder extends BaseVH<BaseItem> {
        TextView mNewsTitle;
        TextView mSource;
        RecyclerView mGridImages;

        ThreeViewHolder(View view) {
            super(view);
            mNewsTitle = (TextView) view.findViewById(R.id.news_title);
            mSource = (TextView) view.findViewById(R.id.source);
            mGridImages = view.findViewById(R.id.news_grid_images);
        }

        @Override
        public void bind(int position, BaseItem data) {
            NewsList.DataBean topNews = (NewsList.DataBean) data.getData();
            mNewsTitle.setText(topNews.getTitle());
            mSource.setText(topNews.getMedia_name());
            long time  = topNews.getPublish_time();
            NewsThreeItemView threeItemView  = (NewsThreeItemView) itemView;
            threeItemView.setTime(DateUtils.convertTimeToFormat(time));
            final String avatar = topNews.getMedia_avatar_url();
            if (!TextUtils.isEmpty(avatar)) {
                enqueue(avatar, mSource, new DrawableCallBack(mContext, avatar) {
                    public void onDrawableLoaded(Drawable drawable, String url) {
//                      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                      mNewsTitle.setCompoundDrawables(drawable,null,null,null);
                        mSource.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                });
            } else {
                mSource.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            NewsThreeItemView.ImageAdapter adapter = (NewsThreeItemView.ImageAdapter) mGridImages.getAdapter();
            List<NewsBean> urls = topNews.getMiddle_image().getUrl_list();
            adapter.setData(urls);
//            mGridImages.setAdapter(adapter);
        }
    }

    public static abstract class DrawableCallBack implements Target {
        Context context;
        String url;

        public DrawableCallBack(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            onDrawableLoaded(new BitmapDrawable(context.getResources(), bitmap), url);
        }

        abstract void onDrawableLoaded(Drawable drawable, String url);

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
