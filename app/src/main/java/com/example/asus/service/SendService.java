package com.example.asus.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.asus.client.ClientThread;
import com.example.asus.client.entity.User;
import com.example.asus.util.HttpUtil;

import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class SendService extends IntentService{
   /* 报出错误：
Unable to instantiate service com.example.asus.service.SendService:
    android.os.NetworkOnMainThreadException
    说明IntentService只是个线程，还是依赖于activity进程*/
   //    定义Socket
   private Socket socket=null;
    //    定义Socket输出流
    private ObjectOutputStream writer;
    //    定义该客户端的用户
    private User user;
    public SendService(){
        super("SendService");
        Log.e("SendService","成功开启SendService后台服务");
    }
    @Override
    protected void onHandleIntent(Intent intent){
        user=(User) intent.getSerializableExtra("user");
        try {
            socket = new Socket(HttpUtil.SERVER_URL, 10000);
            writer = new ObjectOutputStream(socket.getOutputStream());
            writer.writeObject(user);
            new ClientThread(socket,null).start();
        }catch (Exception e1){
            Log.e("IOException", e1.getMessage());
        }
    }
}
