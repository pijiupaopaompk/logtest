package com.zkys.pad.launcher;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.zkys.pad.launcher.util.LogFactory;

import java.util.List;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by mpk on 2018-08-23.
 * MyApplication
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private static Stack<Activity> activityStack;

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;    //4e100356357f4532bdab093ae601324f

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        LogFactory.l().i("getRegistrationID:"+JPushInterface.getRegistrationID(instance));
    }




    /**
     * 多进程情况下，得到当前进程的包名
     * @param context
     * @return
     */
    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除指定activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if(activityStack != null && activity!=null){
            activityStack.remove(activity);
        }
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if(activityStack != null){
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if(activityStack != null && activity!=null){
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if(activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * @return true 进程存在，否在不存在
     */
    public boolean isCheckMainActivity() {
        return isActivityAlive(getApplicationContext(), "com.union.replytax.MainActivity");
    }

    /**
     * 检查指定activity是否存在
     *
     * @param context
     * @param activityName
     * @return
     */
    private boolean isActivityAlive(Context context, String activityName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            // 注意这里的 topActivity 包含 packageName和className，可以打印出来看看
            if (info.topActivity.getClassName().equals(activityName) || info.baseActivity.getClassName().equals(activityName)) {
//                LogUtil.d(TAG, info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
                return true;
            }
        }
        return false;
    }


}
