package com.zkys.pad.launcher.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zkys.pad.launcher.MyApplication;
import com.zkys.pad.launcher.R;
import com.zkys.pad.launcher.dialog.DialogLoading;
import com.zkys.pad.launcher.ui.MainActivity;
import com.zkys.pad.launcher.util.StatusBarUtils;
import com.zkys.pad.launcher.util.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Created by 52620 on 2017/10/17.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements IBaseView{
    private Toolbar toolbar;

    public abstract BasePresenter getBasePresenter();
    public abstract void initBasePresenter();
    protected DialogLoading dialogLoading;
    protected Context context;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getBaseContext();
        addActivity();
        setStatusBarTransparent(true);
        setContentView(getLayoutId());
        dialogLoading = new DialogLoading(this);
        ButterKnife.bind(this);
        initBasePresenter();
        if (getBasePresenter() != null) {
            getBasePresenter().onAttach();
        }
        setToolbar();
        initView();
        initData();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
//            toolbarTitle.setText(R.string.register);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    protected void setToolbarText(String title){
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);
    }

    protected void setToolbarRightText(String title){
        TextView tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setText(title);
        tvRight.setVisibility(View.VISIBLE);
    }

    protected void setToolbarRightText(String title,int colorId){
        TextView tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setText(title);
        tvRight.setTextColor(colorId);
        tvRight.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化数据
     */
    public abstract void initData();
    /**
     * 初始化view的状态等信息
     */
    public abstract void initView();
    /**
     * 资源布局
     * @return
     */
    public abstract int getLayoutId();

    //添加
    private void addActivity() {
        if (this instanceof MainActivity) {
            return;
        }
        MyApplication.getInstance().addActivity(this);
    }

    public void addFragment(Fragment fragment, @IdRes int rId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(rId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    public void replaceFragment(Fragment fragment, @IdRes int rId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(rId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getBasePresenter() != null) {
            getBasePresenter().onDetach();
        }
    }

    //本身会关闭，但要移除activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.getInstance().removeActivity(this);
    }


    //设置顶部的值
    protected void setTopPadding(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            view.setPadding(0, StatusBarUtils.getStatusBarHeight(this), 0, 0);
        }
    }

    protected void setStatusBar(boolean isDark, int color) {
        StatusBarUtils.setStatusBar(this, isDark, color);
    }

    protected void setStatusBarTransparent(boolean isDark) {
        setStatusBar(isDark, Color.TRANSPARENT);
    }

    public void setDialogStyle(Dialog dialog) {
        Window window_date = dialog.getWindow();
        window_date.setGravity(Gravity.BOTTOM);
        window_date.setWindowAnimations(R.style.PickerDialog);
        dialog.show();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void showLoading() {
        dialogLoading.show();
    }

    @Override
    public void closeLoading() {
        dialogLoading.dismiss();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(this, msg);
    }

    @Override
    public Activity getContextActivity() {
        return this;
    }

    @Override
    public LifecycleProvider getLifecycleProvider() {
        return this;
    }


    /***
     * 隐藏键盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        if (imm != null) {
            View view = getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }



    /***
     * 显示键盘
     *
     * @param et
     */
    public void showSoftInput(final EditText et) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService
                        (INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et, 0);
            }

        }, 998);
    }

    //判断键盘是否打开
    public boolean isSoftInput(){
        InputMethodManager imm =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    //关闭键盘
    public void hintKbSoft() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
