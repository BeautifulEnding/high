package com.example.asus.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class ContentList implements Parcelable {

    private ArrayList<Content> contents = new ArrayList<Content>();

    public void setContents(ArrayList contents){
        this.contents=contents;
    }
    public ArrayList<Content> getContents(){
        return this.contents;
    }
    public static ContentList parse(String jsonString) {
//        将json字符串转化成ContentList对象
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        ContentList contentList = new Gson().fromJson(jsonString,ContentList.class);
        return contentList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.contents);
    }

    protected ContentList (Parcel in) {
        this.contents = in.createTypedArrayList(Content.CREATOR);
    }
    public ContentList(){

    }
    public static final Creator<ContentList> CREATOR = new Creator<ContentList>() {
        @Override
        public ContentList createFromParcel(Parcel source) {
            ContentList contentList=new ContentList();
            contentList.contents=source.createTypedArrayList(Content.CREATOR);
            return contentList;
        }

        @Override
        public ContentList[] newArray(int size) {
            return new ContentList[size];
        }
    };

}
