package com.example.asus.util;

import android.content.Context;

import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.entity.Content;
import com.example.asus.entity.ContentList;
import com.example.asus.view.HomeFragmentView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class CacheUtil {
    public static void cacheSave(String topic, Context context, ContentList statusList) {
        String response = new Gson().toJson(statusList);
        SDCardUtil.put(context, Constant.SD_PATH+"topic", topic + ".txt", response);
    }
    public static void cacheSave(int topicType, Context context, ContentList statusList) {
        String topic="";
        switch (topicType){
            case 0:
                topic="帮助";
                break;
            case 1:
                topic="约";
                break;
            case 2:
                topic="we are one";
                break;
            default:
                topic="所有";
                break;
        }
        String response = new Gson().toJson(statusList);
        SDCardUtil.put(context, Constant.SD_PATH+"topic", topic + ".txt", response);
    }
    public static void cacheSave(String fileName, Context context, User user) {
        if (fileName.equals("selfMessage")){
            String response = new Gson().toJson(user);
//            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/high/selfMessage", fileName+ ".txt", response);
            SDCardUtil.put(context, Constant.SD_PATH+"selfMessage", fileName+ ".txt", response);
        }else{
            String response = new Gson().toJson(user);
//            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/high/friends", fileName + ".txt", response);
            SDCardUtil.put(context, Constant.SD_PATH+"friends", fileName+ ".txt", response);
        }

    }
    public static void cacheSave(String fileName, Context context, Message messageList){
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

        }
    }
    public static void cacheDelete(String user_id){
//        SDCardUtil.deleteMessage(user_id,SDCardUtil.getSDCardPath() + "/high/requestFriend");
        SDCardUtil.deleteMessage(user_id,Constant.SD_PATH+"requestFriend");
//        删除请求添加好友信息
    }
    public static boolean cacheLoad(String topic, Context context, ArrayList<Content> contents, HomeFragmentView homeFragmentView) {
        LogUtil.e("正在加载缓存");
        String response = null;
         response = SDCardUtil.get(context, Constant.SD_PATH+"topic", topic + ".txt");
        if (response != null) {
            contents= (ArrayList<Content>) ContentList.parse(response).getContents();
            homeFragmentView.hideLoadingIcon();
            homeFragmentView.scrollToTop(false);
            homeFragmentView.updateListView(contents);
            homeFragmentView.showRecyclerView();
            homeFragmentView.hideEmptyBackground();
            return true;
        } else {
//            ToastUtil.showLong(getActivity(),"还没有缓存的内容");
            return false;
        }
    }

    public static ContentList cacheLoad(int type,Context context){
        String response=null;
        switch (type){
            case 0:
                response=SDCardUtil.get(context, Constant.SD_PATH+"topic",  "帮助.txt");
                break;
            case 1:
                response=SDCardUtil.get(context, Constant.SD_PATH+"topic",  "约.txt");
                break;
            case 2:
                response=SDCardUtil.get(context, Constant.SD_PATH+"topic",  "we are one.txt");
                break;
            default:
                response=SDCardUtil.get(context, Constant.SD_PATH+"topic",  "所有.txt");
                break;

        }
        if (response!=null){
            return ContentList.parse(response);
        }

        return null;
    }

    public static User cacheLoad(String fileName, Context context) {
        LogUtil.e("正在加载缓存");
        String response = null;
        if (fileName.equals("selfMessage")){
//            response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() +"/high/selfMessage", "selfMessage.txt");
            response = SDCardUtil.get(context, Constant.SD_PATH+"selfMessage", "selfMessage.txt");
        }else{
//            response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() +"/high/friends", fileName + ".txt");
            response = SDCardUtil.get(context, Constant.SD_PATH+"friends", fileName + ".txt");
        }
        if (response != null) {
            User user=User.parse(response);
            return user;
        }
        return null;
    }

    public static List<String> cacheLoad(Context context,int type) {
        LogUtil.e("正在加载缓存");
        List<String> response;
        switch (type){
            case Constant.LOAD_MESSAGE :
//                response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/high/requestFriend");
                response = SDCardUtil.get(context, Constant.SD_PATH+"requestFriend");
                return response;
            case Constant.LOAD_USER :
//                response=SDCardUtil.get(context,SDCardUtil.getSDCardPath() + "/high/friends");
                response = SDCardUtil.get(context, Constant.SD_PATH+"friends");
                return response;
        }
        return null;
    }

}
