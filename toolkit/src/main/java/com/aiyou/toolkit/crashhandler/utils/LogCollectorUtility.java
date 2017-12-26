//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.aiyou.toolkit.crashhandler.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.aiyou.toolkit.common.Device;
import com.aiyou.toolkit.common.LogUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogCollectorUtility {
    private static final String TAG = LogCollectorUtility.class.getName();

    public LogCollectorUtility() {
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                if (mNetworkInfo.isAvailable() && mNetworkInfo.isConnected()) {
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(1);
            if (mWiFiNetworkInfo != null) {
                if (mWiFiNetworkInfo.isAvailable() && mWiFiNetworkInfo.isConnected()) {
                    return true;
                }
                return false;
            }
        }

        return false;
    }

    public static File getExternalDir(Context context, String dirName) {
        String cacheDir = "/Android/data/" + context.getPackageName() + "/";
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + cacheDir + dirName + "/");
    }

    public static boolean isSDcardExsit() {
        String state = Environment.getExternalStorageState();
        return "mounted".equals(state);
    }

    public static boolean hasPermission(Context context) {
        if (context != null) {
            boolean b1 = context.checkCallingOrSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED;
            boolean b2 = context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED;
            boolean b3 = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED;
            boolean b4 = context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == PackageManager.PERMISSION_GRANTED;
            boolean b5 = context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == PackageManager.PERMISSION_GRANTED;
            if (!b1 || !b2 || !b3 || !b4 || !b5) {
                LogUtils.d("没添加上传崩溃的权限");
            }
            return b1 && b2 && b3 && b4 && b5;
        } else {
            return false;
        }
    }

    public static String getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(date);
        return time;
    }

    public static String getVerName(Context c) {
        PackageManager pm = c.getPackageManager();
        PackageInfo pi = null;

        try {
            pi = pm.getPackageInfo(c.getPackageName(), 1);
        } catch (NameNotFoundException var4) {
            Log.e(TAG, "Error while collect package info", var4);
            var4.printStackTrace();
            return "error";
        }

        if (pi == null) {
            return "error1";
        } else {
            String versionName = pi.versionName;
            return versionName == null ? "not set" : versionName;
        }
    }

    public static String getVerCode(Context c) {
        PackageManager pm = c.getPackageManager();
        PackageInfo pi = null;

        try {
            pi = pm.getPackageInfo(c.getPackageName(), 1);
        } catch (NameNotFoundException var4) {
            Log.e(TAG, "Error while collect package info", var4);
            var4.printStackTrace();
            return "error";
        }

        if (pi == null) {
            return "error1";
        } else {
            int versionCode = pi.versionCode;
            return String.valueOf(versionCode);
        }
    }

    public static String getMid(Context context) {
//        TelephonyManager tm = (TelephonyManager)context.getSystemService("phone");
//        String imei = tm.getDeviceId();
//        String AndroidID = android.provider.Settings.System.getString(context.getContentResolver(), "android_id");
//        String serialNo = getDeviceSerialForMid2();
        String m2 = Device.getDeviceId(context);
        return m2;
    }

    private static String getDeviceSerialForMid2() {
        String serial = "";

        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", new Class[]{String.class});
            serial = (String) get.invoke(c, new Object[]{"ro.serialno"});
        } catch (Exception var3) {
        }

        return serial;
    }

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
            return "";
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; ++i) {
            if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }
}
