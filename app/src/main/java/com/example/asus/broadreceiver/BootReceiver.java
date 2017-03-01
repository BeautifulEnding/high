package com.example.asus.broadreceiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.asus.client.entity.User;
import com.example.asus.service.BootService;
import com.example.asus.service.SendService;
import com.example.asus.util.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25 0025.
 */

public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
       if (!isServiceRunning(context,"com.example.asus.service.BootService")){
           Log.e("BootReceiver","接收到广播,正在开启BootService");
           Intent intent1=new Intent(context, BootService.class);
           intent1.putExtra("user",(User)intent.getSerializableExtra("user"));
//        启动Service
           context.startService(intent1);
       }
    }

    public static boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
