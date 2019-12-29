package com.zkys.pad.launcher.Rxbus;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by anyrsan on 2017/12/
 * 基于RxJava实现的数据传递类
 */

public class RxBus {

    private static volatile RxBus mRxBus;

    private FlowableProcessor flowable;

    private Map<Object, Disposable> disposableMap;

    private RxBus() {
        disposableMap = new HashMap<>();
        initFlowable();
    }

    private void initFlowable() {
        flowable = PublishProcessor.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }


    /**
     * @param key
     * @param clz
     * @param consumer
     * @param <T>
     */
    public <T> void subscribe(Object key, Class<T> clz, RxConsumer<T> consumer) {
        if (flowable.hasComplete()) {  //如果发现状态为完成，则new新对象
            initFlowable();
        }
        Disposable disposable = flowable.ofType(clz).subscribe(consumer);
        Log.e("msg", "--->" + disposable);
        disposableMap.put(key, disposable);
    }


    /**
     * 注销
     * 移除目标
     *
     * @param key
     */
    public void unRegister(Object key) {
        Disposable disposable = disposableMap.get(key);
        if (disposable != null && !disposable.isDisposed()) {  //中断不发送给他了
            disposable.dispose();
        }
        if (disposable != null) {
            disposableMap.remove(key);
        }
    }
    /**
     * 发送数据
     *
     * @param event
     */
    public void post(Object event) {
        flowable.onNext(event);
    }

    /**
     * 取消注册
     */
    public void unregisterAll() {
        for (Disposable disposable : disposableMap.values()) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposableMap.clear();
    }

    //完成处理
    public void onDestroy() {
        unregisterAll();
        flowable.onComplete();
    }
}
