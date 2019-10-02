package com.soft.logtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int pageNum=0;
    private boolean isStop=false;
    private Button btn,btn1;
    private TextView textView;
    private StringBuffer sb;
    private ArrayList<String> list=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    handler.removeMessages(0);
                    try {
                        String str=TxtUtil.readInternal(pageNum,"debug.log");
                        sb.append(str);
                        textView.setText(sb.toString());
                        pageNum++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessageDelayed(0,1000);
                    break;
                case 1:
                    if(!isStop && pageNum<10){
                        handler.removeMessages(1);
                        for (int i = pageNum*100; i <pageNum*100+100; i++) {
                            Log.e("mpk","logtest=========="+i);
                        }
                        pageNum++;
                        handler.sendEmptyMessageDelayed(1,1000);
                    }
                    break;
                case 2:
                    FileUtil fileUtil=new FileUtil("logtest.log");
                    new Thread(fileUtil).start();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        sb = new StringBuffer("");
        btn = findViewById(R.id.btn);
        btn1 = findViewById(R.id.btn1);
        textView = findViewById(R.id.text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStop=true;
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();


            handler.sendEmptyMessage(1);
            handler.sendEmptyMessageDelayed(2,1000);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent message) {
        this.list=message.list;
        System.out.print("list长度==="+list.size()+"\n");
    }
}
