package com.aiqing.newssdk;

import android.content.Context;
import android.content.Intent;

public class SDKHelper {
    public static void open(Context context) {
        CustomApplication.init(context);
        Intent intent = new Intent(context, MainActivity.class);
        startSDKActivity(context, intent);
    }

    private static void startSDKActivity(Context context, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
