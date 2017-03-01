package com.example.asus.entity;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/12 0012.
 */

public class Content implements Parcelable {
    //    发布人头像
    String user_photo;
    //    发布人id
    String id;
    //    正文内容
    String content;
    //  正文图片
    ArrayList<String> photo;
    //    正文发布时间
//转发数量
    int retweet;
//    评论数量
    int comment;
//    点赞数量
    int pharise;
//    发表时间
    long tag;
//    是否点赞
    int ifPharise;
//    是否是转发来的内容
    int ifretweet;
//    用户
    private User user;
    public User getUser(){
        User user=new User();
        user.id=this.id;
        user.profile_image_url=this.user_photo;
        this.user=user;
        return user;
    }
    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }
    public String getUser_photo(){
        return this.user_photo;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getId(){
        return this.id;
    }
    public void setContent(String content){
        this.content=content;
    }
    public String getContent(){
        return this.content;
    }
    public void setPhoto(ArrayList<String> photo){
        this.photo=photo;
    }
    public ArrayList<String> getPhoto(){
        return this.photo;
    }
    public void setTag(long tag){
        this.tag=tag;
    }
    public long getTag(){
        return this.tag;
    }
    public int getRetweet(){
        return  this.retweet;
    }
    public void setRetweet(int retweet){
        this.retweet=retweet;
    }
    public void setComment(int comment){
        this.comment=comment;
    }
    public int getComment(){
        return comment;
    }
    public void setPharise(int pharise){
        this.pharise=pharise;
    }
    public int getPharise(){
        return this.pharise;
    }
    public int getIfPharise(){
        return ifPharise;
    }
    public void setIfPharise(int ifPharise){
        this.ifPharise=ifPharise;
    }
    public void setIfretweet(int ifretweet){
        this.ifretweet=ifretweet;
    }
    public int getIfretweet(){
        return this.ifretweet;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_photo);
        dest.writeString(this.id);
        dest.writeString(this.content);
        dest.writeStringList(this.photo);
        dest.writeLong(this.tag);
        dest.writeInt(this.retweet);
        dest.writeInt(this.comment);
        dest.writeInt(this.pharise);
        dest.writeInt(this.ifPharise);
        dest.writeParcelable(this.user,flags);
        dest.writeInt(this.ifretweet);
    }
    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel source) {
            Content content=new Content();
            content.user_photo=source.readString();
            content.id=source.readString();
            content.content=source.readString();
            content.photo=source.createStringArrayList();
            content.tag=source.readLong();
            content.retweet=source.readInt();
            content.comment=source.readInt();
            content.pharise=source.readInt();
            content.ifPharise=source.readInt();
            content.user=source.readParcelable(User.class.getClassLoader());
            content.ifretweet=source.readInt();
            return content;
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}
