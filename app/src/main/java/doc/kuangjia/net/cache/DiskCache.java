package com.zkys.pad.launcher.net.cache;


import com.alibaba.fastjson.JSONObject;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author any
 * @date 2017/11/30
 */
public class DiskCache implements ICache {

    private static final String NAME = ".sk";
    /**
     * 更改存储路径
     */
    private File fileDir;

    public DiskCache() {
        fileDir = CacheLoader.getApplication().getCacheDir();
    }

    @Override
    public <T> Observable<T> get(final String key, final Class<T> cls) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                T t = (T) getDataByFilePath(getKey(key), cls);
                if (t != null) {
                    e.onNext(t);
                }
//                Log.e("msg", "DiskCache..." + t);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 存放本地
     *
     * @param key
     * @param t
     * @param <T>
     */
    @Override
    public <T> void put(final String key, final T t) {
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                boolean isSuccess = isSave(getKey(key), t);
                if (isSuccess) {
                    e.onNext(t);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    public <T> boolean isSave(String filePath, T t) {
        String result = JSONObject.toJSONString(t);
        int status = FileUtils.writeResultToFile(filePath, result);
        return status == 1;
    }


    public <T> Object getDataByFilePath(String filePath, final Class<T> cls) {
        String result = FileUtils.getResultByFilePath(filePath);
        T t = JSONObject.parseObject(result, cls);
        return t;
    }

    //
//    /**
//     * 判断缓存是否已经失效
//     */
//    private boolean isCacheDataFailure(File dataFile) {
//        if (!dataFile.exists()) {
//            return false;
//        }
//        long existTime = System.currentTimeMillis() - dataFile.lastModified();
//        boolean failure = false;
////        if (NetWorkUtls.getNetworkType(CacheLoader.getApplication()) == NetWorkUtls.NETTYPE_WIFI) {
////            failure = existTime > WIFI_CACHE_TIME ? true : false;
////        } else {
////            failure = existTime > OTHER_CACHE_TIME ? true : false;
////        }
//        return failure;
//    }
//
    @Override
    public void clearCache(String key) {
        File file = new File(fileDir, getKey(key));
        if (file.exists()) file.delete();
    }


    private String getKey(String key) {
        return fileDir.getAbsolutePath() + File.separator + FileUtils.getMD5(key) + NAME;
    }





}