package com.example.asus.ui;

import android.content.Context;

import com.example.asus.entity.Comment;
import com.example.asus.entity.Content;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/6/25.
 */
public interface StatusDetailModel {

    interface OnCommentCallBack {
        void noMoreDate();

        void onDataFinish(ArrayList<Comment> commentlist);

        void onError(String error);
    }


    interface OnRepostCallBack {
        void noMoreDate();

        void onDataFinish(ArrayList<Content> commentlist);

        void onError(String error);
    }

    public void comment(int groupType, Content status, Context context, OnCommentCallBack onCommentCallBack);

    public void commentNextPage(int groupType, Content status, Context context, OnCommentCallBack onCommentCallBack);


    public void repost(int groupType, Content status, Context context, OnRepostCallBack onRepostCallBack);

    public void repostNextPage(int groupType, Content status, Context context, OnRepostCallBack onRepostCallBack);

}
