package com.soft.logtest;

import android.app.Application;
import android.os.Environment;

import java.io.IOException;

public class LogApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LogcatHelper.getInstance(this).start();

    }
}
