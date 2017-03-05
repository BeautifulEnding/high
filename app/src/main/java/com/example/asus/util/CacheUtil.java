package com.example.asus.util;

import android.content.Context;

import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageList;
import com.example.asus.client.entity.User;
import com.example.asus.constant.*;
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
    public static void cacheSave(String friend, Context context, ContentList statusList) {
        String response = new Gson().toJson(statusList);
        /*if (topic ==Constant.HELP) {
//            帮助
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/high/topic", "帮助" + ".txt", response);
        } else if (topic == Constant.APPOINTMENT) {
//            约
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/high/topic", "约" + ".txt", response);
        } else {
//            we are one
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/high/topic", topic + ".txt", response);
        }*/
        SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/high/topic", friend + ".txt", response);
    }

    public static void cacheSave(String fileName, Context context, User user) {
        if (fileName.equals("selfMessage")){
            String response = new Gson().toJson(user);
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/high/selfMessage", fileName+ ".txt", response);
        }else{
            String response = new Gson().toJson(user);
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/high/friends", fileName + ".txt", response);
        }

    }
    public static void cacheSave(String fileName, Context context, Message messageList){
        String response = new Gson().toJson(messageList);
        SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/high/requestFriend", fileName+ ".txt", response);
    }
    public static void cacheDelete(String user_id){
        SDCardUtil.deleteMessage(user_id,SDCardUtil.getSDCardPath() + "/high/requestFriend");
//        删除请求添加好友信息
    }
    public static boolean cacheLoad(String topic, Context context, ArrayList<Content> contents, HomeFragmentView homeFragmentView) {
        LogUtil.e("正在加载缓存");
        String response = null;
        /*if (topic == Constant.HELP) {
            response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/high/topic", "帮助" + ".txt");
        } else if (topic == Constant.APPOINTMENT) {
            response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/high/topic", "约" + ".txt");
        } else if (topic==Constant.ONE){
            response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() +"/high/topic", topic + ".txt");
        }else {
            response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/high/topic", "帮助" + ".txt");
        }*/
        response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() +"/high/topic", topic + ".txt");
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

    public static User cacheLoad(String fileName, Context context) {
        LogUtil.e("正在加载缓存");
        String response = null;
        if (fileName.equals("selfMessage")){
            response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() +"/high/selfMessage", "selfMessage.txt");
        }else{
            response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() +"/high/friends", fileName + ".txt");
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
                response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/high/requestFriend");
                return response;
            case Constant.LOAD_USER :
                response=SDCardUtil.get(context,SDCardUtil.getSDCardPath() + "/high/friends");
                return response;
        }
        return null;
    }

}
