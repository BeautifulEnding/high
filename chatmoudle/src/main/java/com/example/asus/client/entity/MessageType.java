package com.example.asus.client.entity;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class MessageType {
    public static final int SUCCESS=1;//表明是否成功
    public static final int FAIL=2;//表明失败
    public static final int COM_MES=3;//普通信息包
    public static final int GET_ONLINE_FRIENDS=4;//要求在线好友的包
    public static final int RET_ONLINE_FRIENDS=5;//返回在线好友的包
    public static final int ADD_FRIEND=6;//请求添加好友
    public static final int LOGIN=7;//请求验证登陆
    public static final int USER_MESSAGE=8;//发送用户消息
    public static final int RECEIVE_FRIEND=9;
    public static final int NOTIFICATION=10;
    public static final int AGREE_FRIEND=11;
    public static final int ACCEPT_FRIEND=12;
    public static final int ACCEPT_NOTIFICATION=13;
    public static final int VOICE_MESSAGE=14;
    public static final int PICTURE_MESSAGE=15;
//    public static final int UPDATE_MESSAGE=16;
    public static final int EXIT=17;
    public static final int NOTIFICATION_LM=18;
    public static final int HELP_MESSAGE=19;
    public static final int TOGETHER_MESSAGE=20;
    public static final int ONE_MESSAGE=21;
    public static final int NOTIFICATION_HELP=22;
    public static final int NOTIFICATION_TOGETHER=23;
    public static final int NOTIFICATION_ONE=24;


}
