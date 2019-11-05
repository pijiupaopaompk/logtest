package com.soft.logtest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentActivity extends android.support.v4.app.FragmentActivity {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.tv_text1)
    TextView tvText1;
    @BindView(R.id.tv_text2)
    TextView tvText2;
    @BindView(R.id.tv_text3)
    TextView tvText3;
    @BindView(R.id.tv_text4)
    TextView tvText4;
    @BindView(R.id.lly)
    LinearLayout lly;
    private android.support.v4.app.FragmentManager manager;
    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);

        //获得Fragment的管理器
        manager = getSupportFragmentManager();
        //获得事物的操作
        transaction = manager.beginTransaction();
        Test1Fragment fragment = new Test1Fragment();
        //添加内容
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @OnClick({R.id.tv_text1, R.id.tv_text2, R.id.tv_text3, R.id.tv_text4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_text1:
                //创建对象
                Test1Fragment fragment = new Test1Fragment();
                //添加内容
                transaction.replace(R.id.content, fragment);
                //提交
                break;
            case R.id.tv_text2:
                //创建对象
                Test2Fragment fragment2 = new Test2Fragment();
                //添加内容
                transaction.replace(R.id.content, fragment2);
                //提交
                break;
            case R.id.tv_text3:
                //创建对象
                Test3Fragment fragment3 = new Test3Fragment();
                //添加内容
                transaction.replace(R.id.content, fragment3);
                //提交
                break;
            case R.id.tv_text4:
                //创建对象
                Test4Fragment fragment4 = new Test4Fragment();
                //添加内容
                transaction.replace(R.id.content, fragment4);
                //提交

                break;
        }
        transaction.commit();
    }
}
