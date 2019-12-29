package com.zkys.pad.launcher.base;


import com.zkys.pad.launcher.net.RetrofitBase;

/**
 * @author any
 * @date 2017/11/30
 */
public class RetrofitUtils extends RetrofitBase {

    private static RetrofitUtils mInstance = null;

    private RetrofitUtils() {

    }

    public static RetrofitUtils getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtils.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtils();
                }
            }
        }
        return mInstance;
    }


    @Override
    public String getBaseUrl() {
        return ApiUrl.BASE_URL;
    }
}
