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
