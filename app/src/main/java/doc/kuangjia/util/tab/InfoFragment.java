package com.union.replytax.ui.Info;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.union.replytax.R;
import com.union.replytax.base.BaseFragment;
import com.union.replytax.base.BasePresenter;
import com.union.replytax.kefu.ui.CustomerserviceActivity;
import com.union.replytax.ui.Info.adapter.FmPagerAdapter;
import com.union.replytax.ui.Info.bean.CategoryInfoBean;
import com.union.replytax.ui.Info.ui.activity.SearchActivity;
import com.union.replytax.ui.Info.ui.fragment.RecoFragment;
import com.union.replytax.ui.login.constant.LoginConstant;
import com.union.replytax.ui.login.ui.activity.LoginActivity;
import com.union.replytax.util.SPUtil;
import com.union.replytax.util.StartActivityUtil;
import com.union.replytax.widget.DragRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by Administrator on 2018/9/11.
 */
//资讯
public class InfoFragment extends BaseFragment implements DragRelativeLayout.DragImageClickListener{
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rel_search)
    RelativeLayout relSearch;
    @BindView(R.id.lly_toolbar)
    LinearLayout llyToolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.DragRelative)
    DragRelativeLayout dragRelativeLayout;

    private List<CategoryInfoBean.DataBean> dataX = new ArrayList<>();
    private CategoryInfoBean.DataBean dataBean;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private List<String> myChannelList = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();


    public static InfoFragment getInstance() {
        InfoFragment infoFragment = new InfoFragment();
        return infoFragment;
    }

    @Override
    public BasePresenter getBasePresenter() {
        return null;
    }

    @Override
    public void initBaserPresenter() {

    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_info;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void initView() {
        setTopPadding(llyToolbar);
        bindData();
        initListener();
        dragRelativeLayout.setDragImageListener(this);
    }

    private void bindData() {
        dataX = SPUtil.getDataList();
        myChannelList.add("推荐");
        idList.add(-1);
        for (int i = 0; i < dataX.size(); i++) {
            dataBean = dataX.get(i);
            myChannelList.add(dataBean.getCategoryName());
            idList.add(dataBean.getId());
        }
        viewpager.setOffscreenPageLimit(idList.size());
        if (idList.size() <= 5) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        for (int i = 0; i < idList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
            fragments.add(RecoFragment.newInstance(idList.get(i)));
        }
        viewpager.setAdapter(new FmPagerAdapter(fragments, getFragmentManager()));
        tabLayout.setupWithViewPager(viewpager);
        for (int j = 0; j < myChannelList.size(); j++) {
            tabLayout.getTabAt(j).setText(myChannelList.get(j));
        }
    }

    private void initListener() {


    }

    @OnClick({R.id.rel_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_search:
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onClick() {
        if (!SPUtil.getBooleanData(LoginConstant.isLogin)) {
            StartActivityUtil.startActivity(getActivity(), LoginActivity.class);
        } else {
            StartActivityUtil.startActivity(getActivity(), CustomerserviceActivity.class);
        }
    }
}
