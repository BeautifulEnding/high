package com.example.asus.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.activity.OriginPicTextCommentDetailActivity;
import com.example.asus.entity.Content;
import com.example.asus.he.R;
import com.example.asus.ui.FillContent;
import com.example.asus.ui.NewPauseOnScrollListener;
import com.example.asus.widget.EmojiTextView;
import com.nostra13.universalimageloader.core.*;


import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/20 0020.
 */

public class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    recycleView的适配器，要有ViewHolder
//    直接將從服務器得到的数据传送到这里，在ShowFragment另开一个线程保存得到的数据到SharePreferences中
    private Context mContext;
//    将每条正文数据包装成Content，
    private ArrayList<Content> mDatas;
//    private List<JSONObject> objects;
//    传给ViewHolder的整个布局
    private View mView;
//    区分是原创还是转发
//    原创
    private static final int TYPE_ORINGIN_ITEM = 0;
//    转发
    private static final int TYPE_RETWEET_ITEM = 3;
    SharedPreferences contentPre=null;
    public SimpleAdapter(Context context, ArrayList<Content> datas) {
        Log.e("SimAdapter","正在执行SimpleAdapter---------------------------");
        mContext=context;
        this.mDatas=datas;
        this.contentPre=contentPre;
    }
    public SimpleAdapter(Context context, SharedPreferences contentPre) {
        Log.e("SimAdapter","正在执行SimpleAdapter---------------------------");
        mContext=context;
    }
    /**
     *创建ViewHolder，viewType不一样，创建的ViewHolder也不一样
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        如果是原创
        if (viewType == TYPE_ORINGIN_ITEM) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.home_weiboitem_original_pictext, parent, false);
            OriginViewHolder originViewHolder = new OriginViewHolder(mView);
            originViewHolder.imageList.addOnScrollListener(new NewPauseOnScrollListener(com.nostra13.universalimageloader.core.ImageLoader.getInstance().getInstance(), true, true));
            return originViewHolder;
        } else if (viewType == TYPE_RETWEET_ITEM) {
//            转发
            mView = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem_retweet_pictext, parent, false);
            RetweetViewHolder retweetViewHolder = new RetweetViewHolder(mView);
            retweetViewHolder.retweet_imageList.addOnScrollListener(new NewPauseOnScrollListener(com.nostra13.universalimageloader.core.ImageLoader.getInstance().getInstance(), true, true));
            return retweetViewHolder;
        }

        return null;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        /*if (holder instanceof OriginViewHolder) {

        } else if (holder instanceof RetweetViewHolder) {

        }*/
        ((OriginViewHolder) holder).titlebar_layout.setVisibility(View.VISIBLE);
        ((OriginViewHolder) holder).bottombar_layout.setVisibility(View.VISIBLE);
        ((OriginViewHolder) holder).splitLine.setVisibility(View.GONE);
        ((OriginViewHolder) holder).favoritedelete.setVisibility(View.GONE);
        FillContent.fillTitleBar(mContext, mDatas.get(mDatas.size()-position-1), ((OriginViewHolder) holder).profile_img, ((OriginViewHolder) holder).profile_name, ((OriginViewHolder) holder).profile_time, ((OriginViewHolder) holder).weibo_comefrom);
        FillContent.fillWeiBoContent(mDatas.get(mDatas.size()-position-1).getContent(), mContext, ((OriginViewHolder) holder).weibo_content);
        FillContent.fillButtonBar(mContext, mDatas.get(mDatas.size()-position-1), ((OriginViewHolder) holder).bottombar_retweet, ((OriginViewHolder) holder).bottombar_comment, ((OriginViewHolder) holder).bottombar_attitude, ((OriginViewHolder) holder).redirect, ((OriginViewHolder) holder).comment, ((OriginViewHolder) holder).feedlike);
        FillContent.fillWeiBoImgList(mDatas.get(mDatas.size()-position-1), mContext, ((OriginViewHolder) holder).imageList);
        ((OriginViewHolder) holder).popover_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"点击向下箭头",Toast.LENGTH_SHORT).show();
            }
        });

        //微博背景的点击事件
        ((OriginViewHolder) holder).origin_weibo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OriginPicTextCommentDetailActivity.class);
//                传递正文对象
                intent.putExtra("weiboitem", mDatas.get(mDatas.size()-position-1));
                mContext.startActivity(intent);
//                Toast.makeText(mContext,"微博背景的点击事件",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void setData(ArrayList<Content> objects) {
        this.mDatas = objects;
    }

    /**
     * 原创内容的ViewHolder
     */
    public static class OriginViewHolder extends RecyclerView.ViewHolder {
//        点赞图片
        public ImageView praise;
        public LinearLayout origin_weibo_layout;
        public LinearLayout titlebar_layout;
        public ImageView profile_img;
        public ImageView popover_arrow;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weibo_comefrom;
        public EmojiTextView weibo_content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;
        public RecyclerView imageList;
        public TextView favoritedelete;
        public ImageView splitLine;
        public LinearLayout bottombar_layout;
        public LinearLayout bottombar_retweet;
        public LinearLayout bottombar_comment;
        public LinearLayout bottombar_attitude;

        public OriginViewHolder(View v) {
            super(v);
            praise=(ImageView)v.findViewById(R.id.praise);
            origin_weibo_layout = (LinearLayout) v.findViewById(R.id.origin_weibo_layout);
            titlebar_layout = (LinearLayout) v.findViewById(R.id.titlebar_layout);
//            头像
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
//            向下箭头
            popover_arrow = (ImageView) v.findViewById(R.id.popover_arrow);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            weibo_content = (EmojiTextView) v.findViewById(R.id.weibo_Content);
            weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
//            分割顶部用户信息和正文内容
            splitLine = (ImageView) v.findViewById(R.id.splitLine);
            imageList = (RecyclerView) v.findViewById(R.id.weibo_image);
            favoritedelete = (TextView) v.findViewById(R.id.favorities_delete);
//            底部的转发、评论、赞
            bottombar_layout = (LinearLayout) v.findViewById(R.id.bottombar_layout);
            bottombar_retweet = (LinearLayout) v.findViewById(R.id.bottombar_retweet);
            bottombar_comment = (LinearLayout) v.findViewById(R.id.bottombar_comment);
            bottombar_attitude = (LinearLayout) v.findViewById(R.id.bottombar_attitude);
        }
    }

    /**
     * 转发的正文布局ViewHolder
     */
    public static class RetweetViewHolder extends RecyclerView.ViewHolder {
//        点赞
        public ImageView praise;
        public LinearLayout retweet_weibo_layout;
        public ImageView profile_img;
        public ImageView popover_arrow;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weibo_comefrom;
        public EmojiTextView retweet_content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;
        public EmojiTextView origin_nameAndcontent;
        public RecyclerView retweet_imageList;
        public LinearLayout bottombar_layout;
        public LinearLayout bottombar_retweet;
        public LinearLayout bottombar_comment;
        public LinearLayout bottombar_attitude;
        public LinearLayout retweetStatus_layout;

        public RetweetViewHolder(View v) {
            super(v);
            praise=(ImageView)v.findViewById(R.id.praise);
            retweet_weibo_layout = (LinearLayout) v.findViewById(R.id.retweet_weibo_layout);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            popover_arrow = (ImageView) v.findViewById(R.id.popover_arrow);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            retweet_content = (EmojiTextView) v.findViewById(R.id.retweet_content);
            weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
            origin_nameAndcontent = (EmojiTextView) v.findViewById(R.id.origin_nameAndcontent);
            retweet_imageList = (RecyclerView) v.findViewById(R.id.origin_imageList);
            bottombar_layout = (LinearLayout) v.findViewById(R.id.bottombar_layout);
            bottombar_retweet = (LinearLayout) v.findViewById(R.id.bottombar_retweet);
            bottombar_comment = (LinearLayout) v.findViewById(R.id.bottombar_comment);
            bottombar_attitude = (LinearLayout) v.findViewById(R.id.bottombar_attitude);
            retweetStatus_layout = (LinearLayout) v.findViewById(R.id.retweetStatus_layout);
        }
    }
}
