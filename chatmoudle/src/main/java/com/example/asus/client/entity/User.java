package com.example.asus.client.entity;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;

public class User implements Serializable{
    /**
     * 字符串型的用户 ID
     */
    private String id;
    /**
     * 用户昵称
     */
    private String screen_name;
    /**
     * 用户个人描述
     */
    private String description;
    /**
     * 用户头像地址，50×50像素
     */
    private String profile_image_url;
    /**
     * 性别，m：男、f：女、n：未知
     */
    private String gender;
    /**
     * 用户的在线状态，0：不在线、1：在线
     */
    private int online_status;

    private String telPhone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getOnline_status() {
        return online_status;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public static User parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        User user = new Gson().fromJson(jsonString, User.class);
        return user;
    }
    public User() {

    }

}
