package com.zkys.pad.launcher.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by anyrsan on 2017/12/12.
 */

public class StartActivityUtil {


    public static void startActivity(Activity activity, Class cls, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, code);
    }

    public static void startActivity(Activity activity, Class cls, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, code);
    }

    public static void startActivity(Activity activity, Class cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    public static void startActivity(Context activity, Class cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }


    public static void startActivity(Context activity, Class cls, Bundle bundle) {
        Intent intent = new Intent(activity, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    public static void startActivity(Activity activity, Class cls, Bundle bundle) {
        Intent intent = new Intent(activity, cls);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

}
