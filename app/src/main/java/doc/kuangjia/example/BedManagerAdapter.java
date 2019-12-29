package com.zkys.pad.launcher.ui.homebed.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zkys.pad.launcher.MyApplication;
import com.zkys.pad.launcher.R;
import com.zkys.pad.launcher.constant.Constants;
import com.zkys.pad.launcher.ui.homebed.activity.InhospitalActivity;
import com.zkys.pad.launcher.ui.homebed.activity.OutHospitalActivity;
import com.zkys.pad.launcher.ui.homebed.bean.HomeBedBean;
import com.zkys.pad.launcher.util.StartActivityUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/8/13.
 */

public class BedManagerAdapter extends BaseQuickAdapter<HomeBedBean.DataBean, BaseViewHolder> {

    public BedManagerAdapter(@Nullable List<HomeBedBean.DataBean> data) {
        super(R.layout.item_manager_bed_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HomeBedBean.DataBean item) {

        helper.addOnClickListener(R.id.tv_in_or_leave);
        //科室 + 床位
        helper.setText(R.id.tv_bed_msg, item.getNumber()+"床");
        //姓名
        if(TextUtils.isEmpty(item.getUserName())){
            //说明是空床
            helper.setGone(R.id.al_not_empty, false);
            helper.setVisible(R.id.tv_empty_bed, true);
            helper.setText(R.id.tv_in_or_leave, "入院");
            helper.setVisible(R.id.tv_in_or_leave, true);
        }else {
            //说明不是空床
            helper.setVisible(R.id.al_not_empty, true);
            helper.setGone(R.id.tv_empty_bed, false);
            helper.setText(R.id.tv_patient_name, item.getUserName());
            helper.setText(R.id.tv_in_or_leave, "出院");
            helper.setVisible(R.id.tv_in_or_leave, false);
        }
        helper.addOnClickListener(R.id.tv_in_or_leave);

        //年龄
        if(item.getAge() == 0){
            helper.setText(R.id.tv_patient_age, "");
        }else {
            helper.setText(R.id.tv_patient_age, item.getAge()+"岁");
        }
        //性别
        if(item.getSex() == 1){
            helper.setText(R.id.tv_patient_sex, "男");
        }else if(item.getSex() == 2){
            helper.setText(R.id.tv_patient_sex, "女");
        }else {
            helper.setText(R.id.tv_patient_sex, "");
        }

        if(TextUtils.isEmpty(item.getHospitalizationNumber())){
            helper.setGone(R.id.tv_ble, false);
        }else {
            helper.setVisible(R.id.tv_ble, true);
            helper.setText(R.id.tv_ble, "住院号:"+item.getHospitalizationNumber());
        }

        //护理级别
        if(TextUtils.isEmpty(item.getNursingLevel())){
            helper.setGone(R.id.tv_nursing_grade, false);
        }else {
            helper.setVisible(R.id.tv_nursing_grade, true);
            helper.setText(R.id.tv_nursing_grade, "护理等级:"+item.getNursingLevel());
        }
        //饮食类别
        if(TextUtils.isEmpty(item.getDietCategory())){
            helper.setGone(R.id.tv_diet_type, false);
        }else {
            helper.setVisible(R.id.tv_diet_type, true);
            helper.setText(R.id.tv_diet_type, "饮食类别:"+item.getDietCategory());
        }

        //饮食类别
        if(TextUtils.isEmpty(item.getDeptName())){
            helper.setGone(R.id.tv_dept, false);
        }else {
            helper.setVisible(R.id.tv_dept, true);
            helper.setText(R.id.tv_dept, item.getDeptName());
        }

        //入院时间
        helper.setText(R.id.tv_in_time, "入院时间：" + item.getCreateTabbDate());

        if(TextUtils.isEmpty(item.getUserName())){
            helper.getView(R.id.tv_in_or_leave).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(Constants.HOSPITAL_IN,item);
                    StartActivityUtil.startActivity(MyApplication.getInstance(),InhospitalActivity.class,bundle);
                }
            });
        }else {
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(Constants.HOSPITAL_IN,item);
                    StartActivityUtil.startActivity(MyApplication.getInstance(),OutHospitalActivity.class,bundle);
                }
            });
        }
    }
}
