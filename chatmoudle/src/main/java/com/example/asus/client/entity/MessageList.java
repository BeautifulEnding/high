package com.example.asus.client.entity;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/4 0004.
 */

public class MessageList implements Serializable{
    private ArrayList<Message> messages = new ArrayList<Message>();
    private long lastTime;
    private String title;
    private String content;
    private int type;
    public void setType(int type){
        this.type=type;
    }
    public int getType(){
        return type;
    }
    public void setLastTime(long lastTime){
        this.lastTime=lastTime;
    }
    public long getLastTime(){
        return lastTime;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getTitle(){
        return title;
    }
    public void setContent(String content){
        this.content=content;
    }
    public String getContent(){
        return content;
    }
    public void setMessages(ArrayList<Message> messages){
        if (messages.size()==0){
            this.messages=messages;
        }else{
            messages.addAll(messages);
        }
    }
    public void setMessage(Message message){
        messages.add(message);
    }

    public ArrayList<Message> getMessage(){
        return messages;
    }

    public static MessageList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        MessageList list = new Gson().fromJson(jsonString, MessageList.class);
        return list;
    }
}
