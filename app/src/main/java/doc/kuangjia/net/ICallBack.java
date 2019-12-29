package com.zkys.pad.launcher.net;

import io.reactivex.disposables.Disposable;

/**
 * Created by anyrsan on 2017/12/15.
 */

public interface ICallBack<T> {

    void start(Disposable d);

    void success(T t);

    void error(ExceptionHandle.ResponeThrowable dataEx);

    void complete();
}
