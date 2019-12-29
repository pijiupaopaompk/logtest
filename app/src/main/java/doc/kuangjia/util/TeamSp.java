package com.zkys.pad.launcher.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.zkys.pad.launcher.MyApplication;
import com.zkys.pad.launcher.constant.Constants;

/**
 * Created by anyrsan on 2017/12/26.
 * SharedPreferences 数据存储工具类
 */
public class TeamSp {
//    private static String teamspName = Constants.USER_SHAREDPREFERENCES;

    public static void putInt(String key, int value) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(Constants.USER_SHAREDPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }


    public static void putString(String key, String value) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(Constants.USER_SHAREDPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }


    public static int getInt(String key, int defValue) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(Constants.USER_SHAREDPREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static String getString(String key, String defValue) {

        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(Constants.USER_SHAREDPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }
}
