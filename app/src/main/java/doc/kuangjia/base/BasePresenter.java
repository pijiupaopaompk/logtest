package com.zkys.pad.launcher.base;

/**
 * Created by anyrsan on 2017/12/12.
 */
public interface BasePresenter<T extends IBaseView> {

    void onAttach();

    void onDetach();

    T getView();
}
