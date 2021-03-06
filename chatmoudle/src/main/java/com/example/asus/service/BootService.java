package com.example.asus.service;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.example.asus.client.Client;
import com.example.asus.client.ManageClientThread;
import com.example.asus.client.entity.User;
import com.example.asus.util.LogUtil;
import com.example.asus.util.SDCardUtil;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**启动high则会开启该服务
 *
 */
public class BootService extends Service {
    //    定义Socket
    public Socket socket;
    //    定义Socket输出流
    private ObjectOutputStream writer;
    //    定义该客户端的用户
    private User user;
    private Client client;
    private Handler handler;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
//        LogUtil.e("BootService当前进程名---------->"+AndroidUtil.getCurProcessName(this));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        if (intent.hasExtra("user")){
//            user=intent.getParcelableExtra("user");
            user=(User) intent.getSerializableExtra("user");
        }else{
            user= SDCardUtil.cacheLoad("selfMessage",this);
        }
        if (user!=null){
            client=new Client(user,this);
            client.start();
            ManageClientThread.addClientConServerThread(user,client);
        }else{
            LogUtil.e("BootService user为空");
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        LogUtil.e("BootService正在销毁------------------");
        Intent sevice = new Intent(this, BootService.class);
        sevice.putExtra("user",user);
        this.startService(sevice);
        super.onDestroy();

    }

    public Handler getHandler(){
        return this.handler;
    }
}
