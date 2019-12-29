package com.zkys.pad.launcher.Service;


import com.zkys.pad.launcher.base.ApiUrl;
import com.zkys.pad.launcher.base.BaseBean;
import com.zkys.pad.launcher.base.RetrofitLoginUtils;
import com.zkys.pad.launcher.ui.homebed.bean.DietBean;
import com.zkys.pad.launcher.ui.homebed.bean.HomeBedBean;
import com.zkys.pad.launcher.ui.homebed.bean.NursBean;
import com.zkys.pad.launcher.ui.homebed.bean.StatusBean;
import com.zkys.pad.launcher.ui.homemission.bean.HomeMissionBean;
import com.zkys.pad.launcher.ui.investigation.bean.HistoryBean;
import com.zkys.pad.launcher.ui.investigation.bean.HistoryItemBean;
import com.zkys.pad.launcher.ui.investigation.bean.InvestBean;
import com.zkys.pad.launcher.ui.investigation.bean.InvestDetailBean;
import com.zkys.pad.launcher.ui.investigation.bean.InvestHistoryBean;
import com.zkys.pad.launcher.ui.repair.bean.QuesListBean;
import com.zkys.pad.launcher.ui.repair.bean.RepairStatusBean;
import com.zkys.pad.launcher.ui.team.bean.AddTeamBean;
import com.zkys.pad.launcher.ui.team.bean.AuthenBean;
import com.zkys.pad.launcher.ui.team.bean.BulidBean;
import com.zkys.pad.launcher.ui.team.bean.DepartMentBean;
import com.zkys.pad.launcher.ui.team.bean.HospitalBean;
import com.zkys.pad.launcher.ui.team.bean.TeamBean;
import com.zkys.pad.launcher.ui.team.bean.TeamManagerBean;
import com.zkys.pad.launcher.ui.timetask.bean.BedDetailBean;
import com.zkys.pad.launcher.ui.timetask.bean.BedListBean;
import com.zkys.pad.launcher.ui.timetask.bean.FileNameBean;
import com.zkys.pad.launcher.ui.timetask.bean.MissionBean;
import com.zkys.pad.launcher.ui.timetask.bean.TaskDetailBean;
import com.zkys.pad.launcher.ui.timetask.bean.TaskListBean;
import com.zkys.pad.launcher.ui.timetask.bean.TaskSaveBean;
import com.zkys.pad.launcher.ui.userinfo.bean.MessageBean;
import com.zkys.pad.launcher.ui.userinfo.bean.UserInfoBean;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018/9/5.
 */

public class RetrofitLoginService {
    public static RetrofitLoginServiceApi getApi(HashMap<String,Object> map){
        RetrofitLoginServiceApi api= RetrofitLoginUtils.getInstance(map).create(RetrofitLoginServiceApi.class);
        return api;
    }



    public interface RetrofitLoginServiceApi{

        @GET(ApiUrl.GET_TOKEN)
        Observable<String> getToken(@Query("bucketName") String bucketName);   //获取七牛云token

        @POST(ApiUrl.WORK_UPDATE)
        Observable<UserInfoBean> workUpdate(@QueryMap HashMap<String,Object> map);   //更新用户信息

        @GET(ApiUrl.TEAM_LIST)
        Observable<TeamBean> getTeamList(@Path("workerId") int workerId);

        //获取医院列表
        @POST(ApiUrl.HOSPITAL_LIST)
        Observable<HospitalBean> getHospitals(@QueryMap HashMap<String, Object> map);


        //获取科室列表
        @POST(ApiUrl.GET_DEPERTMENT_LIST)
        Observable<DepartMentBean> getDepartmentList(@QueryMap Map<String, Object> map);

        //添加团队成员
        @GET(ApiUrl.WORKER_MOBILE)
        Observable<AddTeamBean> workerMoblie(@QueryMap Map<String, Object> map);

        //创建团队
        @POST(ApiUrl.TEAM_SAVE)
        Observable<BulidBean> buildTeam(@QueryMap Map<String, Object> map);

        @POST(ApiUrl.TEAM_AUTHENTICATION)
        Observable<AuthenBean> authenticationTeam(@QueryMap Map<String, Object> map);

        @POST(ApiUrl.TEAM_DELETE)
        Observable<BaseBean> deleteTeam(@Query("id") int id);   //删除团队

        @POST(ApiUrl.TEAMMEMBER_SAVE)
        Observable<BaseBean> teamMemberSave(@QueryMap Map<String, Object> map);   //添加团队成员

        @GET(ApiUrl.HOME_MSG)
        Observable<MessageBean> getHomeMsg(@QueryMap Map<String, Object> map);   //首页消息

        @POST(ApiUrl.GET_BED_LIST)
        Observable<HomeBedBean> getBedList(@QueryMap Map<String, Object> map);   //获取床位列表

        //查询护理级别
        @POST(ApiUrl.SYSTEM_DICT_LIST)
        Observable<NursBean> queryNursingLevel(@QueryMap Map<String, Object> map);

        //查询饮食类别
        @POST(ApiUrl.SYSTEM_DICT_LIST)
        Observable<DietBean> queryDietLevel(@QueryMap Map<String, Object> map);

        //入院出院
        @POST(ApiUrl.IN_OUT_HOSPITAL)
        Observable<BaseBean> inOrOutHospital(@QueryMap Map<String, Object> map);

        //宣教
        @POST(ApiUrl.MISSION_LIST)
        Observable<HomeMissionBean> getMissionList(@QueryMap Map<String, Object> map);

        //发送极光推送
        @POST(ApiUrl.J_PUSH_SEND)
        Observable<BaseBean> sendJPush(@QueryMap Map<String, Object> map);

        //查询床头卡状态
        @POST(ApiUrl.SWITCH_STATUS)
        Observable<StatusBean> switchStatus(@QueryMap Map<String, Object> map);

        //打开床头卡状态
        @POST(ApiUrl.SWITCH_STATUS_OPEN)
        Observable<BaseBean> switchOpen(@QueryMap Map<String, Object> map);

        //关闭床头卡状态
        @POST(ApiUrl.SWITCH_STATUS_CLOSE)
        Observable<BaseBean> switchClose(@QueryMap Map<String, Object> map);

        //满意度列表
        @GET(ApiUrl.SURVEY_STASFACTION_LIST)
        Observable<InvestBean> querySatisfactionsList(@QueryMap Map<String, Object> map);

        // 根据id返回调查问卷数据
        @GET(ApiUrl.SURVEY_DETAIL)
        Observable<InvestDetailBean> satisfactionDetail(@Path("id") int id);

        @POST(ApiUrl.J_PUSH_SEND_RECORD)
        Observable<InvestHistoryBean> sendJPushRecord(@QueryMap Map<String, Object> map);

        //满意度回答列表
        @GET(ApiUrl.GET_ANSWER_LIST)
        Observable<BaseBean> getAnswerList(@Path("pusherId") int pusherId,@Path("surveyId") int surveyId);

        //满意度回答列表
        @GET(ApiUrl.GET_ANSWER_STIS)
        Observable<HistoryItemBean> getAnswerStis(@Path("surveyId") int surveyId,@QueryMap Map<String, Object> map);

        //调查表推送统计
        @POST(ApiUrl.POST_PUSH_STIS)
        Observable<HistoryBean> pushStis(@QueryMap Map<String, Object> map);

        //获取团队成员列表
        @POST(ApiUrl.TEAM_MEMBER_LIST)
        Observable<TeamManagerBean> getTeamList(@QueryMap Map<String, Object> map);

        //删除团队成员
        @POST(ApiUrl.TEAM_MEMBER_DELETE)
        Observable<BaseBean> deleteMember(@QueryMap Map<String, Object> map);

        //根据id查询定时事件信息
        @GET(ApiUrl.CRONTAB_FIND_ID)
        Observable<TaskDetailBean> crontabfindById(@Path("id") int id);

        //查找文件名称
        @POST(ApiUrl.CRONTAB_FIND_NAME)
        Observable<FileNameBean> crontabfindName(@QueryMap Map<String, Object> map);

        //保存定时事件
        @POST(ApiUrl.CRONTAB_SAVE)
        Observable<TaskSaveBean> crontabSave(@QueryMap Map<String, Object> map);

        //查询定时事件信息
        @POST(ApiUrl.CRONTAB_LIST)
        Observable<TaskListBean> crontabList(@QueryMap Map<String, Object> map);

        //保存床位信息
        @POST(ApiUrl.CRONTAB_SAVE_BED)
        Observable<BaseBean> crontabSaveBed(@QueryMap Map<String, Object> map);

        //更新定时事件信息
        @POST(ApiUrl.CRONTAB_UPDATE)
        Observable<BaseBean> crontabUpdate(@QueryMap Map<String, Object> map);

        //删除定时事件信息
        @POST(ApiUrl.CRONTAB_DELETE)
        Observable<BaseBean> crontabDelete(@Path("id") int id);

        //删除床位事件
        @POST(ApiUrl.CRONTAB_DELETE_BED)
        Observable<BaseBean> crontabDeleteBed(@QueryMap Map<String, Object> map);

        //床位列表
        @POST(ApiUrl.CRONTAB_BED_LIST)
        Observable<BedListBean> crontabBedList(@QueryMap Map<String, Object> map);

        //床位详情
        @POST(ApiUrl.CRONTAB_BED_LIST)
        Observable<BedDetailBean> crontabBedDetail(@QueryMap Map<String, Object> map);

        //报修问题列表
        @POST(ApiUrl.SYSTEM_DICT_LIST)
        Observable<QuesListBean> questionList(@QueryMap Map<String, Object> map);

        //保存报修问题
        @POST(ApiUrl.REPAIR_SAVE)
        Observable<BaseBean> repairSave(@QueryMap Map<String, Object> map);

        //宣教字典
        @POST(ApiUrl.SYSTEM_DICT_LIST)
        Observable<MissionBean> missionList(@QueryMap Map<String, Object> map);

        //平板状态
        @GET(ApiUrl.REPAIR_STATUS)
        Observable<RepairStatusBean> repairStatus(@QueryMap Map<String, Object> map);
    }

}
