package com.zkys.pad.launcher.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

import com.zkys.pad.launcher.MyApplication;

import java.math.BigDecimal;

/**
 * Created by anyrsan on 2017/12/19.
 */

public class UIUtils {
    public static Context getContext() {
        return MyApplication.getInstance().getApplicationContext();
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    /**
     * 判断Act是否存在
     *
     * @param context
     * @return
     */
    public static boolean isActExist(Context context) {
        if (context == null) {
            return false;
        } else {
            if (context instanceof FragmentActivity) {
                return assertNotDestroyed((FragmentActivity) context);
            } else if (context instanceof Activity) {
                return assertNotDestroyed((Activity) context);
                // 以下2个的判断顺序，不能调换，由于 Application 是 ContextWrapper 子类，则优先判断 Application，直接返回 true
            } else if (context instanceof Application) {
                return true;
            } else if (context instanceof ContextWrapper) {
                return isActExist(((ContextWrapper) context).getBaseContext());
            }
        }

        return false;
    }

    private static boolean assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                return true;
            }
        } else {
            if (activity != null && !activity.isFinishing()) {
                return true;
            }
        }
        return false;
    }



    public static String  getAppCache(Context context) {
        long size = 0;
//		java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.00");
        try {
            size = DataCleanManager.getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                size += DataCleanManager.getFolderSize(context.getExternalCacheDir());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		String str = myformat.format(size)+"MB";
        return getFormatSize(size);
    }


    public  static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
