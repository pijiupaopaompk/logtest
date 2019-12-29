package com.zkys.pad.launcher.net.cache;

import io.reactivex.Observable;

/**
 * @author any
 * @date 2017/11/30
 */
public interface ICache {
    <T> Observable<T> get(String key, Class<T> cls);

    <T> void put(String key, T t);

    void clearCache(String key);
}
