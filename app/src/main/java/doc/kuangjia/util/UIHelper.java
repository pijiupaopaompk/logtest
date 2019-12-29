package com.zkys.pad.launcher.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.zkys.pad.launcher.MyApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/12.
 */

public class UIHelper {
    private static Toast mToast;
    private static Dialog mProgressDialog;
    public static int getScreenWidth(){
        WindowManager wm = (WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getScreenHeight(){
        WindowManager wm = (WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }


    public static int  getVersionCode(Context context){
            PackageManager manager = context.getPackageManager();
            int code = 0;
            try {
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                code = info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return code;
    }

    public static String  getVersionName(Context context){
            PackageManager manager = context.getPackageManager();
            String name = "";
            try {
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                name = info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return name;
    }

    public static final void showResultDialog(Context context, String msg,
                                              String title) {
        if(msg == null) return;
        String rmsg = msg.replace(",", "\n");
        Log.d("Util", rmsg);
        new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
                .setNegativeButton("知道了", null).create().show();
    }

    public static final void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static final void toastMessage(final Activity activity,
                                          final String message) {
        toastMessage(activity, message, null);
    }

    public static final void toastMessage(final Activity activity,
                                          final String message, String logLevel) {
        if ("w".equals(logLevel)) {
            Log.w("sdkDemo", message);
        } else if ("e".equals(logLevel)) {
            Log.e("sdkDemo", message);
        } else {
            Log.d("sdkDemo", message);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
                mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    public static Map<String, String> getDataByUrl(String url) {
        try {
            String[] str = url.split("\\?");
            if (str == null || str.length < 2)
                return null;
            str = str[1].split("\\&");
            if (str == null) {
                return null;
            }
            String temp[] = null;
            Map<String, String> data = new HashMap<>();
            for (String s : str) {
                temp = s.split("=");
                if (temp != null && temp.length > 1) {
                    data.put(temp[0], temp[1]);
                }
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //获取状态栏高度
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        LogFactory.l().e("statusBarHeight:"+frame.top+"px");
        return frame.top;
    }


    //不限定参数拼接字符串
    public static String fun(String... msgs){
        String str="";
        for (int i = 0; i < msgs.length; i++) {
            str+=msgs[i]+",";
        }
        return str.substring(0,str.length()-1);
    }

    //加逗号分隔
    public static String listToString(List<String> list){

        if(list==null){
            return null;
        }

        StringBuilder result = new StringBuilder();
        boolean first = true;

        //第一个前面不拼接","
        for(String string :list) {
            if(first) {
                first=false;
            }else{
                result.append(",");
            }
            result.append(string);
        }
        return result.toString();
    }
}
