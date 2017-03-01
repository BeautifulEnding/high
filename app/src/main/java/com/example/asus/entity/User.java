
package com.example.asus.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * 用户信息结构体。
 */
public class User implements Parcelable {
    /**
     * 字符串型的用户 ID
     */
    public String id;
    /**
     * 用户昵称
     */
    public String screen_name;
    /**
     * 用户个人描述
     */
    public String description;
    /**
     * 用户头像地址，50×50像素
     */
    public String profile_image_url;
    /**
     * 性别，m：男、f：女、n：未知
     */
    public String gender;
    /**
     * 用户的在线状态，0：不在线、1：在线
     */
    public int online_status;

    public String telPhone;

    public String birthday;

    public static User parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        User user = new Gson().fromJson(jsonString, User.class);
        return user;
    }
    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.screen_name);
        dest.writeString(this.description);
        dest.writeString(this.profile_image_url);
        dest.writeString(this.gender);
        dest.writeInt(this.online_status);
        dest.writeString(this.telPhone);
        dest.writeString(this.birthday);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.screen_name = in.readString();
        this.description = in.readString();
        this.profile_image_url = in.readString();
        this.gender = in.readString();
        this.online_status = in.readInt();
        this.telPhone=in.readString();
        this.birthday=in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
