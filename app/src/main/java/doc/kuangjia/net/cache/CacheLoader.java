package com.zkys.pad.launcher.net.cache;

import android.app.Application;
import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author any
 * @date 2017/11/30
 */
public class CacheLoader {

    private static Application application;

    private static CacheLoader cacheLoader;

    private ICache diskCache;


    private CacheLoader() {
        diskCache = new DiskCache();
    }


    public static CacheLoader getInstance(Context context) {
        application = (Application) context.getApplicationContext();
        if (cacheLoader == null) {
            synchronized (CacheLoader.class) {
                if (cacheLoader == null) {
                    cacheLoader = new CacheLoader();
                }
            }
        }
        return cacheLoader;
    }

    public static Application getApplication() {
        return application;
    }

    public <T> Observable<T> asDataObservable(String key, Class<T> cls, NetworkCache networkCache) {
        return Observable.concat(disk(key, cls), net(key, cls, networkCache));
    }


    public <T> Observable<T> disk(String key, Class<T> cls) {
        return diskCache.get(key, cls).doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
//                Log.e("msg", "我是磁盘缓存");
                // 放入内存
            }
        });
    }

    public <T> Observable<T> net(final String key, Class<T> cls, NetworkCache<T> networkCache) {
        return networkCache.get(key, cls).doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                if (t != null) {
                    diskCache.put(key, t);
//                    Log.e("msg", "我是网络缓存");
                }
            }
        });
    }


    public void clearMemoryDisk(String key) {
        diskCache.clearCache(key);
    }
}



