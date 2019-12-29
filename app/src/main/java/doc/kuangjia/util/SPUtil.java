package com.zkys.pad.launcher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zkys.pad.launcher.MyApplication;

/**
 * Created by anyrsan on 2017/12/26.
 * SharedPreferences 数据存储工具类
 */
public class SPUtil {

    private static String PreferenceName = "ehuts";

    private static String TK = "token";

    // 通过类名字去获取一个对象
    public static <T> T getObject(Class<T> clazz) {
        String key = getKey(clazz);
        String json = getString(key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getKey(Class<?> clazz) {
        return clazz.getName();
    }


    public static String getString(String key, String defValue) {
        LogFactory.l().i("PreferenceName==="+PreferenceName);
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void putObject(Object object) {
        String key = getKey(object.getClass());
        Gson gson = new Gson();
        String json = gson.toJson(object);
        putString(key, json);
    }

    public static void putString(String key, String value) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void putInt(String key, int value) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    /**
     * 存储token
     * @param token
     */
    public static void saveToken(String token) {
        SharedPreferences sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        // 存储
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(TK, token);
        editor.commit();
    }

    /***
     * 获取token
     * @return
     */
    public static String getToken() {
        SharedPreferences sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getString(TK, null);
    }


    public static void clearToken() {
        SharedPreferences sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        // 存储
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(TK, "");
        editor.commit();
    }


    //清除SharedPreferences
    public static void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        editor.commit();
    }


    public static void saveData(String key, boolean value) {
        SharedPreferences sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        // 存储
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static boolean getBooleanData(String key) {
        SharedPreferences sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
}
