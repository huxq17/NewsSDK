package com.aiqing.newssdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiqing.newssdk.R;
import com.aiqing.newssdk.decoration.SpaceItemDecoration;
import com.aiqing.newssdk.news.NewsBean;
import com.aiqing.newssdk.utils.NewsTimeDrawer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsThreeItemView extends FrameLayout {
    private RecyclerView mGridImages;
    int margin;
    private NewsTimeDrawer drawer;

    public NewsThreeItemView(Context context) {
        super(context);
        setId(R.id.item_cardview);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setTransitionName(getResources().getString(R.string.top_news_parent));
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setForeground(context.getResources().getDrawable(R.drawable.ripple_item));
//        }
        margin = getDimen(R.dimen.margin_8dp);
        LayoutParams relativeLp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setPadding(0, margin, margin, margin);
        relativeLayout.setBackgroundColor(getColor(R.color.transparent_00ffffff));
        addView(relativeLayout, relativeLp);

        RelativeLayout.LayoutParams textImagelp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textImagelp.setMargins(margin, margin, margin, margin);
        TextView newsText = new TextView(context);
        newsText.setId(R.id.news_title);
        newsText.setTextColor(getColor(R.color.black_000000));
        newsText.setTextSize(getXmlDef(context, R.dimen.text_size_mid));
        relativeLayout.addView(newsText, textImagelp);

        mGridImages = new RecyclerView(context);
        mGridImages.setFocusableInTouchMode(false);
        mGridImages.setFocusable(false);
        mGridImages.addItemDecoration(new SpaceItemDecoration(margin));
        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 3);
        mGridImages.setLayoutManager(mLayoutManager);
        mGridImages.setHasFixedSize(true);
        mGridImages.setId(R.id.news_grid_images);
        RelativeLayout.LayoutParams imagesImagelp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        imagesImagelp.addRule(RelativeLayout.BELOW, R.id.news_title);
        imagesImagelp.setMargins(0, 0, 0, 0);
        relativeLayout.addView(mGridImages, imagesImagelp);
        ImageAdapter adapter = new ImageAdapter();
        mGridImages.setAdapter(adapter);

        RelativeLayout.LayoutParams sourcelp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        sourcelp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        sourcelp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        sourcelp.addRule(RelativeLayout.BELOW, R.id.news_grid_images);
        sourcelp.setMargins(margin, margin, margin, 0);
        sourceText = new TextView(context);
        sourceText.setGravity(Gravity.CENTER_VERTICAL);
        sourceText.setId(R.id.source);
        sourceText.setCompoundDrawablePadding(margin);
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

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        public List<NewsBean> datas = new ArrayList<>();

        public void setData(List<NewsBean> datas) {
            this.datas.clear();
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            NewsImageView view = new NewsImageView(viewGroup.getContext());
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            ImageView imageView = (ImageView) viewHolder.itemView;
            String url = datas.get(position).getUrl();
            if (!TextUtils.isEmpty(url)) {
                Picasso.with(imageView.getContext()).load(url)
                        .into(imageView);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }
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
        int sourceWidth = sourceText.getWidth();
        int sourceHeight = sourceText.getHeight();
        drawer.draw(time, sourceWidth + margin + margin, getHeight()-margin-sourceHeight/2, canvas);
    }
}
