package com.example.asus.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.asus.broadreceiver.AddFriendReceiver;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageType;
import com.example.asus.fragment.NewsFragment;
import com.example.asus.he.R;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class NotificationUtil {

    public static void showHangNotification(Message message,Context context){
        /*NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        *//*Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/itachi85/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
        builder.setContentIntent(pendingIntent);*//*
        builder.setContentText(message.getSender_id()+"请求添加好友");
//        builder.setSmallIcon();
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.label));
        builder.setAutoCancel(true);
        builder.setContentTitle("悬挂式通知");
        //设置点击跳转
        Intent hangIntent = new Intent();
        hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        hangIntent.setClass(context, AddFriendReceiver.class);
        hangIntent.putExtra("message",message);
        //如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
//        PendingIntent hangPendingIntent = PendingIntent.getActivity(context, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent hangPendingIntent = PendingIntent.getBroadcast(context,0,hangIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setFullScreenIntent(hangPendingIntent, true);
        notificationManager.notify(2, builder.build());*/
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setContentTitle("请求添加好友");// 设置通知栏标题

        mBuilder.setSmallIcon(R.drawable.label);// 设置通知小ICON

        mBuilder.setContentText(message.getSender_id() + "请求添加好友");

        mBuilder.setTicker(message.getSender_id()); // 通知首次出现在通知栏，带上升动画效果的

        mBuilder.setWhen(System.currentTimeMillis());// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        Intent intent=new Intent("add.friend.message");
        intent.putExtra("message",message);
        message.setType(MessageType.NOTIFICATION);
        PendingIntent hangPendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentIntent(hangPendingIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS);

        Notification notification = mBuilder.build();//API 16

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, notification);
    }
}
