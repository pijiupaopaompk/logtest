package com.zkys.pad.launcher.net;


/**
 * Created by anyrsan on 2017/12/15.
 */

public interface ICallBackDoNext<T> {
    void handleData(T t);
}
