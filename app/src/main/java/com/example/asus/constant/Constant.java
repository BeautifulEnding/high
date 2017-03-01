package com.example.asus.constant;

import android.os.Environment;

import com.example.asus.client.Client;
import com.example.asus.entity.User;
import com.example.asus.util.HttpUtil;

public class Constant {
    //Btn的标识
    public static final int BTN_FLAG_MESSAGE = 0x01 << 1;
    public static final int BTN_FLAG_SHOW = 0x01;
    public static final int BTN_FLAG_SEARCH = 0x01 << 3;
    public static final int BTN_FLAG_SEND = 0x01 << 2;
    public static final int BTN_FLAG_SETTING = 0x01 << 4;

    //Fragment的标识
    public static final String FRAGMENT_FLAG_MESSAGE = "消息";
    public static final String FRAGMENT_FLAG_SHOW = "首页";
    public static final String FRAGMENT_FLAG_SEARCH= "好友";
    public static final String FRAGMENT_FLAG_SETTING = "设置";
//    public static final String FRAGMENT_FLAG_SEND = "发布";

    //用户信息标识
    public static  final String[] key={"user_id","user_name","user_tel","user_self","user_sex","user_birthday","user_photo","user_password"};
    public static  final String[] allKey={"user_photo","user_id","user_password","user_name","user_tel","user_self","user_sex","user_birthday"};
    //定义SD卡路径
    public static final String SD_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/high";
    public static final String PHOTO_PATH= SD_PATH+"/photo";
    public static final String USER_PHOTO_PATH=SD_PATH+"/user_photo";
//    服务器用户头像路径
    public static final String USER_PHOTO=HttpUtil.URL+"user_photo/";
//    服务器正文图片路径
    public static final String CONTENT_PHOTO= HttpUtil.URL+"contentPhoto/";
//    话题
    public static final String HELP = "帮助";
    public static final String APPOINTMENT = "约";
    public static final String ONE ="we are one";
//    该客户端用户
    public static User SELF_USER;
    public static Client CLIENT;
//    添加好友格式  自己的id 对方id@AddFriend 其中 和@为分隔符，@后面的为消息类型

}
