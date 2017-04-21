package com.example.asus.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.entity.Content;
import com.example.asus.entity.User;
import com.example.asus.he.R;
import com.example.asus.ui.FillContent;
import com.example.asus.widget.EmojiTextView;

import java.util.ArrayList;


/**
 * 用于显示评论列表的adapter
 * Created by wenmingvs on 16/4/23.
 */
public class MentionDetailAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private ArrayList<Content> mDatas;
    private View mView;


    public MentionDetailAdapter(Context mContext, ArrayList<Content> datas) {
        this.mContext = mContext;
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.detail_commentbar_comment_item, parent, false);
        RetweetViewHolder commentViewHolder = new RetweetViewHolder(mView);
        return commentViewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mDatas.get(position).getUser();
        String content = mDatas.get(position).getContent();
        FillContent.fillProfileImg(mContext, user, ((RetweetViewHolder) holder).profile_img);
        FillContent.fillWeiBoContent(content, mContext, ((RetweetViewHolder) holder).content);
        FillContent.setWeiBoName(((RetweetViewHolder) holder).profile_name, user);
        FillContent.setWeiBoTime(mContext, ((RetweetViewHolder) holder).profile_time, mDatas.get(position));
    }


    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    public void setData(ArrayList<Content> data) {
        this.mDatas = data;
    }

    public class RetweetViewHolder extends ViewHolder {
        //微博列表的控件
        public ImageView profile_img;
        public TextView profile_name;
        public TextView profile_time;
        public EmojiTextView content;

        public RetweetViewHolder(View v) {
            super(v);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_name = (TextView) v.findViewById(R.id.comment_profile_name);
            profile_time = (TextView) v.findViewById(R.id.comment_profile_time);
            content = (EmojiTextView) v.findViewById(R.id.comment_content);
        }
    }


}
