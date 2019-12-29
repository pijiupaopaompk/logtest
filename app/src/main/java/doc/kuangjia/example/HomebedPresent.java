package com.zkys.pad.launcher.ui.homebed.presenter;


import com.zkys.pad.launcher.Service.RetrofitLoginService;
import com.zkys.pad.launcher.base.BaseBean;
import com.zkys.pad.launcher.base.BasePresenter;
import com.zkys.pad.launcher.base.IBaseView;
import com.zkys.pad.launcher.net.ExceptionHandle;
import com.zkys.pad.launcher.net.ICallBack;
import com.zkys.pad.launcher.net.RequestDataManager;
import com.zkys.pad.launcher.ui.homebed.bean.HomeBedBean;
import com.zkys.pad.launcher.ui.homebed.bean.StatusBean;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/9/5.
 */

public class HomebedPresent implements BasePresenter {
    private IHomebedView homebedView;
    private RequestDataManager dataManager;
    private RetrofitLoginService.RetrofitLoginServiceApi api;
    public HomebedPresent(IHomebedView rigisterView) {
        this.homebedView = rigisterView;
    }

    @Override
    public void onAttach() {
        dataManager=new RequestDataManager();
    }

    @Override
    public void onDetach() {
        api=null;
        dataManager=null;
    }

    @Override
    public IBaseView getView() {
        return homebedView;
    }


    //查询床位列表
    public void getBedList(int pageNum,int teamId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("teamId", teamId);
        map.put("pageNum", pageNum);
        map.put("pageSize", 10);
        api=RetrofitLoginService.getApi(map);
        dataManager.doTask(api.getBedList(map), getView().getLifecycleProvider(), new ICallBack<HomeBedBean>() {
            @Override
            public void start(Disposable d) {

            }

            @Override
            public void success(HomeBedBean baseBean) {
                if(baseBean.getCode()==200){
                    homebedView.getBedList(baseBean);
                }else {
                    homebedView.showToast(baseBean.getMsg());
                    homebedView.getBedListFail();
                }
            }

            @Override
            public void error(ExceptionHandle.ResponeThrowable dataEx) {
                getView().showToast(dataEx.getMessage());
                homebedView.getBedListFail();
            }

            @Override
            public void complete() {

            }
        });
    }


    public void switchStatus(int deptId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("deptId", deptId);
        map.put("type", 1);
        api=RetrofitLoginService.getApi(map);
        dataManager.doTask(api.switchStatus(map), getView().getLifecycleProvider(), new ICallBack<StatusBean>() {
            @Override
            public void start(Disposable d) {

            }

            @Override
            public void success(StatusBean baseBean) {
                if(baseBean.getCode()==200){
                    homebedView.switchStatus(baseBean);
                }else {
                    homebedView.showToast(baseBean.getMsg());
                    homebedView.switchStatusFail();
                }
            }

            @Override
            public void error(ExceptionHandle.ResponeThrowable dataEx) {
                getView().showToast(dataEx.getMessage());
                homebedView.switchStatusFail();
            }

            @Override
            public void complete() {

            }
        });
    }

    //开启床头卡
    public void switchOpen(int teamId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("teamId", teamId);
        map.put("type", 1);
        api=RetrofitLoginService.getApi(map);
        dataManager.doTask(api.switchOpen(map), getView().getLifecycleProvider(), new ICallBack<BaseBean>() {
            @Override
            public void start(Disposable d) {

            }

            @Override
            public void success(BaseBean baseBean) {
                if(baseBean.getCode()==200){
                    homebedView.switchOpen(baseBean);
                }else {
                    homebedView.showToast(baseBean.getMsg());
                }
            }

            @Override
            public void error(ExceptionHandle.ResponeThrowable dataEx) {
                getView().showToast(dataEx.getMessage());
            }

            @Override
            public void complete() {

            }
        });
    }

    //关闭床头卡
    public void switchClose(int teamId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("teamId", teamId);
        map.put("type", 1);
        api=RetrofitLoginService.getApi(map);
        dataManager.doTask(api.switchClose(map), getView().getLifecycleProvider(), new ICallBack<BaseBean>() {
            @Override
            public void start(Disposable d) {

            }

            @Override
            public void success(BaseBean baseBean) {
                if(baseBean.getCode()==200){
                    homebedView.switchClose(baseBean);
                }else {
                    homebedView.showToast(baseBean.getMsg());
                }
            }

            @Override
            public void error(ExceptionHandle.ResponeThrowable dataEx) {
                getView().showToast(dataEx.getMessage());
            }

            @Override
            public void complete() {

            }
        });
    }


    public interface IHomebedView extends IBaseView{
        void getBedList(HomeBedBean baseBean);
        void getBedListFail();
        void switchStatus(StatusBean baseBean);
        void switchStatusFail();
        void switchOpen(BaseBean baseBean);
        void switchClose(BaseBean baseBean);
    }
}
