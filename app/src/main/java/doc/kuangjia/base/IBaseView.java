package com.zkys.pad.launcher.base;

import android.app.Activity;

import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * Created by anyrsan on 2017/12/12.
 */

public interface IBaseView {

    void showLoading();

    void closeLoading();

    void showToast(String msg);

    Activity getContextActivity();

    LifecycleProvider getLifecycleProvider();
}
