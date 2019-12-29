package com.union.replytax.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.rxbus.RxBus;
import com.hyphenate.easeui.rxbus.RxConsumer;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.PermissionsUtil;
import com.hyphenate.easeui.utils.StatusBarUtils;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.union.replytax.BuildConfig;
import com.union.replytax.MyApplication;
import com.union.replytax.R;
import com.union.replytax.base.BaseActivity;
import com.union.replytax.base.BaseBean;
import com.union.replytax.base.BasePresenter;
import com.union.replytax.constant.Constants;
import com.union.replytax.dialog.DownLoadDialog;
import com.union.replytax.dialog.UpdateDialog;
import com.union.replytax.hxapp.HxMainApp;
import com.union.replytax.rxbus.bean.RxRefreshMessage;
import com.union.replytax.rxbus.bean.RxRefreshUserInfo;
import com.union.replytax.rxbus.bean.RxSeeMore;
import com.union.replytax.rxbus.bean.RxStartLoginActivity;
import com.union.replytax.rxbus.bean.RxUpdateUnReadNum;
import com.union.replytax.service.Net_Service;
import com.union.replytax.ui.Info.HomeFragment;
import com.union.replytax.ui.expert.ExpertFragment;
import com.union.replytax.ui.login.constant.LoginConstant;
import com.union.replytax.ui.login.ui.activity.LoginActivity;
import com.union.replytax.ui.message.ui.fragment.MessageFragment;
import com.union.replytax.ui.mine.MineFragment;
import com.union.replytax.ui.mine.model.UpdateBean;
import com.union.replytax.ui.mine.model.UserBean;
import com.union.replytax.ui.mine.presenter.GetUserInfoPresent;
import com.union.replytax.util.FileSizeUtil;
import com.union.replytax.util.LogFactory;
import com.union.replytax.util.LogUtil;
import com.union.replytax.util.SPUtil;
import com.union.replytax.util.StartActivityUtil;
import com.union.replytax.util.UIHelper;
import com.union.replytax.widget.CallOtherOpeanFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements GetUserInfoPresent.IMineView{
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.tv_unread_mess)
    TextView tvUnreadMess;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private int selectCheckedId = -1;
    private HomeFragment homeFragment;
    private ExpertFragment expertFragment;
    private MessageFragment messageFragment;
    private MineFragment mineFragment;
    private RadioButton firstRadio;
    private GetUserInfoPresent present;
    private UserBean.DataBean user;
    private int selectPosition = 0;
    private static final String TAG = "MainActivity";
    private String savePathString,speed;
    private UpdateDialog updateDialog;
    private DownLoadDialog downloadDialog;
    private InputStream inputStream;
    private URLConnection connection;
    private OutputStream outputStream;
    private Intent sevice;
    private int isForce, fileSize;
    private int DownedFileLength = 0;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            LogFactory.l().e("msg.what---" + msg.what);
            switch (msg.what) {
                case 0:
                    DownLoadDialog.prb_progressbar.setMax(fileSize);
                    break;
                case 1:
                    DownLoadDialog.tv_speed.setText(speed);
                    DownLoadDialog.tv_totle.setText(FileSizeUtil.FormetFileSize(DownedFileLength)
                            + "/"
                            + FileSizeUtil.FormetFileSize(fileSize));
                    DownLoadDialog.prb_progressbar.setProgress(DownedFileLength);
                    break;
                case 2:
                    if (sevice != null) {
                        stopService(sevice);
                    }
//                    finish();
                    autoInstallApk(MainActivity.this,savePathString);
//                    installApk();
                    break;
                default:
                    break;
            }
        }
    };
    private File file;
    private MyReceiver myreceiver;


    @Override
    public BasePresenter getBasePresenter() {
        return present;
    }

    @Override
    public void initBasePresenter() {
        present = new GetUserInfoPresent(this);
    }

    @Override
    public void initData() {
        requestPermission();
        if (SPUtil.getBooleanData(LoginConstant.isLogin)) {
            present.getUserInfo();
        }
        RxBus.getInstance().subscribe(this, RxUpdateUnReadNum.class, new
                RxConsumer<RxUpdateUnReadNum>() {
                    @Override
                    public void handler(RxUpdateUnReadNum updateUnReadNum) {
                        LogUtil.i("updateUnReadNum isFrom:" + updateUnReadNum.isFrom);
                        updateUnReadNum();
                    }
                });
        RxBus.getInstance().subscribe(this, RxRefreshUserInfo.class, new
                RxConsumer<RxRefreshUserInfo>() {
                    @Override
                    public void handler(RxRefreshUserInfo o) {
                        if (o.isRefreshFlag()) {
                            getContextActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    present.getUserInfo();
                                }
                            });
                        }
                    }
                });
        RxBus.getInstance().subscribe(this, RxStartLoginActivity.class, new
                RxConsumer<RxStartLoginActivity>() {
                    @Override
                    public void handler(RxStartLoginActivity o) {
                        showToast(o.getMessage());//Token无效，重新登录
                        StartActivityUtil.startActivity(MainActivity.this, LoginActivity.class);
                        SPUtil.saveData(LoginConstant.isLogin, false);
                        SPUtil.clearToken();
                        SPUtil.clearUserBean();
                    }
                });

        RxBus.getInstance().subscribe(this, RxSeeMore.class, new RxConsumer<RxSeeMore>() {
            @Override
            public void handler(RxSeeMore o) {
                ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
            }
        });

        setEaseUIProviders();
        setEaseSettingProviders();

        present.getVersionInfo();
    }

    private void setEaseSettingProviders() {
        EaseUI.getInstance().setSettingsProvider(new EaseUI.EaseSettingsProvider() {
            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if(!SPUtil.getBooleanData("isNoticeFlag")){
                    return true;
                }else {
                    return false;
                }
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                LogFactory.l().e("pushVoiceFlag==="+SPUtil.getBooleanData("pushVoiceFlag"));
                if(!SPUtil.getBooleanData("pushVoiceFlag")){
                    return true;
                }else {
                    return false;
                }

            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                LogFactory.l().e("ShockNoticeFlag==="+SPUtil.getBooleanData("ShockNoticeFlag"));
                if(!SPUtil.getBooleanData("ShockNoticeFlag")){
                    return true;
                }else {
                    return false;
                }
            }

            @Override
            public boolean isSpeakerOpened() {
                return false;
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = 0;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            index = bundle.getInt("index");
        }
        firstRadio = (RadioButton) radioGroup.getChildAt(index);
        firstRadio.setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void initView() {
        LogFactory.l().e("initView");
        fragmentManager = getSupportFragmentManager();
        StatusBarUtils.setStatusBar(this, true, Color.WHITE);

        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        firstRadio = (RadioButton) radioGroup.getChildAt(0);
        firstRadio.setChecked(true);

//        myreceiver = new MyReceiver();   //显示网速
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.union.replytax.service.Net_Service");
//        registerReceiver(myreceiver, filter);
    }


    private void addShowOrHideFragment(Fragment showFg, Fragment hideFg) {
        FragmentTransaction tran = fragmentManager.beginTransaction();
        if (showFg.isAdded()) {
            tran.show(showFg);
        } else {
            tran.add(R.id.content, showFg);
        }
        if (hideFg != null) {
            tran.hide(hideFg);
        }
        tran.commitAllowingStateLoss();
    }

   /* private long time;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //双击退出
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 2000)) {
                showToast("再按一次返回桌面");
                time = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }*/

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup
            .OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if (selectCheckedId == checkedId) {
                return;
            }
            Fragment showFg = null;
            Fragment hideFg = null;
            switch (selectCheckedId) {
                case R.id.rdobtn_info:
                    selectPosition = 0;
//                    hideFg = infoFragment;
                    hideFg = homeFragment;
                    break;
                case R.id.rdobtn_expert:
                    selectPosition = 1;
                    hideFg = expertFragment;
                    break;
                case R.id.rdobtn_mess:
                    hideFg = messageFragment;
                    break;
                case R.id.rdobtn_mine:
                    selectPosition = 3;
                    hideFg = mineFragment;
                    break;
            }
            switch (checkedId) {
                case R.id.rdobtn_info:
//                    showFg = getInfoFragment();
                    showFg = getHomeFragment();
                    break;
                case R.id.rdobtn_expert:
                    showFg = getExpertFragment();
                    break;
                case R.id.rdobtn_mess:
//                    showFg = getMessageFragment();
                    if (!SPUtil.getBooleanData(LoginConstant.isLogin)) {
                        StartActivityUtil.startActivity(MainActivity.this, LoginActivity.class, 0);
                        return;
                    } else {
                        showFg = getMessageFragment();
                    }
                    break;
                case R.id.rdobtn_mine:
                    showFg = getMineFragment();
                    break;
            }
            selectCheckedId = checkedId;
            addShowOrHideFragment(showFg, hideFg);
        }
    };

    private HomeFragment getHomeFragment() {
        if (homeFragment == null) {
            homeFragment = HomeFragment.getInstance();
        }
        return homeFragment;
    }


    private ExpertFragment getExpertFragment() {
        if (expertFragment == null) {
            expertFragment = new ExpertFragment();
        }
        return expertFragment;
    }

    private MessageFragment getMessageFragment() {
        if (messageFragment == null) {
            messageFragment = MessageFragment.getInstance();
        }
        return messageFragment;
    }

    private MineFragment getMineFragment() {
        if (mineFragment == null) {
            mineFragment = MineFragment.getInstance();
        }
        return mineFragment;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
                break;
            case 1:
                ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
                break;
            case 3:
                ((RadioButton) radioGroup.getChildAt(3)).setChecked(true);
                break;

        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            RxPermissions.getInstance(this).requestEach(Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
                            .READ_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Permission>() {
                        @Override
                        public void call(Permission permission) {
                            if (permission.name.equals(Manifest.permission.READ_PHONE_STATE)) {
                                if (!permission.granted) {
                                    PermissionsUtil.createLoadedAlertDialog(MainActivity.this,
                                            "在设置-应用-" + getString(R.string.app_name) +
                                                    "-权限中开启手机状态权限，以正常使用App功能", false);
                                } else {
//                                    MyApplication.getInstance().initUmPush();
                                }
                            } else if (permission.name.equals(Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE) || permission.name.equals(Manifest
                                    .permission
                                    .READ_EXTERNAL_STORAGE)) {
                                if (!permission.granted) {
                                    PermissionsUtil.createLoadedAlertDialog(MainActivity.this,
                                            "在设置-应用-" + getString(R.string.app_name) +
                                                    "-权限中开启读写存储卡权限，以正常使用App功能", false);
                                }
                            }
                        }
                    });
        }
    }

    public void updateUnReadNum() {
        final int num = HxMainApp.getInstance().getAllUnReadMessage() + MyApplication.getInstance
                ().getMessageNum();
        LogFactory.e("updateUnReadNum HxMainApp.isConnected:" + HxMainApp.getInstance()
                .isConnected());
        LogFactory.e("updateUnReadNum getMessageNum:" + MyApplication.getInstance().getMessageNum
                ());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MyApplication.getInstance().getMessageNum() > 0 || HxMainApp.getInstance()
                        .getAllUnReadMessage() > 0) {
                    LogFactory.e("updateUnReadNum 222 totalNum:" + num);
                    tvUnreadMess.setText("" + num);
                    tvUnreadMess.setVisibility(View.VISIBLE);
                } else {
                    tvUnreadMess.setText("");
                    tvUnreadMess.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    public void setEaseUIProviders() {

        /**
         * 设置通知消息提供者
         */
        EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotifier
                .EaseNotificationInfoProvider() {
            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = EaseCommonUtils.getMessageDigest(message, MainActivity.this);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                if (message.getFrom().equals(Constants.IM_NUMBER)) {
                    return "客服: " + ticker;
                } else {
                    return "你有新消息: " + ": " + ticker;
                }
            }

            /**
             * 根据消息条数来判断如果显示
             * @param message
             *            接收到的消息
             * @param fromUsersNum
             *            发送人的数量
             * @param messageNum
             *            消息数量
             * @return
             */
            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // 当只有一个人，发来一条消息时，显示消息内容 TODO 表情符显示为图片
                LogFactory.l().e("message.getMsgId===" + message.getMsgId());
                if (messageNum == 1) {
                    if (!message.getFrom().equals(Constants.IM_NUMBER)) {
                        return EaseCommonUtils.getMessageDigest(message, MainActivity.this)
                                .replace("\\[.{2,3}\\]", "[表情]");
                    } else {
                        return "客服：" + EaseCommonUtils.getMessageDigest(message, MainActivity.this)
                                .replace("\\[.{2,3}\\]", "[表情]");
                    }
                } else {
                    return fromUsersNum + " 个联系人，发来 " + messageNum + " 条消息";
                }
            }

            @Override
            public String getTitle(EMMessage message) {
                return null;
            }

            /**
             * 设置通知栏小图标，规定要求大小为24dp
             * @param message
             * @return
             */
            @Override
            public int getSmallIcon(EMMessage message) {
                return R.mipmap.ic_launcher_round;
            }

            /**
             * 通知栏点击跳转设置，这里因为只有客服，所以没有其他判断，直接跳转到聊天界面，
             * 把客服username传递过去即可
             * @param message
             * 显示在notification上最近的一条消息
             * @return
             */
            @Override
            public Intent getLaunchIntent(EMMessage message) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("index", 2);
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, MainActivity.class);
                return intent;
            }
        });
    }

    private EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //收到消息体的撤回回调，消息体已经成功撤回。
        }

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            LogFactory.l().e("答税app新消息来了-----" + TAG);
            //收到消息
            for (final EMMessage message : messages) {
                LogFactory.l().e("msg:" + message.getUserName() + "=>" + message.getBody().toString() + "==>" + message.getMsgId());
                String json = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_IS_AGREE_CONSULT_EXPRESSION, null);
                if (json != null) {
                    LogFactory.l().e("json---" + json);
                }
                if (!EaseUI.getInstance().hasForegroundActivies()) {
                    EaseUI.getInstance().getNotifier().notify(messages);
                }
            }
            getContextActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RxBus.getInstance().post(new RxUpdateUnReadNum(TAG + "--》onMessageReceived"));
                }
            });


        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            for (EMMessage message : messages) {
                LogFactory.l().e("收到已读回执" + message.getUserName() + "=>" + message.getBody()
                        .toString());
            }
        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
            //收到已送达回执
            for (EMMessage message : messages) {
                LogFactory.l().e("收到已送达回执" + message.getUserName() + "=>" + message.getBody()
                        .toString());
            }
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            LogFactory.l().e("消息状态变动" + message.getUserName() + "=>" + message.getBody().toString
                    ());
        }
    };

    @Override
    public void success(UserBean userBean) {
        user = userBean.getData();
        SPUtil.saveUserBean(user);
        MyApplication.getInstance().loginEM();
        RxBus.getInstance().post(new RxRefreshMessage());
    }

    @Override
    public void getVersinInfo(UpdateBean updateBean) {
        UpdateBean.DataBean dataBean = updateBean.getData();
        if (Integer.valueOf(dataBean.getVersionCode())>UIHelper.getVersionCode(this)){
            String versionCode = dataBean.getVersionCode();
            String versionName = dataBean.getVersionName();
            String downloadUrl = dataBean.getDownloadUrl();
            String desc = dataBean.getDesc();
            boolean isForce = dataBean.isIsForce();
            fileSize = (int) dataBean.getFileSize();
            update(this,downloadUrl,desc,versionName,fileSize,true);
        }
    }

    @Override
    public void getUserInfoFail() {

    }

    @Override
    public void getSignSuccess(BaseBean baseBean) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().onDestroy();
    }



    private void update(final Context context, final String url, final String prompt, final String verName, final int fileSize, final boolean isCancelable) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePathString = Environment.getExternalStorageDirectory().getAbsolutePath() + "/replayTax/" + "ReplayTax.apk";
            LogFactory.e("savePathString---" + savePathString);
            if (FileSizeUtil.getAutoFileOrFilesSize1(savePathString) == fileSize / 1024
                    && FileSizeUtil.getVersionNameFromApk(context, savePathString).equals(verName)) {
                updateDialog = new UpdateDialog(1, context, "立即安装", prompt, verName, fileSize, new UpdateDialog.SureButtonClick() {
                    @Override
                    public void onSureButtonClick() {
                        autoInstallApk(MainActivity.this,savePathString);
                    }
                });
            } else {
                updateDialog = new UpdateDialog(0, context, "立即更新", prompt, verName, fileSize, new UpdateDialog.SureButtonClick() {
                    @Override
                    public void onSureButtonClick() {
                        showDoladDialog(context, url, fileSize);
                    }
                });
            }
            updateDialog.setCancelable(isCancelable);
            updateDialog.show();
        }
    }


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.union.replytax.service.Net_Service")) {
                Bundle bundle = intent.getExtras();
                speed = bundle.getString("speed");
            }
        }
    }

    private void autoInstallApk(Context context,String path) {
        LogFactory.l().e("path==="+path);

        File file = new File(path);
        CallOtherOpeanFile.openFile(MainActivity.this,file);
    }

    private void showDoladDialog(Context context, final String url, final int fileSize) {
        downloadDialog = new DownLoadDialog(context);
        downloadDialog.show();
        Thread thread = new Thread() {
            public void run() {
                try {
                    DownFile(url, fileSize);
                } catch (Exception e) {
                }
            }
        };
        thread.start();
    }

    private void DownFile(String urlString, int fileSize) {
        /*
         * 连接到服务器
         */
        sevice = new Intent(MainActivity.this, Net_Service.class);
        startService(sevice);
        try {
            URL url = new URL(urlString);
            connection = url.openConnection();
            inputStream = connection.getInputStream();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * 文件的保存路径和和文件名其中attendance.apk是在手机上要保存的路径，如果不存在则新建
         */
        String savePAth = Environment.getExternalStorageDirectory().getAbsolutePath() + "/replayTax";
        File file1 = new File(savePAth);
        if (!file1.exists()) {
            file1.mkdir();
        }
        savePathString = Environment.getExternalStorageDirectory().getAbsolutePath() + "/replayTax/" + "ReplayTax.apk";
        file = new File(savePathString);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
         * 向SD卡中写入文件,用Handle传递线程
         */
        Message message = new Message();
        try {
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 4];
            int len = -1;
            message.what = 0;
            handler.sendMessage(message);
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                DownedFileLength += len;

                Message message1 = new Message();
                message1.what = 1;
                handler.sendMessage(message1);
            }
            Message message2 = new Message();
            message2.what = 2;
            handler.sendMessage(message2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
