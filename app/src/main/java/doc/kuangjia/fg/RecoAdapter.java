package com.union.replytax.ui.Info.adapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.union.replytax.MyApplication;
import com.union.replytax.R;
import com.union.replytax.network.ApiUrl;
import com.union.replytax.ui.Info.bean.InfoBean;
import com.union.replytax.ui.Info.bean.InfoEntity;
import com.union.replytax.ui.Info.ui.activity.ArticleDetailActivity;
import com.union.replytax.ui.Info.ui.activity.EventActivity;
import com.union.replytax.ui.Info.ui.activity.InfoDetailActivity;
import com.union.replytax.util.StartActivityUtil;

import java.util.ArrayList;

import any.com.loadbitmap.GlideImageLoader;

/**
 * Created by Administrator on 2018/9/26.
 */

public class RecoAdapter extends BaseMultiItemQuickAdapter<InfoEntity, BaseViewHolder> {

    private InfoBean.DataBean.RecordsBean itemRecordsBean;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public RecoAdapter(ArrayList<InfoEntity> data) {
        super(data);
        addItemType(InfoEntity.EVENT, R.layout.home_page_top);
//        addItemType(InfoEntity.ORDINARY, R.layout.home_page_item);
        addItemType(InfoEntity.RECRUIT, R.layout.item_recruit);
    }

    @Override
    protected void convert(BaseViewHolder helper, final InfoEntity item) {
        itemRecordsBean = item.getRecordsBean();
        switch (item.getItemType()) {
            case InfoEntity.EVENT:
                helper.setText(R.id.tv_top_title, itemRecordsBean.getTitle());
                helper.setText(R.id.tv_event, itemRecordsBean.getRightTag());
                if(itemRecordsBean.getCover()!=null && (!itemRecordsBean.getCover().equals(""))){
                    helper.setVisible(R.id.iv_top,true);
                    GlideImageLoader.loadImage(itemRecordsBean.getCover(), (ImageView) helper.getView(R.id.iv_top));
                }else {
                    helper.getView(R.id.iv_top).setVisibility(View.GONE);
                }

                if(itemRecordsBean.isIsTop()){
                    helper.setVisible(R.id.tv_top,true);
                }else {
                    helper.getView(R.id.tv_top).setVisibility(View.GONE);
                }
                if(itemRecordsBean.isFree()){
                    helper.getView(R.id.tv_isFree).setVisibility(View.GONE);
                }else {
                    helper.setVisible(R.id.tv_isFree,true);
                }
                helper.setText(R.id.tv_tab_top, itemRecordsBean.getCategoryName());
                if(itemRecordsBean.getCategoryName().equals("热门活动")){
                    helper.setTextColor(R.id.tv_tab_top,MyApplication.getInstance().getApplicationContext().getResources().getColor(R.color.light_yellow));
                }else if(itemRecordsBean.getCategoryName().equals("特色专题")){
                    helper.setTextColor(R.id.tv_tab_top,MyApplication.getInstance().getApplicationContext().getResources().getColor(R.color.blue_029bf0));
                }else {
                    helper.setTextColor(R.id.tv_tab_top,MyApplication.getInstance().getApplicationContext().getResources().getColor(R.color.color_cccccc));
                }
                helper.setText(R.id.tv_top_time, itemRecordsBean.getRecordArticleTime());
                helper.setText(R.id.tv_top_title, itemRecordsBean.getTitle());
                if(!itemRecordsBean.getWritNo().equals("") && (!itemRecordsBean.getWritOrder().equals(""))){
                    helper.setText(R.id.tv_wenhao,itemRecordsBean.getWritNo()+"["+itemRecordsBean.getYear()+"]"+itemRecordsBean.getWritOrder()+"号");
                }else {
                    helper.setText(R.id.tv_wenhao,"");
                }
                break;
            case InfoEntity.RECRUIT:
                helper.setText(R.id.tv_position, itemRecordsBean.getTitle());
                helper.setText(R.id.tv_salary, itemRecordsBean.getRightTag());
                helper.setText(R.id.tv_education, itemRecordsBean.getCategoryName());
                helper.setText(R.id.tv_date, itemRecordsBean.getCreateTimeStr());
                break;
            default:
                break;
        }

        final Bundle intent=new Bundle();
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoBean.DataBean.RecordsBean bean = item.getRecordsBean();
                intent.putString("id",bean.getSrcId()+"");
                intent.putString("InfoImg",bean.getCover());
                switch (bean.getType()){
                    case 0:  //文章
                        intent.putInt("type",0);
                        intent.putString("infoUrl", ApiUrl.ARTICLE_DETAIL);
                        StartActivityUtil.startActivity(MyApplication.getInstance().getApplicationContext(), ArticleDetailActivity.class,intent);
                        break;
                    case 1:  //活动
                        intent.putInt("type",1);
                        intent.putString("infoUrl", ApiUrl.ACTIVE_DETAIL);
                        StartActivityUtil.startActivity(MyApplication.getInstance().getApplicationContext(), EventActivity.class,intent);
                        break;
                    case 2: //招聘
                        intent.putInt("type",2);
                        intent.putString("infoUrl", ApiUrl.JOINUS_DETAIL);
                        StartActivityUtil.startActivity(MyApplication.getInstance().getApplicationContext(), InfoDetailActivity.class,intent);
                        break;
                }

            }
        });

    }
}