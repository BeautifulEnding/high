package com.example.asus.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.entity.Content;
import com.example.asus.he.R;
import com.example.asus.ui.FillContent;
import com.example.asus.util.LogUtil;
import com.example.asus.widget.EmojiTextView;
import com.example.asus.widget.LoadingFooter;

/**
 * Created by wenmingvs on 16/4/26.
 */
public class RetweetPicTextHeaderView extends LinearLayout {

    private View mView;
    private LinearLayout retweet_weibo_layout;
    private ImageView profile_img;
    private ImageView profile_verified;
    private TextView profile_name;
    private TextView profile_time;
    private TextView weibo_comefrom;
    private EmojiTextView retweet_content;
    private LinearLayout bottombar_layout;
    private EmojiTextView origin_nameAndcontent;
    private RecyclerView retweet_imageList;
    private TextView commentView;
    private TextView retweetView;
    private TextView likeView;
    private RelativeLayout mNoneView;
    private Context mContext;
    private ImageView mCommentIndicator;
    private ImageView mRetweetIndicator;
    private ImageView mPopover_arrow;
    private OnDetailButtonClickListener onDetailButtonClickListener;
//    定義為1
    private int mType = 0x1;
    public LinearLayout retweetStatus_layout;


    public RetweetPicTextHeaderView(Context context, Content status, int type) {
        super(context);
        mType = type;
        init(context, status);
        switch (mType) {
            case 0x1:
                commentHighlight();
                break;
            case 0x2:
                repostHighlight();
                break;
        }
    }

    public void setOnDetailButtonClickListener(OnDetailButtonClickListener onDetailButtonClickListener) {
        this.onDetailButtonClickListener = onDetailButtonClickListener;
    }

    public void init(Context context, Content status) {
        mContext = context;
        mView = inflate(context, R.layout.mainfragment_weiboitem_detail_commentbar_retweet_pictext_headview, this);
        retweet_weibo_layout = (LinearLayout) findViewById(R.id.retweet_weibo_layout);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_time = (TextView) findViewById(R.id.profile_time);
        retweet_content = (EmojiTextView) findViewById(R.id.retweet_content);
        weibo_comefrom = (TextView) findViewById(R.id.weiboComeFrom);
        bottombar_layout = (LinearLayout) findViewById(R.id.bottombar_layout);
        origin_nameAndcontent = (EmojiTextView) findViewById(R.id.origin_nameAndcontent);
        retweet_imageList = (RecyclerView) findViewById(R.id.origin_imageList);
        commentView = (TextView) findViewById(R.id.commentBar_comment);
        retweetView = (TextView) findViewById(R.id.commentBar_retweet);
        likeView = (TextView) findViewById(R.id.commentBar_like);
        mPopover_arrow = (ImageView) mView.findViewById(R.id.popover_arrow);
        mCommentIndicator = (ImageView) findViewById(R.id.comment_indicator);
        mRetweetIndicator = (ImageView) findViewById(R.id.retweet_indicator);
        retweetStatus_layout = (LinearLayout) findViewById(R.id.retweetStatus_layout);
        initWeiBoContent(context, status);
    }

    private void initWeiBoContent(Context context, final Content status) {
        FillContent.fillTitleBar(mContext, status, profile_img, profile_name, profile_time, weibo_comefrom);
        FillContent.fillWeiBoContent(status.getContent(), context, retweet_content);
//        FillContent.fillRetweetContent(status, context, origin_nameAndcontent);
        FillContent.fillWeiBoImgList(null, context, retweet_imageList);
//        FillContent.showButtonBar(View.GONE, bottombar_layout);
//        FillContent.fillDetailBar(status.comments_count, status.reposts_count, status.attitudes_count, commentView, retweetView, likeView);
//        FillContent.refreshNoneView(mContext, mType, status.reposts_count, status.comments_count, mNoneView);
//        boolean isNightMode = (boolean) SharedPreferencesUtil.get(mContext, "setNightMode", false);
        if (false) {
            likeView.setTextColor(Color.parseColor("#45484a"));
        } else {
            likeView.setTextColor(Color.parseColor("#828282"));
        }

        /*mPopover_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrowDialog arrowDialog = new TimelineArrowWindow.Builder(mContext, status)
                        .setCanceledOnTouchOutside(true)
                        .setCancelable(true)
                        .create();
                int width = ScreenUtil.getScreenWidth(mContext) - DensityUtil.dp2px(mContext, 80);
                arrowDialog.show();
                arrowDialog.getWindow().setLayout(width, (ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        });*/

        retweetView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                repostHighlight();
                onDetailButtonClickListener.OnRetweet();
            }
        });
        commentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commentHighlight();
                onDetailButtonClickListener.OnComment();
            }
        });

        retweetStatus_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (status.retweeted_status != null && status.retweeted_status.user != null) {
                    Intent intent = new Intent(mContext, OriginPicTextCommentDetailActivity.class);
                    intent.putExtra("weiboitem", status.retweeted_status);
                    mContext.startActivity(intent);
                }*/
                LogUtil.e("retweetStatus_layout被单击");
            }
        });
    }


    public void refreshDetailBar(int comments_count, int reposts_count, int attitudes_count) {
//        FillContent.fillDetailBar(comments_count, reposts_count, attitudes_count, commentView, retweetView, likeView);
//        FillContent.refreshNoneView(mContext, mType, reposts_count, comments_count, mNoneView);
    }

    public void commentHighlight() {
//        boolean isNightMode = (boolean) SharedPreferencesUtil.get(mContext, "setNightMode", false);
        if (false) {
            commentView.setTextColor(Color.parseColor("#000000"));
            mCommentIndicator.setVisibility(View.VISIBLE);
            retweetView.setTextColor(Color.parseColor("#828282"));
            mRetweetIndicator.setVisibility(View.INVISIBLE);
        } else {
            commentView.setTextColor(Color.parseColor("#888888"));
            mCommentIndicator.setVisibility(View.VISIBLE);
            retweetView.setTextColor(Color.parseColor("#45484a"));
            mRetweetIndicator.setVisibility(View.INVISIBLE);
        }


    }

    public void repostHighlight() {
//        boolean isNightMode = (boolean) SharedPreferencesUtil.get(mContext, "setNightMode", false);
        if (false) {
            retweetView.setTextColor(Color.parseColor("#000000"));
            mRetweetIndicator.setVisibility(View.VISIBLE);
            commentView.setTextColor(Color.parseColor("#828282"));
            mCommentIndicator.setVisibility(View.INVISIBLE);
        } else {
            retweetView.setTextColor(Color.parseColor("#888888"));
            mRetweetIndicator.setVisibility(View.VISIBLE);
            commentView.setTextColor(Color.parseColor("#45484a"));
            mCommentIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
