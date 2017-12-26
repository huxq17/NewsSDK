package com.aiyou.toolkit.tractor.listener.impl;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiyou.toolkit.common.DensityUtil;
import com.aiyou.toolkit.common.IDFactory;
import com.aiyou.toolkit.common.LogUtils;
import com.aiyou.toolkit.tractor.listener.LoadListener;
import com.aiyou.toolkit.tractor.task.Task;
import com.aiyou.toolkit.tractor.task.TaskPool;
import com.aiyou.toolkit.tractor.utils.BackGroudSeletor;


public class LoadListenerImpl implements LoadListener {
    private Context context;
    private Dialog mProgressDialog;
    private String mMessage;
    private boolean mShowCloseButton = true;
    private long mDismissTime = 0_0_0;

    public LoadListenerImpl() {
    }

    public LoadListenerImpl(Context context) {
        this.context = context;
        if (context != null) {
            initProgressDialog(null);
        }
    }

    public LoadListenerImpl(Context context, String Message) {
        this.context = context;
        mMessage = Message;
        if (context != null) {
            initProgressDialog(Message);
        }
    }

    public LoadListenerImpl(Context context, String Message, boolean show) {
        this.context = context;
        mMessage = Message;
        mShowCloseButton = show;
        if (context != null) {
            initProgressDialog(Message);
        }
    }

    @Override
    public void onFail(Object result) {
        dimiss(mDismissTime);
    }

    @Override
    public void onStart(Object result) {
        show();
    }

    public void doDimiss() {
        try {
            if (null != mProgressDialog && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (progressBar != null) {
                stopAnim(progressBar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param time
     */
    public void setDismissTime(long time) {
        mDismissTime = time;
    }

    /**
     * @param dismissTime 在dialog消失前延迟的时间，不传则不延迟
     */
    public void dimiss(final long... dismissTime) {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            if (dismissTime != null && dismissTime[0] > 0) {
                LoadListenerImpl listener = new LoadListenerImpl() {
                    @Override
                    public void onSuccess(Object result) {
                        LogUtils.i("onSuccess mProgressDialog.isShowing()="+mProgressDialog.isShowing());
                        super.onSuccess(result);
                        doDimiss();
                    }
                };
                //做一个延迟关闭dialog的任务
                TaskPool.getInstance().execute(new Task("dimissTask", listener) {
                    @Override
                    public void onRun() {
                        SystemClock.sleep(dismissTime[0]);
                    }

                    @Override
                    public void cancelTask() {

                    }
                });
            }else {
                doDimiss();
            }
        }
    }

    ImageView progressBar;

    private void show() {
        if (null != mProgressDialog && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
            RelativeLayout contentView = new RelativeLayout(context);
            ViewGroup.LayoutParams rlp = new ViewGroup.LayoutParams(
                    DensityUtil.dip2px(context, 330), DensityUtil.dip2px(
                    context, 84));
            contentView.setBackgroundColor(Color.WHITE);

            RelativeLayout.LayoutParams pblp = new RelativeLayout.LayoutParams(
                    DensityUtil.dip2px(context, 19), DensityUtil.dip2px(
                    context, 19));
            progressBar = new ImageView(context);
            progressBar.setId(IDFactory.generateId());
            pblp.addRule(RelativeLayout.CENTER_VERTICAL);
            pblp.setMargins(DensityUtil.dip2px(context, 24), 0,
                    DensityUtil.dip2px(context, 24), 0);
            contentView.addView(progressBar, pblp);
            startAnim(progressBar);
            ImageView iv = null;
            if (mShowCloseButton) {
                iv = new ImageView(context);
                iv.setId(IDFactory.generateId());
                RelativeLayout.LayoutParams ivlp = new RelativeLayout.LayoutParams(
                        DensityUtil.dip2px(context, 27), DensityUtil.dip2px(
                        context, 27));
                iv.setImageDrawable(BackGroudSeletor.getdrawble("gy_image_close",
                        context));
                ivlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                ivlp.addRule(RelativeLayout.CENTER_VERTICAL);
                ivlp.setMargins(DensityUtil.dip2px(context, 24), 0,
                        DensityUtil.dip2px(context, 24), 0);
                contentView.addView(iv, ivlp);
                iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onCancelClick();
                    }
                });
            }

            tv = new TextView(context);
            RelativeLayout.LayoutParams tvlp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            tvlp.addRule(RelativeLayout.CENTER_VERTICAL);
            tvlp.addRule(RelativeLayout.RIGHT_OF, progressBar.getId());
            if (iv != null) {
                tvlp.addRule(RelativeLayout.LEFT_OF, iv.getId());
            }
            tv.setText(mMessage);
            tv.setTextSize(18);
            tv.setTextColor(Color.BLACK);
            contentView.addView(tv, tvlp);

            contentView.setBackgroundDrawable(BackGroudSeletor.get9png(
                    "gy_image_editview", context));
            mProgressDialog.setContentView(contentView, rlp);
        }
    }

    TextView tv;

    public void stopAnim(View view) {
        AnimationDrawable anim = (AnimationDrawable) view.getBackground();
        if (anim.isRunning()) { // 如果正在运行,就停止
            anim.stop();
        }
    }

    public void setMessage(String msg) {
        mMessage = msg;
        if (tv != null) {
            tv.setText(mMessage);
        }
    }

    public void startAnim(View view) {
        // 完全编码实现的动画效果
        AnimationDrawable anim = new AnimationDrawable();
        for (int i = 1; i <= 8; i++) {
            // 根据资源名称和目录获取R.java中对应的资源ID
            Drawable drawable = BackGroudSeletor.getdrawble("0" + i, context);
            // 将此帧添加到AnimationDrawable中
            anim.addFrame(drawable, 100);
        }
        anim.setOneShot(false); // 设置为loop
        view.setBackgroundDrawable(anim); // 将动画设置为ImageView背景
        anim.start(); // 开始动画
    }

    private void initProgressDialog(String msg) {
        if (null == this.mProgressDialog) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            this.mProgressDialog = progressDialog;
        }
    }

    @Override
    public void onSuccess(Object result) {
        dimiss(mDismissTime);
    }

    @Override
    public void onLoading(Object result) {}

    @Override
    public void onCancel(Object result) {
        dimiss(mDismissTime);
    }

    @Override
    public void onTimeout(Object result) {
        dimiss(mDismissTime);
    }

    @Override
    public void onCancelClick() {
    }
}
