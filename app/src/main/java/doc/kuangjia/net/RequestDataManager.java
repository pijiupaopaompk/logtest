package com.zkys.pad.launcher.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.zkys.pad.launcher.MyApplication;
import com.zkys.pad.launcher.Rxbus.RxBus;
import com.zkys.pad.launcher.Rxbus.bean.RxLogoutEvent;
import com.zkys.pad.launcher.net.cache.CacheLoader;
import com.zkys.pad.launcher.net.cache.NetworkCache;
import com.zkys.pad.launcher.ui.login.activity.LoginActivity;
import com.zkys.pad.launcher.ui.login.constants.LoginConstant;
import com.zkys.pad.launcher.util.LogFactory;
import com.zkys.pad.launcher.util.SPUtil;
import com.zkys.pad.launcher.util.StartActivityUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anyrsan on 2017/12/15.
 */

public class RequestDataManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Disposable dp = null;

    // 统一处理数据
    public int pageSize = 10;


    public Map<String, Object> getParamMap(int pageNo) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("pageNo", pageNo);
        objectMap.put("pageSize", pageSize);
        return objectMap;
    }


    /**
     * 网络加载，并且生成缓存数据 控制数据流
     *
     * @param context
     * @param key
     * @param t
     * @param data
     */
    public <T> void doTask(Context context, String key, Class<T> t, final Observable<T> data, LifecycleProvider lifecycleProvider, final ICallBack iCallBack) {
        NetworkCache<T> networkCache = new NetworkCache<T>() {
            @Override
            public Observable<T> get(String key, Class<T> cls) {
                return data;
            }
        };
        Observable<T> observable = CacheLoader.getInstance(context).net(key, t, networkCache);
        observable
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                iCallBack.start(d);
            }

            @Override
            public void onNext(T newsBean) {
                iCallBack.success(newsBean);
            }

            @Override
            public void onError(Throwable e) {
                ExceptionHandle.ResponeThrowable dataException = ExceptionHandle.handleException(e);
                iCallBack.error(dataException);
            }

            @Override
            public void onComplete() {
                iCallBack.complete();
            }
        });
    }


    /**
     * 控制数据流
     *
     * @param context
     * @param key
     * @param t
     * @param data
     * @param isDispose true的话，本地有数据就会中断网络，false是不管本地有没有数据都会调用网络
     */
    public <T> void doTask(Context context, String key, Class<T> t, final boolean isDispose, final Observable<T> data, LifecycleProvider lifecycleProvider, final ICallBack iCallBack) {
        NetworkCache<T> networkCache = new NetworkCache<T>() {
            @Override
            public Observable<T> get(String key, Class<T> cls) {
                return data;
            }
        };
        Observable<T> observable = CacheLoader.getInstance(context).asDataObservable(key, t, networkCache);
        observable
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<T>() {
            @Override
            public void accept(T newsBean) throws Exception {
                if (isDispose && dp != null && !dp.isDisposed()) {
                    dp.dispose();
                }
            }
        }).subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                dp = d;
                iCallBack.start(d);
            }

            @Override
            public void onNext(T newsBean) {
                iCallBack.success(newsBean);
            }

            @Override
            public void onError(Throwable e) {
                ExceptionHandle.ResponeThrowable dataException = ExceptionHandle.handleException(e);
                iCallBack.error(dataException);
            }

            @Override
            public void onComplete() {
                iCallBack.complete();
            }
        });
    }


    /***
     *  直接加载网络，并无缓存处理
     * @param data
     * @param lifecycleProvider
     * @param iCallBack
     */
    public <T> void doTask(final Observable<T> data, LifecycleProvider lifecycleProvider, final ICallBack iCallBack) {
        Observable<T> observable = data;
        observable
                .compose(lifecycleProvider.bindToLifecycle()) // 注意代码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        iCallBack.start(d);
                    }

                    @Override
                    public void onNext(T newsBean) {
                            try{
                                String str = JSONObject.toJSONString(newsBean);
                                JSONObject jsonObject = JSON.parseObject(str);
                                LogFactory.l().e("jsonObject===");
                                if(jsonObject.getIntValue("code") == 1001){  //token过期
                                    SPUtil.saveData(LoginConstant.isLogin, false);
                                    RxBus.getInstance().post(new RxLogoutEvent());
//                                    SPUtil.clear(MyApplication.getInstance());
                                    Toast.makeText(MyApplication.getInstance(),"登录状态失效,请重新登录",Toast.LENGTH_SHORT).show();
                                    StartActivityUtil.startActivity(MyApplication.getInstance(),LoginActivity.class);
                                    return;
                                }
                            }catch (Exception e){
                                LogFactory.l().e("e==="+e.getMessage());
                                e.printStackTrace();
                            }

                        iCallBack.success(newsBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionHandle.ResponeThrowable dataException = ExceptionHandle.handleException(e);
                        iCallBack.error(dataException);
                    }

                    @Override
                    public void onComplete() {
                        iCallBack.complete();
                    }
                });
    }

    /***
     *  直接加载网络，并无缓存处理
     * @param data
     * @param iCallBack
     */
    public <T> void doTask(final Observable<T> data, final ICallBack iCallBack) {
        Observable<T> observable = data;
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        iCallBack.start(d);
                    }

                    @Override
                    public void onNext(T newsBean) {
                        iCallBack.success(newsBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionHandle.ResponeThrowable dataException = ExceptionHandle.handleException(e);
                        iCallBack.error(dataException);
                    }

                    @Override
                    public void onComplete() {
                        iCallBack.complete();
                    }
                });
    }


    /***
     *  直接加载网络，可以在子线中处理一些其它事情操作
     * @param data
     * @param lifecycleProvider
     * @param iCallBack
     */
    public <T> void doTask(final Observable<T> data, LifecycleProvider lifecycleProvider, final ICallBack iCallBack, final ICallBackDoNext<T> iCallBackDoNext) {
        Observable<T> observable = data;
        observable
                .compose(lifecycleProvider.bindToLifecycle()) // 注意代码
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(T o) throws Exception {
                        iCallBackDoNext.handleData(o);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        iCallBack.start(d);
                    }

                    @Override
                    public void onNext(T newsBean) {
                        iCallBack.success(newsBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionHandle.ResponeThrowable dataException = ExceptionHandle.handleException(e);
                        iCallBack.error(dataException);
                    }

                    @Override
                    public void onComplete() {
                        iCallBack.complete();
                    }
                });
    }
    /***
     *  直接加载网络，可以在子线中处理一些其它事情操作
     * @param data
     * @param iCallBack
     */
    public <T> void doTask(final Observable<T> data, final ICallBack iCallBack, final ICallBackDoNext<T> iCallBackDoNext) {
        Observable<T> observable = data;
        observable
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(T o) throws Exception {
                        iCallBackDoNext.handleData(o);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        iCallBack.start(d);
                    }

                    @Override
                    public void onNext(T newsBean) {
                        iCallBack.success(newsBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionHandle.ResponeThrowable dataException = ExceptionHandle.handleException(e);
                        iCallBack.error(dataException);
                    }

                    @Override
                    public void onComplete() {
                        iCallBack.complete();
                    }
                });
    }


}
