package com.example.asus.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.example.asus.activity.LoginActivity;
import com.example.asus.activity.MainActivity;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class SaveMessage extends Service{
    //定义广播接收器
    MyReceiver serviceReceiver;
    //该服务用于保存Android对个人信息的更改，并将此更改返回给服务器，时机在整个应用结束之后
    //必须实现的方法
    //定义onBind方法所返回的对象
    private MyBinder binder=new MyBinder();
    //通过继承Binder类来实现IBinder类
    public class MyBinder extends Binder {

    }
    //接收成功登录后服务器传来的数据，并进行相应的处理
    //必须实现的方法,绑定该service时回调该方法
    @Override
    public IBinder onBind(Intent arg0){
        return  null;
    }
    //Service被创建时回调该方法
    @Override
    public void onCreate(){
        super.onCreate();
        //获取只能被本程序读写的sharedPreferences对象
        SharedPreferences preferences=getSharedPreferences("user_message",MODE_PRIVATE);
        if (preferences.contains("user_id")&&preferences.contains("password")){
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            stopSelf();
        }
        else{
            Intent intent=new Intent(this, LoginActivity.class);
            startActivity(intent);
            stopSelf();
        }
    }
    //service被断开连接时回调该方法
    @Override
    public boolean onUnbind(Intent intent){
        super.onUnbind(intent);
        return true;
    }
    //service被关闭时回调该方法
    @Override
    public void onDestroy(){
        super.onDestroy();

    }
}
class MyReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context,Intent intent){

    }
}