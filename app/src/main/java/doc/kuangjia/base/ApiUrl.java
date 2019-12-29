package com.zkys.pad.launcher.base;

/**
 * Created by Administrator on 2018/9/5.
 */

public class ApiUrl {
//    public static String BASE_URL = "http://192.168.1.200:8087";
//    public static String BASE_URL = "http://hu.test.zgzkys.com";
    public static String BASE_URL = "http://hu.zgzkys.com";
    public static String PIC_URL = "http://qiniuimage.zgzkys.com/";
//        public static String BASE_URL="http://192.168.1.35:8181/";


    //短信接口
    public static final String POST_SEND_VERIFYCODE = "/sms/verifyCode";   //短信发送接口

    public static final String POST_SAVE_WORKER = "/worker/save";   //保存医护人员信息

    public static final String WORKER_LOGIN = "/worker/login";   //登录

    public static final String GET_TOKEN = "/qn/cloud/getToken";   //获取七牛云token

    public static final String WORK_UPDATE = "/worker/update";   //更新用户信息

    public static final String TEAM_LIST = "/team/{workerId}/teams";   //获取团队信息

    public static final String HOSPITAL_LIST = "/hospital/list";   //获取医院列表

    public static final String GET_DEPERTMENT_LIST = "/system/dept/deptList";//获取科室列表

    public static final String WORKER_MOBILE = "/worker/mobile";//添加团队成员

    public static final String TEAM_SAVE = "/team/save";//创建团队

    public static final String TEAM_AUTHENTICATION = "/teamAuthenticate/save";//提交团队申请认证

    public static final String TEAM_DELETE = "/team/delete";//删除团队

    public static final String TEAMMEMBER_SAVE = "/teamMember/save";//添加团队成员

    public static final String HOME_MSG = "/hu/homeMsg/get";//首页消息

    public static final String GET_BED_LIST = "/deviceBinding/list";//获取床位列表

    public static final String SYSTEM_DICT_LIST = "/system/dict/dictList";//字典列表

    public static final String IN_OUT_HOSPITAL = "/hospitalBedTabb/newSave";//入院、出院

    public static final String MISSION_LIST = "/hospitalPublicize/list";//宣教列表

    public static final String J_PUSH_SEND = "/jpush/customPush";//发送极光推送

    public static final String SWITCH_STATUS = "/bed/tabb/switch/state";//查询床头卡状态模式

    public static final String SWITCH_STATUS_OPEN = "/bed/tabb/switch/open";//打开床头卡

    public static final String SWITCH_STATUS_CLOSE = "/bed/tabb/switch/close";//关闭床头卡

    public static final String SURVEY_STASFACTION_LIST = "/survey/list";//满意度列表

    public static final String SURVEY_DETAIL = "/survey/{id}/data";//满意度详情

    public static final String J_PUSH_SEND_RECORD = "/pushTabb/list";//极光推送记录

    public static final String GET_ANSWER_LIST = "/surveyStis/answerList/{surveyId}/{pusherId}";//调查表回答信息

    public static final String GET_ANSWER_STIS = "/surveyStis/answerStis/{surveyId}";//调查表回答统计

    public static final String POST_PUSH_STIS = "/surveyStis/pushStis";//调查表推送统计

    public static final String TEAM_MEMBER_LIST = "/teamMember/list";//获取团队成员列表

    public static final String TEAM_MEMBER_DELETE = "/teamMember/delete";//删除成员

    public static final String CRONTAB_FIND_ID = "/crontab/find/{id}";//根据Id查询定时事件信息

    public static final String CRONTAB_FIND_NAME = "/crontab/findFileName";//查找文件名称

    public static final String CRONTAB_SAVE = "/crontab/save";//保存定时事件信息

    public static final String CRONTAB_LIST = "/crontab/list";//查询定时事件信息

    public static final String CRONTAB_SAVE_BED = "/crontab/saveBed";//保存床位信息

    public static final String CRONTAB_UPDATE = "/crontab/update";//更新定时事件信息

    public static final String CRONTAB_DELETE = "/crontab/delete/{id}";//删除定时事件信息

    public static final String CRONTAB_BED_LIST = "/crontab/bedList";//床位列表

    public static final String CRONTAB_DELETE_BED = "/crontab/deleteCrontabBed";//删除床位事件

    public static final String REPAIR_QUESTION_LIST = "/repair/questionList";//报修问题列表

    public static final String REPAIR_LIST = "/repair/list";//报修列表

    public static final String REPAIR_SAVE = "/repair/save";//一键报修保存

    public static final String REPAIR_STATUS = "/padStatus/padOrderStatus";//平板状态

























    public static final String USER_AGREEMENT_DETAIL="http://taocan.zgzkys.com/#/protocol"; //用户协议


}
