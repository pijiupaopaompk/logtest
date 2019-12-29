package com.zkys.pad.launcher.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;

import com.zkys.pad.launcher.util.UIUtils;


/**
 * Created by zy2 on 2016/5/30.
 * <p/>
 * 安全退出的 dialog，判断依赖的 Act 是否还存在，在决定是否 dismiss 或者 show
 */
public class BaseDialog extends Dialog {

    private Context mActivity;

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        mActivity = context;
    }

    @Override
    public void dismiss() {
        if (!UIUtils.isActExist(mActivity)) {
            return;
        }

        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        if (!UIUtils.isActExist(mActivity)) {
            return;
        }
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置顶部的值
    protected void setTopPadding(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setVisibility(View.VISIBLE);
        }
    }
}
