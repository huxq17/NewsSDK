package com.aiyou.toolkit.common;

import com.aiyou.toolkit.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtil {

    public static int getInt(JSONObject json, String key) {
        int result = 0;
        try {
            if (json.has(key)) {
                result = json.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean getBoolean(JSONObject json, String key) {
        boolean result = false;
        try {
            if (json.has(key)) {
                result = json.getBoolean(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getString(JSONObject json, String key) {
        String result = null;
        try {
            if (json.has(key)) {
                result = json.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getString(JSONObject json, String key, String defaultValue) {
        String result = defaultValue;
        try {
            if (json.has(key)) {
                result = json.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Gson gson = new Gson();

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> beanClass) {
        T result = null;
        try {
            result = gson.fromJson(json, beanClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
