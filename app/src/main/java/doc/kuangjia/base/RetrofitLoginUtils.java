package com.zkys.pad.launcher.base;


import com.zkys.pad.launcher.net.RetrofitLoginBase;
import com.zkys.pad.launcher.util.SPUtil;

import java.util.HashMap;

/**
 * @author any
 * @date 2017/11/30
 */
public class RetrofitLoginUtils extends RetrofitLoginBase {

    private static RetrofitLoginUtils mInstance = null;
    HashMap<String,Object> map=new HashMap<>();

    public RetrofitLoginUtils(HashMap<String, Object> map) {
        super(map);
        this.map = map;
    }


    public static RetrofitLoginUtils getInstance(HashMap<String,Object> map) {
        mInstance = new RetrofitLoginUtils(map);
       /* if (mInstance == null) {
            synchronized (RetrofitLoginUtils.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitLoginUtils(map);
                }
            }
        }*/
        return mInstance;
    }


    @Override
    public String getBaseUrl() {
        return ApiUrl.BASE_URL;
    }

    @Override
    public String getToken() {
        return SPUtil.getToken();
    }


}
