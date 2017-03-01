package com.example.asus.client.entity;

import java.io.Serializable;
public class Message implements Serializable{
    int type;
    //	发送该消息的人
    User sender;
    String sender_id;
    String senderNick;
    int senderAvatar;
    //	接收消息的人
    User receiver;
    String receiver_id;
    String content;
    long sendTime;

    public String getSender_id() {
        return sender_id;
    }
    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
    public String getReceiver_id() {
        return receiver_id;
    }
    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public String getSenderNick() {
        return senderNick;
    }
    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }
    public int getSenderAvatar() {
        return senderAvatar;
    }
    public void setSenderAvatar(int senderAvatar) {
        this.senderAvatar = senderAvatar;
    }
    public User getReceiver() {
        return receiver;
    }
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getSendTime() {
        return sendTime;
    }
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

}

