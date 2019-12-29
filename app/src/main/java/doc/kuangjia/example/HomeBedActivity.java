package com.zkys.pad.launcher.ui.homebed.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zkys.pad.launcher.R;
import com.zkys.pad.launcher.Rxbus.RxBus;
import com.zkys.pad.launcher.Rxbus.RxConsumer;
import com.zkys.pad.launcher.Rxbus.bean.RxRefreshHospitalBed;
import com.zkys.pad.launcher.base.BaseActivity;
import com.zkys.pad.launcher.base.BaseBean;
import com.zkys.pad.launcher.base.BasePresenter;
import com.zkys.pad.launcher.constant.Constants;
import com.zkys.pad.launcher.ui.homebed.adapter.BedManagerAdapter;
import com.zkys.pad.launcher.ui.homebed.bean.HomeBedBean;
import com.zkys.pad.launcher.ui.homebed.bean.StatusBean;
import com.zkys.pad.launcher.ui.homebed.presenter.HomebedPresent;
import com.zkys.pad.launcher.util.TeamSp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeBedActivity extends BaseActivity implements HomebedPresent.IHomebedView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcv_list)
    RecyclerView rcvList;
    @BindView(R.id.btn_card)
    Button btnCard;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.lly_empty_beds)
    LinearLayout llyEmptyBeds;
    private HomebedPresent present;
    private int teamId = -1;
    private int pageNum = 1;
    private List<HomeBedBean.DataBean> dataBeanList = new ArrayList<>();
    private List<HomeBedBean.DataBean> tempdataBeanList = new ArrayList<>();
    private BedManagerAdapter bedManagerAdapter;
    private boolean isBedOpen=false;
    private int deptId=-1;

    @Override
    public BasePresenter getBasePresenter() {
        return present;
    }

    @Override
    public void initBasePresenter() {
        present = new HomebedPresent(this);
    }


    @Override
    public void initData() {
        Bundle extras = getIntent().getExtras();
        teamId = extras.getInt(Constants.CHOOSE_TEAM_ID);
        deptId = TeamSp.getInt(Constants.CHOOSE_TEAM_DEPT_ID,-1);
        present.getBedList(1, teamId);

        present.switchStatus(deptId);

        RxBus.getInstance().subscribe(this, RxRefreshHospitalBed.class, new
                RxConsumer<RxRefreshHospitalBed>() {
            @Override
            public void handler(RxRefreshHospitalBed o) {
                present.getBedList(1, teamId);
            }
        });
    }

    @Override
    public void initView() {
        setToolbarText("床位管理");

        initRecyclerView();

        initListener();
    }


    private void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                present.getBedList(1, teamId);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                present.getBedList(1, teamId);
            }
        });

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvList.setLayoutManager(layoutManager);
        bedManagerAdapter = new BedManagerAdapter(dataBeanList);
        rcvList.setAdapter(bedManagerAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_bed;
    }


    @Override
    public void getBedList(HomeBedBean baseBean) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        llyEmptyBeds.setVisibility(View.GONE);
        if (pageNum == 1) {
            dataBeanList = baseBean.getData();
            if (dataBeanList != null && dataBeanList.size() > 0) {
                if (dataBeanList.size() < 10) {
                    refreshLayout.setEnableLoadMore(false);
                } else {
                    refreshLayout.setEnableLoadMore(true);
                }
            }
        } else {
            tempdataBeanList = baseBean.getData();
            if (tempdataBeanList != null && tempdataBeanList.size() > 0) {
                if (tempdataBeanList.size() < 10) {
                    refreshLayout.setEnableLoadMore(false);
                } else {
                    refreshLayout.setEnableLoadMore(true);
                }
                dataBeanList.addAll(tempdataBeanList);
            }
        }
        bedManagerAdapter.setNewData(dataBeanList);
    }


    @Override
    public void getBedListFail() {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();

        llyEmptyBeds.setVisibility(View.VISIBLE);
    }

    @Override
    public void switchStatus(StatusBean statusBean) {
        btnCard.setVisibility(View.VISIBLE);
        if(statusBean.getData()==0){
            isBedOpen=false;
        }else {
            isBedOpen=true;
        }
        setBedStatus();
    }

    //设置按钮
    private void setBedStatus() {
        if(isBedOpen){
            btnCard.setText(getResources().getString(R.string.out_bed_card));
            btnCard.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_out_bed_card));
        }else {
            btnCard.setText(getResources().getString(R.string.enter_bed_card));
            btnCard.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_login));
        }
    }

    @Override
    public void switchStatusFail() {
        btnCard.setVisibility(View.GONE);
    }

    @Override
    public void switchOpen(BaseBean baseBean) {
        isBedOpen=true;
        setBedStatus();
    }

    @Override
    public void switchClose(BaseBean baseBean) {
        isBedOpen=false;
        setBedStatus();
    }


    @OnClick(R.id.btn_card)
    public void onViewClicked() {
        if(isBedOpen){
            present.switchClose(teamId);
        }else {
            present.switchOpen(teamId);
        }
    }
}
