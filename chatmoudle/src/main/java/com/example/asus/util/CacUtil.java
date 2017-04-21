package com.example.asus.util;

import android.content.Context;

import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageList;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class CacUtil {
    public static void cacheSave(String fileName, Context context, User user) {
        if (fileName.equals("selfMessage")){
            String response = new Gson().toJson(user);
            SDCardUtil.put(context, Constant.SD_PATH+"selfMessage", fileName+ ".txt", response);
        }else{
            String response = new Gson().toJson(user);
            SDCardUtil.put(context, Constant.SD_PATH+"friends", fileName + ".txt", response);
        }

    }
    public static void cacheSave(String fileName, Context context, MessageList messageList){
        String response = new Gson().toJson(messageList);
        switch (fileName){
            case "helpMessage":
                SDCardUtil.put(context, Constant.SD_PATH+"helpMessage", fileName+ ".txt", response);
                break;
            case "requestFriend":
                SDCardUtil.put(context, Constant.SD_PATH+"requestFriend", fileName+ ".txt", response);
                break;
            case "togetherMessage":
                SDCardUtil.put(context, Constant.SD_PATH+"togetherMessage", fileName+ ".txt", response);
                break;
            case "oneMessage":
                SDCardUtil.put(context, Constant.SD_PATH+"oneMessage", fileName+ ".txt", response);
                break;
            default:
                SDCardUtil.put(context, Constant.CHAT_PATH, fileName+ ".txt", response);
        }
    }
    public static void cacheSave(String fileName, Context context, Message message){
        if (fileName.equals("requestFriend")){
            String response = new Gson().toJson(message);
            SDCardUtil.put(context, Constant.SD_PATH+"requestFriend", message.getSender_id()+ ".txt", response);
        }
    }
    public static void cacheAppend(String fileName, Context context, Message message){
        MessageList messageList= MessageList.parse(CacUtil.cacheLoad(0,context,fileName));
        if (messageList==null){
            messageList=new MessageList();
        }
        messageList.getMessage().add(message);
        CacUtil.cacheSave(fileName,context,messageList);
    }
    public static void cacheDelete(String user_id){
        SDCardUtil.deleteMessage(user_id,Constant.SD_PATH+"requestFriend");
//        删除请求添加好友信息
    }
    public static User cacheLoad(String fileName, Context context) {
        String response = null;
        if (fileName.equals("selfMessage")){
            response = SDCardUtil.get(context, Constant.SD_PATH+"selfMessage", "selfMessage.txt");
        }else{
            response = SDCardUtil.get(context, Constant.SD_PATH+"friends", fileName + ".txt");
        }
        if (response != null) {
            User user=User.parse(response);
            return user;
        }
        return null;
    }

    public static String cacheLoad(int type, Context context,String fileName) {
        String response = null;
        if (type==Constant.LOAD_MESSAGELIST){
            response = SDCardUtil.get(context,Constant.CHAT_PATH, fileName + ".txt");
        }else{
//            response = SDCardUtil.get(context, Constant.SD_PATH+"friends", fileName + ".txt");
            response = SDCardUtil.get(context, Constant.SD_PATH+fileName, fileName + ".txt");
        }
        if (response != null) {
            return response;
        }
        return null;
    }

    public static List<String> cacheLoad(Context context, int type) {
        LogUtil.e("正在加载缓存");
        List<String> response;
        switch (type){
            case Constant.LOAD_MESSAGE :
                response = SDCardUtil.get(context, Constant.SD_PATH+"requestFriend");
                return response;
            case Constant.LOAD_USER :
                response=SDCardUtil.get(context,Constant.SD_PATH+"friends");
                return response;
            case Constant.LOAD_MESSAGELIST:
                response=SDCardUtil.get(context,Constant.CHAT_PATH);
                return response;
        }
        return null;
    }

}
