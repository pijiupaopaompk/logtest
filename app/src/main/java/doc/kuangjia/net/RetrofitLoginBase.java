package com.zkys.pad.launcher.net;


import android.text.TextUtils;
import android.util.Log;

import com.zkys.pad.launcher.constant.Constants;
import com.zkys.pad.launcher.util.LogFactory;
import com.zkys.pad.launcher.util.SPUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * @author any
 * @date 2017/11/30
 */
public abstract class RetrofitLoginBase {

    private static final String TAG = "RetrofitLoginBase";
    /**
     * 连接超时时长x秒
     */
    public static final int CONNECT_TIME_OUT = 10;
    /**
     * 读数据超时时长x秒
     */
    public static final int READ_TIME_OUT = 10;
    /**
     * 写数据接超时时长x秒
     */
    public static final int WRITE_TIME_OUT = 10;

    private Retrofit mRetrofit;


    public RetrofitLoginBase(HashMap<String,Object> map) {
        String userKey = SPUtil.getString(Constants.USER_KEY, "");
        String sign=Signature.getSign2(map,userKey);
//        LogFactory.l().i("userKey==="+userKey+"===sign==="+sign);
        mRetrofit = initRetrofit(map,sign);
    }
    /**
     * 配置Okhttp
     * @return
     */
    public OkHttpClient configOkHttpClient(final HashMap<String,Object> map,final String sign) {
        //日志拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("zkysphone", message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //添加参数 Header
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                Request request = null;
                builder.addHeader("Content-Type", "application/json; charset=UTF-8")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "*/*")
                        .addHeader("Cookie", "add cookies here")
                        .addHeader("TES", "1");
                if(!TextUtils.isEmpty(getToken())){
                    builder = builder.addHeader("WORKER", getToken());
                }
                if(!sign.equals("")){
                    builder = builder.addHeader("SIGN", sign);
                    LogFactory.l().i("sign==="+sign);
                }
                request = builder.build();
                return chain.proceed(request);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addInterceptor(headerInterceptor)
//                .retryOnConnectionFailure(true)    //添加重联
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        return builder.build();
    }


    public abstract String getBaseUrl();

    public abstract String getToken();




    /**
     * 获取Retrofit对象
     *
     * @return
     */
    public Retrofit initRetrofit(HashMap<String,Object> map,String sign) {
        Retrofit mRetrofit= new Retrofit.Builder()
                    .client(configOkHttpClient(map,sign))
                    .baseUrl(getBaseUrl())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .build();
        return mRetrofit;
    }


    /**
     * 创建ServiceApi
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service_agent is null!");
        }
        return mRetrofit.create(service);
    }


}
