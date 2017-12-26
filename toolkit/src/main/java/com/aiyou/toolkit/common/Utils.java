/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @Copyright (C) 2012 Menue Information Technology Co., Ltd.
 */

package com.aiyou.toolkit.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class containing some static utility methods.
 */
public class Utils {
    private Utils() {
    }

    ;

    @TargetApi(11)
   /* public static void enableStrictMode() {
        if (GUtils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (GUtils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder
                        .setClassInstanceLimit(ImageGridActivity.class, 1)
                        .setClassInstanceLimit(ImageDetailActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }*/

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getDpi(context);
        int contentHeight = getScreenHeight(context);
        return totalHeight - contentHeight;
    }

    /**
     * 标题栏高度
     *
     * @return
     */
    public static int getTitleHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static boolean isMz() {
        return getManufacturer().contains("meizu");
    }

    public static boolean isOnePlus() {
        return getManufacturer().contains("oneplus");
    }

    private static String getManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        manufacturer = manufacturer.toLowerCase();
        LogUtils.d("手机型号是=" + manufacturer);
        return manufacturer;
    }

    public static void setNoFullScreen(EditText et) {
        et.setImeOptions(EditorInfo.IME_FLAG_NO_FULLSCREEN);
    }

    public static void limitSpaceInput(EditText et, int length,
                                       final Context context) {
        setNoFullScreen(et);
        final Context applicationContext = context.getApplicationContext();
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // 返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
                if (source.equals(" ") || verifyChinese(source.toString())) {
                    Base.getInstance(applicationContext).toast("不能为空格或者中文");
                    return "";
                } else
                    return null;
            }
        };
        if (length > 0) {
            et.setFilters(new InputFilter[]{filter,
                    new InputFilter.LengthFilter(length)});
        } else {
            et.setFilters(new InputFilter[]{filter});
        }
    }

    /**
     * 验证密码是否包含中文
     *
     * @param string
     * @return
     */
    public static boolean verifyChinese(String string) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA5]+$");
        Matcher m = p.matcher(string);
        System.out.print(m.matches() + "---");
        return m.matches();
    }

    public static boolean isLandscape(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            return true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            return false;
        }
        return false;
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) closeable.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteFileSafely(File file) {
        if (file != null) {
            if (file.exists()) {
                String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
                File tmp = new File(tmpPath);
                file.renameTo(tmp);
                return tmp.delete();
            }
        }
        return false;
    }

    public static Drawable setStokenBg(int strokenWidth, int round, int strokenColor, int background) {
        GradientDrawable gd = new GradientDrawable();//创建drawable
        if (background != 0) {
            gd.setColor(background);
        }
        if (round > 0) {
            gd.setCornerRadius(round);
        }
        if (strokenWidth > 0) {
            gd.setStroke(strokenWidth, strokenColor);
        }
        return gd;
    }

    public static String sortHashMap(LinkedHashMap hashMap) {
        Object[] key = hashMap.keySet().toArray();
        Arrays.sort(key);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < key.length; i++) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key[i]).append("=").append(hashMap.get(key[i]));
        }
        return sb.toString();
    }
}
