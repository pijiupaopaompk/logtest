package com.zkys.pad.launcher.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zkys.pad.launcher.MyApplication;
import com.zkys.pad.launcher.R;
import com.zkys.pad.launcher.dialog.DialogLoading;
import com.zkys.pad.launcher.util.StatusBarUtils;
import com.zkys.pad.launcher.util.ToastUtil;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhangqi on 2017/2/21.
 * 如果有BasePresenter 请直接继承 BasePresenterFragment
 */
public abstract class BaseFragment extends RxFragment implements IBaseView {


    protected Unbinder unbinder;
    /**
     * 申明activity对象
     */
    public View rootView;


    private DialogLoading dialogLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public LifecycleProvider getLifecycleProvider() {
        return this;
    }

    @Override
    public void showLoading() {
        dialogLoading.show();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }

    @Override
    public void closeLoading() {
        dialogLoading.dismiss();
    }

    @Override
    public Activity getContextActivity() {
        return getActivity();
    }

    public abstract BasePresenter getBasePresenter();

    public abstract void initBaserPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        try {
            rootView = inflater.inflate(getResourceId(), container, false);
            unbinder = ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            e.printStackTrace();
            //
            getActivity().finish();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            //
            MyApplication.getInstance().finishActivity(getActivity());
        }
        if (rootView == null) {
            MyApplication.getInstance().finishActivity(getActivity());
        }
        onBack();
        initView();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBaserPresenter();
        if (getBasePresenter() != null) {
            getBasePresenter().onAttach();
        }
    }

    //绑定返回事件
    public void onBack() {
        if (rootView == null) return;
        View view = rootView.findViewById(R.id.common_top_back_iv);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.getInstance().finishActivity(getActivity());
                }
            });
        }
    }

    public void setTitle(String title) {
        TextView view = (TextView) rootView.findViewById(R.id.common_top_title_tv);
        if (view != null) {
            view.setText(title);
        }
    }

    //设置顶部的值
    protected void setTopPadding(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dialogLoading = new DialogLoading(getContext());
        initData();
    }

    /**
     * 资源布局
     *
     * @return
     */
    public abstract int getResourceId();

    /**
     * 初始化数据
     */
    public abstract void initData();


    /**
     * 初始化view的状态等信息
     */
    public abstract void initView();


    /**
     * http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (getBasePresenter() != null) {
            getBasePresenter().onDetach();
        }
    }





    public Handler handler = new Handler();

    //加载数据
    protected void reLoadData(Runnable runnable) {
        handler.postDelayed(runnable, 300);
    }

    protected void reLoadData(Runnable runnable,long delayTime) {
        handler.postDelayed(runnable, delayTime);
    }

}
