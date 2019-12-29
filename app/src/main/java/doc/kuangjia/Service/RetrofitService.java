package com.zkys.pad.launcher.Service;


import com.zkys.pad.launcher.base.ApiUrl;
import com.zkys.pad.launcher.base.BaseBean;
import com.zkys.pad.launcher.base.RetrofitUtils;
import com.zkys.pad.launcher.ui.login.model.UserBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018/9/5.
 */

public class RetrofitService {
    public static RetrofitServiceApi getApi(){
        RetrofitServiceApi api= RetrofitUtils.getInstance().create(RetrofitServiceApi.class);
        return api;
    }



    public interface RetrofitServiceApi{
        @POST(ApiUrl.POST_SEND_VERIFYCODE)
        Observable<BaseBean> getSmsCode(@QueryMap Map<String, Object> map);   //获取验证码


        @POST(ApiUrl.POST_SAVE_WORKER)
        Observable<BaseBean> workSave(@QueryMap Map<String, Object> map);   //保存医护人员信息||修改密码

        @POST(ApiUrl.WORKER_LOGIN)
        Observable<UserBean> login(@QueryMap Map<String, Object> map);   //登录
    }



}
