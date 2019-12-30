package com.union.replytax.ui.login.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hyphenate.easeui.rxbus.RxBus;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.union.replytax.MyApplication;
import com.union.replytax.R;
import com.union.replytax.base.BaseActivity;
import com.union.replytax.base.BaseBean;
import com.union.replytax.base.BasePresenter;
import com.union.replytax.constant.Constants;
import com.union.replytax.rxbus.bean.RxRefreshUserInfo;
import com.union.replytax.ui.login.bean.QQUser;
import com.union.replytax.ui.login.bean.TokenBean;
import com.union.replytax.ui.login.constant.LoginConstant;
import com.union.replytax.ui.login.presenter.LoginPresent;
import com.union.replytax.util.ClickTimeUtils;
import com.union.replytax.util.LogFactory;
import com.union.replytax.util.SPUtil;
import com.union.replytax.util.StartActivityUtil;
import com.union.replytax.util.StringUtils;
import com.union.replytax.util.ToastUtil;
import com.union.replytax.widget.ClearEditText;
import com.union.replytax.widget.EasyIndicator;
import com.union.replytax.widget.PwdCheckUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

//登录
public class LoginActivity extends BaseActivity implements LoginPresent.ILoginView {
    @BindView(R.id.easy_indicator)
    EasyIndicator easyIndicator;
    @BindView(R.id.et_phone)
    ClearEditText etPhone;
    @BindView(R.id.edtTxt_pwd)
    ClearEditText edtTxtPwd;
    @BindView(R.id.iv_showpwd)
    ImageView ivShowpwd;
    @BindView(R.id.rv_pwd)
    RelativeLayout rvPwd;
    @BindView(R.id.edtTxt_code)
    ClearEditText edtTxtCode;
    @BindView(R.id.tv_getcode)
    TextView tvGetcode;
    @BindView(R.id.rv_phone)
    RelativeLayout rvPhone;
    @BindView(R.id.tv_forgotpwd)
    TextView tvForgotpwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_create_account)
    TextView tvCreateAccount;
    @BindView(R.id.iv_wechat_login)
    ImageView ivWechatLogin;
    @BindView(R.id.iv_qq_login)
    ImageView ivQqLogin;
    @BindView(R.id.iv_weibo_login)
    ImageView ivWeiboLogin;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.rel_login)
    RelativeLayout relLogin;
    private LoginPresent present;
    private MyCount countDown;
    private boolean isShowPwd = false;
    private int loginType = 0;
    private String phoneNum = "";
    private SsoHandler mSsoHandler; //微博登录
    private Oauth2AccessToken mAccessToken;
    public static Tencent mTencent;
    private static boolean isServerSideLogin = false;
    private int state = -1;
    private String avatar="";
    private String openId="";
    private String memberName="";
    private int gender=0;
    private UserInfo userInfo;
    private int quickLoginType=0;
    @Override
    public BasePresenter getBasePresenter() {
        return present;
    }

    @Override
    public void initBasePresenter() {
        present = new LoginPresent(this);
    }


    @Override
    public void initData() {
        countDown = new MyCount(60 * 1000, 1000);
    }


    @Override
    public void initView() {
        setTopPadding(relLogin);
        state = getIntent().getIntExtra("state", 0);
        easyIndicator.setTabTitles(new String[]{getResources().getString(R.string
                .account_password_login),
                getResources().getString(R.string.sms_shortcut_login)});
        easyIndicator.setOnTabClickListener(onTabClickListener);

        mSsoHandler = new SsoHandler(this);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if(mTencent==null)
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this);
    }

    EasyIndicator.onTabClickListener onTabClickListener = new EasyIndicator.onTabClickListener() {
        @Override
        public void onTabClick(String title, int position) {
            LogFactory.l().e(title + "::" + position);
            if (position == 0) {
                loginType = 0;
                rvPwd.setVisibility(View.VISIBLE);
                rvPhone.setVisibility(View.GONE);
            } else {
                loginType = 1;
                rvPhone.setVisibility(View.VISIBLE);
                rvPwd.setVisibility(View.GONE);
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    public void success(TokenBean tokenBean) {
        //设置成功去登录
        SPUtil.saveData(LoginConstant.isLogin, true);
        SPUtil.saveToken(tokenBean.getData().getToken());
        RxBus.getInstance().post(new RxRefreshUserInfo(true));

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setVerificationCode(BaseBean baseBean) {
        if (!baseBean.isSuccess()) {  //中断
            countDown.cancel();
            initGetSmsText();
        }
        if (!baseBean.isSuccess()) {  //设置成功去登录
            showToast(baseBean.getMessage());
        }
    }

    @Override
    public void quickLoginSuccess(TokenBean baseBean) {
        //快捷登录成功
        if(baseBean.isSuccess()){
            SPUtil.saveData(LoginConstant.isLogin, true);
            SPUtil.saveToken(baseBean.getData().getToken());
            RxBus.getInstance().post(new RxRefreshUserInfo(true));
            setResult(RESULT_OK);
            finish();
        }else if(baseBean.getCode()==615){
            Bundle bundle=new Bundle();
            bundle.putString("openId",openId);
            bundle.putString("avatar",avatar);
            bundle.putInt("gender",gender);
            bundle.putInt("type",quickLoginType);
            bundle.putString("memberName",memberName);
            StartActivityUtil.startActivity(this, BindPhoneActivity.class,bundle);
            finish();
        }else {
            showToast(baseBean.getMessage());
        }

    }

    @OnClick({R.id.iv_showpwd, R.id.tv_getcode, R.id.tv_forgotpwd, R.id.btn_login, R.id.iv_close,
            R.id.tv_create_account, R.id.iv_wechat_login, R.id.iv_qq_login, R.id.iv_weibo_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_showpwd:
                hideOrShowPwd();
                break;
            case R.id.tv_getcode:   // 获取验证码
                sendVerCode();
                break;
            case R.id.tv_forgotpwd: //忘记密码
                StartActivityUtil.startActivity(this, ForgotPwdActivity.class);
                break;
            case R.id.btn_login:
                doLogin();
                break;
            case R.id.tv_create_account:    //创建账号
                StartActivityUtil.startActivity(this, RegisterActivity.class);
                finish();
                break;
            case R.id.iv_wechat_login:
                if(ClickTimeUtils.isFastDoubleClick())
                    return;
                quickLoginType=0;
                onWeChatLogin();
                break;
            case R.id.iv_qq_login:  //qq登录
                if(ClickTimeUtils.isFastDoubleClick())
                    return;
                quickLoginType=1;
                onQQclickLogin();
                break;
            case R.id.iv_weibo_login:   //新浪微博登录
                if(ClickTimeUtils.isFastDoubleClick())
                    return;
                quickLoginType=2;
                mSsoHandler.authorize(new selfWbAuthListener());
                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }


    private void hideOrShowPwd() {
        if (isShowPwd) {  //隐藏密码
            ivShowpwd.setImageResource(R.drawable.login_hide);
            isShowPwd = false;
            edtTxtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType
                    .TYPE_TEXT_VARIATION_PASSWORD);
        } else { //显示密码
            ivShowpwd.setImageResource(R.drawable.login_show);
            isShowPwd = true;
            edtTxtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    private void doLogin() {
        phoneNum = etPhone.getText().toString().trim();
        String password = edtTxtPwd.getText().toString().trim();
        if (phoneNum.equals("")) {
            ToastUtil.showToast(context, getResources().getString(R.string.phone_num_null));
            return;
        }
        if (!(phoneNum.length() == 11 && StringUtils.isMobile(phoneNum))) {
            showToast(getResources().getString(R.string.incorrect_phone_num));
            return;
        }
        if (loginType == 0) {   //账号密码登录
            if (password.equals("")) {
                showToast("登录密码不能为空");
                return;
            }
            if(password.length()<6){
                showToast("密码不能少于6位");
                return;
            }
            if(!PwdCheckUtil.isContainAll(password)){
                showToast("密码格式错误，请输入6-16位英文和数字组合的密码！");
                return;
            }
            present.doLogin(loginType, phoneNum, password);
        } else if (loginType == 1) {             //短信验证码登录
            if (edtTxtCode.getText().toString().trim().equals("")) {
                showToast("验证码不能为空");
                return;
            }
            present.doLogin(loginType, phoneNum, edtTxtCode.getText().toString().trim());
        }

    }

    private void sendVerCode() {
        String phoneNum = etPhone.getText().toString().trim();
        if (phoneNum.length() == 11 && StringUtils.isMobile(phoneNum)) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", LoginConstant.LOGIN_TYPE);
            map.put("phone", phoneNum);
            present.getVerCode(map);
            tvGetcode.setEnabled(false);
            tvGetcode.setText("60" + getResources().getString(R.string.retry));
            countDown.start();
            edtTxtCode.requestFocus();
            edtTxtCode.findFocus();
        } else {
            showToast(getResources().getString(R.string.incorrect_phone_num));
        }
    }


    private class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetcode.setText("" + (millisUntilFinished / 1000) + getResources().getString(R
                    .string.retry));
        }

        @Override
        public void onFinish() {
            initGetSmsText();
            this.cancel();
        }
    }

    private void initGetSmsText() {
        tvGetcode.setEnabled(true);
        tvGetcode.setText(getResources().getString(R.string.get_validation_code));
    }


    //微博登录
    private class selfWbAuthListener implements WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken oauth2AccessToken) {
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken=oauth2AccessToken;
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                        OkHttpUtils.get()
                                .url("https://api.weibo.com/2/users/show.json")
                                .addParams("access_token",mAccessToken.getToken())
                                .addParams("uid",mAccessToken.getUid())
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        LogFactory.l().e("获取失败："+e.getMessage());
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        LogFactory.l().e("response:"+response);

                                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                                        avatar = jsonObject.getString("avatar_large");
                                        if(jsonObject.getString("gender").equals("f")){
                                            gender=0;
                                        }else {
                                            gender=1;
                                        }
                                        memberName=jsonObject.getString("screen_name");
                                        openId=jsonObject.getString("idstr");
                                        present.quickLogin(2,openId);
                                    }
                                });
                    }
                }
            });
        }

        @Override
        public void cancel() {
            showToast("取消微博登录");
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            showToast(wbConnectErrorMessage.getErrorMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogFactory.l().e("resultCode---"+requestCode);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        Tencent.onActivityResultData(requestCode,resultCode,data,new BaseUiListener());
    }

    //qq登录
    private void onQQclickLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", listener);
            isServerSideLogin = false;
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(this);
                mTencent.login(this, "all", listener);
                isServerSideLogin = false;
                return;
            }
            mTencent.logout(this);
        }
    }

    private BaseUiListener listener = new BaseUiListener();
    class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
//            ViseLog.d("授权:"+o.toString());
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(o.toString());
                initOpenidAndToken(jsonObject);
                updateUserInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError e) {

        }
        @Override
        public void onCancel() {

        }
    }

    /**
     * 获取登录QQ腾讯平台的权限信息(用于访问QQ用户信息)
     * @param jsonObject
     */
    public void initOpenidAndToken(org.json.JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
            String qqopenId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(qqopenId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(qqopenId);
                openId=qqopenId;
            }
        } catch(Exception e) {
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }
                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
//                    ViseLog.e("................"+response.toString());
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
                @Override
                public void onCancel() {
                   //登录取消
                }
            };
            userInfo = new UserInfo(this, mTencent.getQQToken());
            userInfo.getUserInfo(listener);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                com.alibaba.fastjson.JSONObject response = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(msg.obj));
               LogFactory.l().e("UserInfo:"+ JSON.toJSONString(response));
                QQUser user= com.alibaba.fastjson.JSONObject.parseObject(response.toJSONString(),QQUser.class);
                if (user!=null) {
                    if(user.getGender().equals("男")){
                        gender=1;
                    }else {
                        gender=0;
                    }
                    avatar=user.getFigureurl_qq_2();
                    memberName=user.getNickname();
                    present.quickLogin(1,openId);
                }
            }
        }
    };



    //微信登录
    private void onWeChatLogin() {
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            showToast("请先安装微信");
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        boolean sendReq = MyApplication.mWxApi.sendReq(req);
        LogFactory.l().e("secdReq---"+sendReq);
        SPUtil.saveData("isFromLogin",true);
//        setResult(RESULT_OK);
        finish();
    }
}
