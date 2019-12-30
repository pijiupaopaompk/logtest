package com.union.replytax.ui.Info.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.union.replytax.R;
import com.union.replytax.base.BaseFragment;
import com.union.replytax.base.BasePresenter;
import com.union.replytax.ui.Info.adapter.RecoAdapter;
import com.union.replytax.ui.Info.bean.InfoBean;
import com.union.replytax.ui.Info.bean.InfoEntity;
import com.union.replytax.ui.Info.presenter.RecomPresent;
import com.union.replytax.util.DensityUtil;
import com.union.replytax.util.LogFactory;
import com.union.replytax.widget.VerticalLineDecoration;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/9/6.
 */
//首页咨询数据
public class RecoFragment extends BaseFragment implements RecomPresent.IRecomInfoView {
    @BindView(R.id.my_recyclerview)
    RecyclerView myRecyclerview;
    @BindView(R.id.refreshLayout)
    EasyRefreshLayout refreshLayout;
    private RecoAdapter recoAdapter;
    private ArrayList<InfoBean.DataBean.RecordsBean> recordsBeanList = new ArrayList<>();
    private ArrayList<InfoBean.DataBean.RecordsBean> temprecordsBeanList = new ArrayList<>();
    private ArrayList<InfoEntity> infoEntityList = new ArrayList<>();
    private InfoBean.DataBean dataBean;
    private RecomPresent present;
    private boolean isInitView = false;
    private boolean isVisible = false;
    private int id = -1;
    private static String KEY = "TID";
    public JSONArray idarray = new JSONArray();
    @Override
    public BasePresenter getBasePresenter() {
        return present;
    }

    @Override
    public void initBaserPresenter() {
        present = new RecomPresent(this);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_recommend;
    }


    public static RecoFragment newInstance(int tid) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, tid);
        RecoFragment fragment = new RecoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        id = -1;
        idarray = new JSONArray();
        if (getArguments() != null) {
            //取出保存的频道TID
            id = getArguments().getInt(KEY);
//            LogFactory.l().e("id===" + id);
            idarray.add(id);
        }
        initRefreView();
    }


    public JSONObject getJsonMap() {
        JSONObject object = new JSONObject();
        if (id > 0) {
            object.put("categoryIdList", idarray);
        }
        object.put("isSearch", false);
        return object;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见，获取该标志记录下来
//       LogFactory.l().e("isVisibleToUser==="+isVisibleToUser);
        if(isVisibleToUser){
            isVisible = true;
            isCanLoadData();
        }else{
            isVisible = false;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isInitView=true;
        isCanLoadData();
    }

    private void isCanLoadData(){
        //所以条件是view初始化完成并且对用户可见
//        LogFactory.l().e("isInitView==="+isInitView);
//        LogFactory.l().e("isVisible==="+isVisible);
        if(isInitView && isVisible ){
            refreshLayout.autoRefresh(300);
            //防止重复加载数据
            isInitView = false;
            isVisible = false;
        }
    }


    private void initRefreView() {
//        LogFactory.l().e("initRefreView");
//        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        myRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerview.setHasFixedSize(true);
        myRecyclerview.setFadingEdgeLength(0);
        myRecyclerview.addItemDecoration(new VerticalLineDecoration(DensityUtil.dip2px(getContext
                (), 1),
                true));
        myRecyclerview.setFocusable(false);
        recoAdapter = new RecoAdapter(null);
        myRecyclerview.setAdapter(recoAdapter);
        refreshLayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        refreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                //加载更多
                if (dataBean != null) {
                    if (dataBean.isHasNextPage()) {
                        present.selectListByPage(dataBean.getNextPage(), getJsonMap());
                    }
                }
            }

            @Override
            public void onRefreshing() {
                //下拉刷新
                loadRefresh();
            }
        });
//        refreshLayout.autoRefresh(300);
    }

    private void loadRefresh() {
        present.selectListByPage(1, getJsonMap());
    }

    @Override
    public void onStop() {
        super.onStop();
//        LogFactory.l().e("onStop");
        refreshLayout.refreshComplete();
        refreshLayout.loadMoreComplete();
    }

    @Override
    public void getListsuccess(InfoBean infoBean) {
        refreshLayout.refreshComplete();
        refreshLayout.loadMoreComplete();

        dataBean = infoBean.getData();
        infoEntityList.clear();
        if (dataBean.isFirstPage()) {
            recordsBeanList = dataBean.getRecords();
            for (int i = 0; i < recordsBeanList.size(); i++) {
                InfoBean.DataBean.RecordsBean recordsBean = recordsBeanList.get(i);
                InfoEntity infoEntity = new InfoEntity();
                infoEntity.setRecordsBean(recordsBean);
                if (recordsBean.getType() == 2) {   //招聘
                    infoEntity.setType(InfoEntity.RECRUIT);
                } else {
                    infoEntity.setType(InfoEntity.EVENT);
                }
                infoEntityList.add(infoEntity);
            }
            recoAdapter.setNewData(infoEntityList);
        } else {
            temprecordsBeanList = dataBean.getRecords();
            recordsBeanList.addAll(temprecordsBeanList);
            for (int i = 0; i < recordsBeanList.size(); i++) {
                InfoBean.DataBean.RecordsBean recordsBean = recordsBeanList.get(i);
                InfoEntity infoEntity = new InfoEntity();
                infoEntity.setRecordsBean(recordsBean);
                if (recordsBean.getType() == 2) {   //招聘
                    infoEntity.setType(InfoEntity.RECRUIT);
                } else {
                    infoEntity.setType(InfoEntity.EVENT);
                }
                infoEntityList.add(infoEntity);
            }
            LogFactory.l().e("infoEntityList---" + infoEntityList.size());
            recoAdapter.setNewData(infoEntityList);
        }
        if (dataBean.isHasNextPage()) {
            refreshLayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        } else {
            refreshLayout.setLoadMoreModel(LoadModel.NONE);
        }
    }


    @Override
    public void getListError() {
        LogFactory.l().e("getListError");
        refreshLayout.refreshComplete();
        refreshLayout.loadMoreComplete();
    }
}
