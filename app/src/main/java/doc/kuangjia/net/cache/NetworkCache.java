package com.zkys.pad.launcher.net.cache;


import io.reactivex.Observable;

/**
 * @author any
 * @date 2017/11/30
 */
public abstract class NetworkCache<T> {
    public abstract Observable<T> get(String key, final Class<T> cls);
}