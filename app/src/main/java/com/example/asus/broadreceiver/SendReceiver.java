package com.example.asus.broadreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class SendReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context,Intent intent){
       /* Log.e("SendReceiver","接收到广播");
        Bundle bundle=intent.getExtras();
        Intent intent1=new Intent(context, SendService.class);
        intent1.putExtras(bundle);
//        启动LoginService
        context.startService(intent1);*/
    }
}
