package com.example.asus.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import com.example.asus.activity.MainActivity;
import com.example.asus.he.R;
import com.example.asus.util.LogUtil;
import com.example.asus.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.io.File;


/**
 * Created by wenmingvs on 16/5/8.
 */
public class PostService extends Service {


    private Context mContext;
    private NotificationManager mSendNotifity;

    public String mPostType;

    public static final String POST_SERVICE_REPOST_STATUS = "转发微博";
    public static final String POST_SERVICE_CREATE_WEIBO = "发微博";
    public static final String POST_SERVICE_COMMENT_STATUS = "评论微博";
    public static final String POST_SERVICE_REPLY_COMMENT = "回复评论";


    /**
     * 微博发送成功
     */
    private static final int SEND_STATUS_SUCCESS = 1;
    /**
     * 微博发送失败
     */
    private static final int SEND_STATUS_ERROR = 2;
    /**
     * 微博发送中
     */
    private static final int SEND_STATUS_SEND = 3;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPostType = intent.getStringExtra("postType");
        showSendNotifiy();
        switch (mPostType) {
            case POST_SERVICE_CREATE_WEIBO:
                LogUtil.e("发微博");
                break;
            case POST_SERVICE_REPOST_STATUS:
                LogUtil.e(POST_SERVICE_REPOST_STATUS);
                break;
            case POST_SERVICE_REPLY_COMMENT:
                LogUtil.e(POST_SERVICE_REPLY_COMMENT);
//                CommentReplyBean commentReplyBean = intent.getParcelableExtra("commentReplyBean");
//                replyComment(commentReplyBean);
                break;
            case POST_SERVICE_COMMENT_STATUS:
                LogUtil.e(POST_SERVICE_CREATE_WEIBO);
               /* WeiBoCommentBean weiBoCommentBean = intent.getParcelableExtra("weiBoCommentBean");
                commentWeiBo(weiBoCommentBean);*/
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void onRequestComplete() {
        mSendNotifity.cancel(SEND_STATUS_SEND);
        showSuccessNotifiy();
        final Message message = Message.obtain();
        message.what = SEND_STATUS_SEND;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(message);
            }
        }, 2000);
    }

    /**
     * 获取本地的图片,并且根据图片鞋带的信息纠正旋转方向
     *
     * @param absolutePath
     * @return
     */
    private Bitmap getLoacalBitmap(String absolutePath) {
        return null;
    }

    /**
     * 显示发送的notify
     */
    private void showSendNotifiy() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, SEND_STATUS_SEND, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";
        builder.setContentIntent(rightPendIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.label));
        builder.setSmallIcon(R.drawable.queue_icon_send);
        builder.setTicker(ticker);
        builder.setContentTitle("WeiSwift");
        builder.setContentText("正在发送");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setProgress(0, 0, true);
        Notification notification = builder.build();
        mSendNotifity = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        mSendNotifity.notify(SEND_STATUS_SEND, notification);
    }

    /**
     * 发送成功的通知
     */
    private void showSuccessNotifiy() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, SEND_STATUS_SUCCESS, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";
        builder.setContentIntent(rightPendIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.label));
        builder.setSmallIcon(R.drawable.queue_icon_success);
        builder.setTicker(ticker);
        builder.setContentTitle("WeiSwift");
        builder.setContentText("发送成功");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        Notification notification = builder.build();
        // 发送该通知
        mSendNotifity = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        mSendNotifity.notify(SEND_STATUS_SUCCESS, notification);
    }

    /**
     * 发送失败的通知
     */
    private void showErrorNotifiy() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, SEND_STATUS_ERROR, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";
        builder.setContentIntent(rightPendIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.label));
        builder.setSmallIcon(R.drawable.queue_icon_miss);
        builder.setTicker(ticker);
        builder.setContentTitle("WeiSwift");
        builder.setContentText("发送失败");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        Notification notification = builder.build();
        // 发送该通知
        mSendNotifity = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        mSendNotifity.notify(SEND_STATUS_ERROR, notification);
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSendNotifity.cancelAll();
            stopSelf();

        }
    };

}
