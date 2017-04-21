package com.example.asus.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.example.asus.R;

import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageType;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class NotificationUtil {

    public static void showHangNotification(Message message,Context context){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        switch (message.getType()){
            case MessageType.RECEIVE_FRIEND:
                mBuilder.setContentTitle("请求添加好友");// 设置通知栏标题
                mBuilder.setContentText(message.getSender_id() + "请求添加好友");
                message.setType(MessageType.NOTIFICATION);
//                将好友请求保存在本地，如果下次和好友列表有冲突，则不显示，没有冲突则显示
                CacUtil.cacheSave("requestFriend",context,message);
                break;
            case MessageType.ACCEPT_FRIEND:
                mBuilder.setContentTitle("同意");// 设置通知栏标题
                mBuilder.setContentText(message.getSender_id() + "同意你的好友请求");
                message.setType(MessageType.ACCEPT_NOTIFICATION);
                break;
            case MessageType.COM_MES:case MessageType.PICTURE_MESSAGE:case MessageType.VOICE_MESSAGE:
                mBuilder.setContentTitle("新消息");
                mBuilder.setContentText(message.getSender_id() + "给你发送了新消息，请及时查看");
                message.setType(MessageType.NOTIFICATION_LM);
                break;
            case MessageType.HELP_MESSAGE:
                mBuilder.setContentTitle("新消息");
                mBuilder.setContentText(message.getSender_id() + "请求给予你帮助，点击查看详情");
                message.setType(MessageType.NOTIFICATION_HELP);
                break;
            case MessageType.TOGETHER_MESSAGE:
                mBuilder.setContentTitle("新消息");
                mBuilder.setContentText(message.getSender_id() + "请求参与‘约’活动，点击查看详情");
                message.setType(MessageType.NOTIFICATION_TOGETHER);
                break;

        }
        mBuilder.setSmallIcon(R.drawable.label);// 设置通知小ICON
        mBuilder.setTicker(message.getSender_id()); // 通知首次出现在通知栏，带上升动画效果的
        mBuilder.setWhen(System.currentTimeMillis());// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        Intent intent=new Intent("add.friend.message");
        intent.putExtra("message",message);
        PendingIntent hangPendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(hangPendingIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS);
        Notification notification = mBuilder.build();//API 16
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
    }
}
