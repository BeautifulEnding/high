package com.example.asus.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.entity.Content;
import com.example.asus.he.R;
import com.example.asus.ui.FillContent;
import com.example.asus.widget.EmojiTextView;

/**
 * 包括recyclerView以上的所有内容
 */
public class OriginPicTextHeaderView extends LinearLayout {
    private View mView;
    private LinearLayout origin_weibo_layout;
    private ImageView profile_img;
    private ImageView profile_verified;
    private TextView profile_name;
    private TextView profile_time;
    private TextView weibo_comefrom;
    private EmojiTextView weibo_content;
    private LinearLayout bottombar_layout;
    private RecyclerView imageList;
    private TextView retweetView;
    private TextView commentView;
    private TextView likeView;
    private RelativeLayout mNoneView;
    private Context mContext;
    private ImageView mCommentIndicator;
    private ImageView mRetweetIndicator;
    private ImageView mPopover_arrow;
    private OnDetailButtonClickListener onDetailButtonClickListener;
    private int mType = 0x1;
    public OriginPicTextHeaderView(Context context, Content status, int type) {
        super(context);
        mType = type;
        init(context, status);
        switch (mType) {
            case 0x1:
//                评论底部显示黄色线条
                commentHighlight();
                break;
            case 0x2:
//                转发底部显示黄色线条
                repostHighlight();
                break;
        }
    }

    public void setOnDetailButtonClickListener(OnDetailButtonClickListener onDetailButtonClickListener) {
        this.onDetailButtonClickListener = onDetailButtonClickListener;
    }

    public void init(Context context, Content status) {
        mContext = context;
        mView = inflate(context, R.layout.detail_commentbar_origin_pictext_headview, this);
//        原创布局
        origin_weibo_layout = (LinearLayout) mView.findViewById(R.id.origin_weibo_layout);
//        头像
        profile_img = (ImageView) mView.findViewById(R.id.profile_img);
        profile_name = (TextView) mView.findViewById(R.id.profile_name);
        profile_time = (TextView) mView.findViewById(R.id.profile_time);
        weibo_content = (EmojiTextView) mView.findViewById(R.id.weibo_Content);
        weibo_comefrom = (TextView) mView.findViewById(R.id.weiboComeFrom);
//        原创正文布局底部布局
        bottombar_layout = (LinearLayout) mView.findViewById(R.id.bottombar_layout);
        imageList = (RecyclerView) mView.findViewById(R.id.weibo_image);
//        mPopover_arrow = (ImageView) mView.findViewById(R.id.popover_arrow);
//        评论
        commentView = (TextView) mView.findViewById(R.id.commentBar_comment);
//        转发
        retweetView = (TextView) mView.findViewById(R.id.commentBar_retweet);
//        赞
        likeView = (TextView) mView.findViewById(R.id.commentBar_like);
//        评论下面的图片
        mCommentIndicator = (ImageView) findViewById(R.id.comment_indicator);
        mRetweetIndicator = (ImageView) findViewById(R.id.retweet_indicator);
//        没有人转发评论时代替RecyclerView的布局
        mNoneView=(RelativeLayout) mView.findViewById(R.id.noneLayout);
        initWeiBoContent(context, status);
    }
    private void initWeiBoContent(Context context, final Content status) {
        FillContent.fillTitleBar(mContext, status, profile_img, profile_name, profile_time, weibo_comefrom);
        FillContent.fillWeiBoContent(status.getContent(), context, weibo_content);
        FillContent.fillWeiBoImgList(status, context, imageList);
        FillContent.showButtonBar(View.GONE, bottombar_layout);
//        FillContent.fillDetailBar(status.comments_count, status.reposts_count, status.reposts_count, commentView, retweetView, likeView);
        FillContent.refreshNoneView(mContext, mType, status.getRetweet(), status.getComment(), mNoneView);
        if (true) {
            likeView.setTextColor(Color.parseColor("#45484a"));
        }else {
            likeView.setTextColor(Color.parseColor("#828282"));
        }

       /* mPopover_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                DetailWeiBoArrowWindow detailWeiBoArrowWindow = new DetailWeiBoArrowWindow(mContext, status);
//                detailWeiBoArrowWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);

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
                FillContent.refreshNoneView(mContext, mType, status.getRetweet(), status.getComment(), mNoneView);
            }
        });

        commentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commentHighlight();
                onDetailButtonClickListener.OnComment();
                FillContent.refreshNoneView(mContext, mType, status.getRetweet(), status.getComment(), mNoneView);
            }
        });

    }

    public void refreshDetailBar(int comments_count, int reposts_count, int attitudes_count) {
//        FillContent.fillDetailBar(comments_count, reposts_count, attitudes_count, commentView, retweetView, likeView);
        FillContent.refreshNoneView(mContext, mType, reposts_count, comments_count, mNoneView);
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
