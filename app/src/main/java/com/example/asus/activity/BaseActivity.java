package com.example.asus.activity;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class BaseActivity extends Activity{
    String SAVE_ACTION="save.message";
        @Override
        public  void finish() {
            super.finish();
            ServiceConnection connection=new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            /*if(isTaskRoot()){
                Toast.makeText(this, "已经退出程序", Toast.LENGTH_LONG).show();
                //创建IntentFilter
                IntentFilter filter=new IntentFilter();
                //指定BroadcastReceiver监听的Action
                filter.addAction(SEND_ACTION);
                //注册BroadcaseReceiver
                registerReceiver();
                Intent intent=new Intent();
                intent.setAction(SAVE_ACTION);
                bindService(intent,connection, Service.BIND_AUTO_CREATE);
            }*/
        }

}
