package com.zkys.pad.launcher.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgyersdk.update.PgyUpdateManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zkys.pad.launcher.R;
import com.zkys.pad.launcher.Rxbus.RxBus;
import com.zkys.pad.launcher.Rxbus.RxConsumer;
import com.zkys.pad.launcher.Rxbus.bean.RxCloseDrawLayoutEvent;
import com.zkys.pad.launcher.Rxbus.bean.RxLogoutEvent;
import com.zkys.pad.launcher.Rxbus.bean.RxRefreshInfo;
import com.zkys.pad.launcher.Rxbus.bean.RxRefreshMainTeam;
import com.zkys.pad.launcher.Rxbus.bean.RxRefreshMessage;
import com.zkys.pad.launcher.base.BaseActivity;
import com.zkys.pad.launcher.base.BasePresenter;
import com.zkys.pad.launcher.constant.Constants;
import com.zkys.pad.launcher.loadbitmap.GlideImageLoader;
import com.zkys.pad.launcher.ui.about.AboutActivity;
import com.zkys.pad.launcher.ui.homebed.activity.HomeBedActivity;
import com.zkys.pad.launcher.ui.homemission.activity.MissionActivity;
import com.zkys.pad.launcher.ui.investigation.activity.InvestigationActivity;
import com.zkys.pad.launcher.ui.login.model.UserBean;
import com.zkys.pad.launcher.ui.mess.ui.MessageActivity;
import com.zkys.pad.launcher.ui.repair.activity.RepairStatusActivity;
import com.zkys.pad.launcher.ui.setting.SystemSettingActivity;
import com.zkys.pad.launcher.ui.team.activity.TeamActivity;
import com.zkys.pad.launcher.ui.timetask.activity.TimeTaskActivity;
import com.zkys.pad.launcher.ui.userinfo.UserInfoActivity;
import com.zkys.pad.launcher.ui.userinfo.adapter.MessageAdapter;
import com.zkys.pad.launcher.ui.userinfo.bean.MessageBean;
import com.zkys.pad.launcher.ui.userinfo.presenter.MainPresent;
import com.zkys.pad.launcher.util.LogFactory;
import com.zkys.pad.launcher.util.SPUtil;
import com.zkys.pad.launcher.util.StartActivityUtil;
import com.zkys.pad.launcher.util.TeamSp;
import com.zkys.pad.launcher.util.UIHelper;
import com.zkys.pad.launcher.view.EBDrawerLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//主页
public class MainActivity extends BaseActivity implements MainPresent.IMainView {
    @BindView(R.id.rel_open)
    RelativeLayout relOpen;
    @BindView(R.id.lly_bed)
    LinearLayout llyBed;
    @BindView(R.id.lly_team)
    LinearLayout llyTeam;
    @BindView(R.id.lly_mission)
    LinearLayout llyMission;
    @BindView(R.id.lly_dy)
    LinearLayout llyDy;
    @BindView(R.id.lly_top)
    LinearLayout llyTop;
    @BindView(R.id.rcv_list)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.lly_draw)
    LinearLayout llyDraw;
    @BindView(R.id.drawlayout)
    EBDrawerLayout drawlayout;
    @BindView(R.id.iv_user)
    ImageView ivUser;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_team)
    TextView tvTeam;
    @BindView(R.id.rel_edit)
    RelativeLayout relEdit;
    @BindView(R.id.lly_dw_team)
    LinearLayout llyDwTeam;
    @BindView(R.id.lly_dw_set)
    LinearLayout llyDwSet;
    @BindView(R.id.lly_dw_about)
    LinearLayout llyDwAbout;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_choose_team)
    TextView tvChooseTeam;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;
    @BindView(R.id.rel_nodata)
    RelativeLayout relNodata;
    private UserBean userBean;
    private MainPresent present;
    private MessageAdapter messageAdapter;
    private List<MessageBean.DataBean> dataBeanList = new ArrayList<>();
    private List<MessageBean.DataBean> tempdataBeanList = new ArrayList<>();
    private int pageNum = 1;
    private int teamId = -1;
    private String hospitalName = "";
    private String deptName = "";
    private String teamName = "";
    @Override
    public BasePresenter getBasePresenter() {
        return present;
    }

    @Override
    public void initBasePresenter() {
        present = new MainPresent(this);
    }


    @Override
    public void initData() {
        userBean = SPUtil.getObject(UserBean.class);
        Constants.USER_SHAREDPREFERENCES=userBean.getData().getMobile();
        Constants.USER_BEAN_ID=userBean.getData().getId();
        present.setJPush();
        present.getHomeMsg(1, userBean.getData().getId());
        LogFactory.l().i("======"+Constants.USER_SHAREDPREFERENCES);
        hospitalName = TeamSp.getString(Constants.CHOOSE_TEAM_HOSPITAL, "");
        deptName = TeamSp.getString(Constants.CHOOSE_TEAM_DEPT, "");
        teamName = TeamSp.getString(Constants.CHOOSE_TEAM_NAME, "");
        if (!hospitalName.equals("") && (!deptName.equals(""))) {
            tvTeam.setText(hospitalName + "-" + deptName);
        }
        if (!teamName.equals("")) {
            tvChooseTeam.setText(teamName);
        }

        tvUser.setText(userBean.getData().getName());
        if (userBean.getData().getPortrait() != null && (!userBean.getData().getPortrait().equals
                (""))) {
            GlideImageLoader.loadCircleImage(userBean.getData().getPortrait(), ivUser);
        } else {
            GlideImageLoader.loadCircleImage(R.mipmap.home_logo, ivUser);
        }
        tvVersion.setText("当前版本:" + UIHelper.getVersionName(this) + "   Builds:" + UIHelper
                .getVersionCode(this));

        RxBus.getInstance().subscribe(this, RxRefreshInfo.class, new RxConsumer<RxRefreshInfo>() {
            @Override
            public void handler(RxRefreshInfo o) {
                userBean = SPUtil.getObject(UserBean.class);
                tvUser.setText(userBean.getData().getName());
                GlideImageLoader.loadCircleImage(userBean.getData().getPortrait(), ivUser);
            }
        });

        /** 新版本 **/
        new PgyUpdateManager.Builder()
                .setForced(true)                //设置是否强制更新
                .setUserCanRetry(true)         //失败后是否提示重新下载
                .setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk
                .register();

        //退出事件
        RxBus.getInstance().subscribe(this, RxLogoutEvent.class, new RxConsumer<RxLogoutEvent>() {
            @Override
            public void handler(RxLogoutEvent o) {
                finish();
            }
        });

        //关闭侧边栏事件
        RxBus.getInstance().subscribe(this, RxCloseDrawLayoutEvent.class, new
                RxConsumer<RxCloseDrawLayoutEvent>() {
                    @Override
                    public void handler(RxCloseDrawLayoutEvent o) {
                        if (drawlayout.isDrawerOpen(GravityCompat.START)) {
                            drawlayout.closeDrawer(GravityCompat.START);
                        }
                    }
                });

        RxBus.getInstance().subscribe(this, RxRefreshMessage.class, new
                RxConsumer<RxRefreshMessage>() {
                    @Override
                    public void handler(RxRefreshMessage o) {
                        present.getHomeMsg(1, userBean.getData().getId());
                    }
                });

        RxBus.getInstance().subscribe(this, RxRefreshMainTeam.class, new
                RxConsumer<RxRefreshMainTeam>() {
                    @Override
                    public void handler(RxRefreshMainTeam team) {
                        tvTeam.setText(team.hosipitalName + "-" + team.depName);
                        tvChooseTeam.setText(team.teamName);
                    }
                });
    }

    @Override
    public void initView() {
        setTopPadding(llyTop);

        initRecyclerView();

        initListener();
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                present.getHomeMsg(1, userBean.getData().getId());
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                present.getHomeMsg(pageNum, userBean.getData().getId());
            }
        });
    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(dataBeanList);
        recyclerview.setAdapter(messageAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @OnClick({R.id.rel_open, R.id.lly_bed, R.id.lly_team, R.id.lly_mission, R.id.lly_dy, R.id
            .lly_user, R.id.lly_dw_team, R.id.lly_dw_set, R.id.lly_dw_about,
            R.id.lly_time,R.id.lly_repair,R.id.lly_mess})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        teamId = TeamSp.getInt(Constants.CHOOSE_TEAM_ID, -1);
        switch (view.getId()) {
            case R.id.rel_open:
                openLeft();
                break;
            case R.id.lly_bed:
                if (chooseTeam()) return;
                bundle.putInt(Constants.CHOOSE_TEAM_ID, teamId);
                StartActivityUtil.startActivity(this, HomeBedActivity.class, bundle);
                break;
            case R.id.lly_team:
                StartActivityUtil.startActivity(this, TeamActivity.class);
                break;
            case R.id.lly_mission:
                if (chooseTeam()) return;
                bundle.putInt(Constants.CHOOSE_TEAM_ID, teamId);
                StartActivityUtil.startActivity(this, MissionActivity.class, bundle);
                break;
            case R.id.lly_dy:
                if (chooseTeam()) return;
                bundle.putInt(Constants.CHOOSE_TEAM_ID, teamId);
                StartActivityUtil.startActivity(this, InvestigationActivity.class, bundle);
                break;
            case R.id.lly_user:
                StartActivityUtil.startActivity(this, UserInfoActivity.class);
                break;
            case R.id.lly_dw_team:
                bundle.putInt(Constants.CHOOSE_TYPE, 1);
                StartActivityUtil.startActivity(this, TeamActivity.class, bundle);
                break;
            case R.id.lly_dw_set:
                StartActivityUtil.startActivity(this, SystemSettingActivity.class);
                break;
            case R.id.lly_dw_about:
                StartActivityUtil.startActivity(this, AboutActivity.class);
                break;
            case R.id.lly_time:
                if (chooseTeam()) return;
                StartActivityUtil.startActivity(this, TimeTaskActivity.class);
                break;
            case R.id.lly_repair:
                if (chooseTeam()) return;
                StartActivityUtil.startActivity(this, RepairStatusActivity.class);
                break;
            case R.id.lly_mess:
                if (chooseTeam()) return;
                StartActivityUtil.startActivity(this, MessageActivity.class);
                break;
        }
    }

    //选择团队
    private boolean chooseTeam() {
        if (teamId == -1) {
            showToast("请选择团队");
            openLeft();
            return true;
        }
        return false;
    }

    private void openLeft() {
        if (!drawlayout.isDrawerOpen(GravityCompat.START)) {
            drawlayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void getHomeMsg(MessageBean messageBean) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        relNodata.setVisibility(View.GONE);
        if (pageNum == 1) {
            dataBeanList = messageBean.getData();
            if (dataBeanList != null && dataBeanList.size() > 0) {
                if (dataBeanList.size() < 10) {
                    refreshLayout.setEnableLoadMore(false);
                } else {
                    refreshLayout.setEnableLoadMore(true);
                }
            } else {
                relNodata.setVisibility(View.VISIBLE);
                tvNodata.setText("暂无最新消息");
            }
        } else {
            tempdataBeanList = messageBean.getData();
            if (tempdataBeanList != null && tempdataBeanList.size() > 0) {
                if (tempdataBeanList.size() < 10) {
                    refreshLayout.setEnableLoadMore(false);
                } else {
                    refreshLayout.setEnableLoadMore(true);
                }
                dataBeanList.addAll(tempdataBeanList);
            }
        }
        messageAdapter.setNewData(dataBeanList);
    }

    @Override
    public void getMsgFail() {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        relNodata.setVisibility(View.VISIBLE);
        tvNodata.setText("暂无最新消息");
    }


    //记录用户首次点击返回键的时间
    private long firstTime=0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                long secondTime=System.currentTimeMillis();
                if(secondTime-firstTime>2000){
                    showToast("再按一次退出程序");
                    firstTime=secondTime;
                    return true;
                }else{
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
