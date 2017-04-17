package com.example.asus.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public class Message implements Parcelable{
    private String newIcon;
    private String newTitle;
    private String newData;
    private String newContent;
    public void setNewIcon(String newIcon){
        this.newIcon=newIcon;
    }
    public String getNewIcon(){
        return newIcon;
    }
    public void setNewTitle(String NewTitle){
        this.newTitle=NewTitle;
    }
    public String getNewTitle(){
        return newTitle;
    }
    public void setNewData(String newData){
        this.newData=newData;
    }
    public String getNewData(){
        return newData;
    }
    public void setNewContent(String newContent){
        this.newContent=newContent;
    }
    public String getNewContent(){
        return newIcon;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.newIcon);
        dest.writeString(this.newTitle);
        dest.writeString(this.newData);
        dest.writeString(this.newContent);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            Message message=new Message();
            message.newIcon=source.readString();
            message.newTitle=source.readString();
            message.newData=source.readString();
            message.newContent=source.readString();
            return message;
        }
        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
