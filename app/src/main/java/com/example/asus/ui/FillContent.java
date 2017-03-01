package com.example.asus.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.asus.activity.IdeaActivity;
import com.example.asus.activity.OriginPicTextCommentDetailActivity;
import com.example.asus.activity.RetweetPicTextCommentDetailActivity;
import com.example.asus.activity.ImageDetailsActivity;
import com.example.asus.constant.*;
import com.example.asus.constant.Constant;
import com.example.asus.entity.Comment;
import com.example.asus.entity.Content;
import com.example.asus.entity.User;
import com.example.asus.he.R;
import com.example.asus.service.PostService;
import com.example.asus.util.*;
import com.example.asus.util.LogUtil;
import com.example.asus.widget.EmojiTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2017/2/12 0012.
 */

public class FillContent {

    private static DisplayImageOptions mAvatorOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avator_default)
            .showImageForEmptyUri(R.drawable.avator_default)
            .showImageOnFail(R.drawable.avator_default)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new CircleBitmapDisplayer(14671839, 1))
            .build();

    //        填充正文的顶部布局
    public static void fillTitleBar(Context context, Content content, ImageView user_photo, TextView user_id,TextView send_time,TextView comefrom){
        //        填充头像
//        Glide.with(context).load(Constant.USER_PHOTO+content.getUser_photo()).placeholder(R.color.gray).dontAnimate().dontTransform().into(user_photo);
        user_photo.setBackground(Drawable.createFromPath(Constant.USER_PHOTO+content.getUser_photo()));
        LogUtil.e("用户图像地址"+ Constant.USER_PHOTO+content.getUser_photo());
//        填充用户名
        user_id.setText(content.getId());
//        填充发布时间,现在为毫秒值
        send_time.setText(content.getTag()+"");
//        填充来自的地方
        comefrom.setText("   未实现");
    }
    //        填充正文
    public static void fillWeiBoContent(String content, Context context, EmojiTextView textView){
//        填充正文文字
        textView.setText(content);

    }
    //        填充正文的底部布局
    public static void fillButtonBar(final Context context, final Content content, LinearLayout retweetLayout, LinearLayout commentLayout , final LinearLayout attLayout, final TextView retweet, final TextView comment, final TextView feedlike){
//        转发
        if (content.getRetweet()==0){
            retweet.setText("转发");
        }else {
            retweet.setText(" "+content.getRetweet());
        }

        retweetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                content.setRetweet(content.getRetweet()+1);
                Intent intent = new Intent(context, IdeaActivity.class);
                intent.putExtra("ideaType", PostService.POST_SERVICE_REPOST_STATUS);
                intent.putExtra("status", content);
                context.startActivity(intent);
//                retweet.setText(" "+content.getRetweet());
//                Toast.makeText(context,"转发一次",Toast.LENGTH_SHORT).show();
            }
        });
//        评论
        if (content.getComment()==0){
            comment.setText("评论");
        }else {
            comment.setText(" "+content.getComment());
        }

        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setComment(content.getComment()+1);
                if (content.getIfretweet() ==0) {
                    Intent intent = new Intent(context, OriginPicTextCommentDetailActivity.class);
                    intent.putExtra("weiboitem", content);
                    ((Activity) context).startActivityForResult(intent, 101);
                } else {
                    Intent intent = new Intent(context, RetweetPicTextCommentDetailActivity.class);
                    intent.putExtra("weiboitem", content);
                    ((Activity) context).startActivityForResult(intent, 101);
                }
//                comment.setText(" "+content.getComment());
//                Toast.makeText(context,"评论一次",Toast.LENGTH_SHORT).show();
            }
        });
//        点赞
        if (content.getPharise()==0){
            feedlike.setText("点赞");
        }else {
            feedlike.setText(""+content.getPharise());
        }
        attLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content.getIfPharise()==0){
//                    之前没有点赞
                    content.setPharise(content.getPharise()+1);
                    ImageView imageView=(ImageView) attLayout.findViewById(R.id.praise);
//                    imageView.invalidateDrawable(context.getResources().getDrawable(R.drawable.timeline_icon_unlike));
                    imageView.setBackground(context.getResources().getDrawable(R.drawable.praise));
                    feedlike.setText(" "+content.getPharise());
                    content.setIfPharise(1);
                }else {
//                    取消赞
                    content.setPharise(content.getPharise()-1);
                    ImageView imageView=(ImageView) attLayout.findViewById(R.id.praise);
                    imageView.setBackground(context.getResources().getDrawable(R.drawable.timeline_icon_unlike));
                    feedlike.setText(" "+content.getPharise());
                    content.setIfPharise(0);
                }


//                Toast.makeText(context,"点赞一次",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //        填充正文的图片
    public static void fillWeiBoImgList(Content content, Context context, RecyclerView recyclerview){
        ArrayList<String> imageDatas = content.getPhoto();
        if (imageDatas == null || imageDatas.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            return;
        }
        if (recyclerview.getVisibility() == View.GONE) {
            recyclerview.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = initGridLayoutManager(imageDatas, context);
        ImageAdapter imageAdapter = new ImageAdapter(content,context);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(imageAdapter);
        recyclerview.setLayoutManager(gridLayoutManager);
        imageAdapter.setData(imageDatas);
        imageAdapter.notifyDataSetChanged();
    }

    /**
     * 根据图片数量，初始化GridLayoutManager，并且设置列数，
     * 当图片 = 1 的时候，显示1列
     * 当图片<=4张的时候，显示2列
     * 当图片>4 张的时候，显示3列
     *
     * @return
     */
    private static GridLayoutManager initGridLayoutManager(ArrayList<String> imageDatas, Context context) {
        GridLayoutManager gridLayoutManager;
        if (imageDatas != null) {
            switch (imageDatas.size()) {
                case 1:
                    gridLayoutManager = new GridLayoutManager(context, 1);
                    break;
                case 2:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                case 3:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
                case 4:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                default:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
            }
        } else {
            gridLayoutManager = new GridLayoutManager(context, 3);
        }
        return gridLayoutManager;
    }

    /**
     * 填充列表图片
     *
     * @param context
     * @param status
     * @param options
     * @param position
     * @param longImg
     * @param norImg
     * @param gifImg
     * @param imageLabel
     * @param mSingleImageSize
     * @param mDoubleImgSize
     * @param mThreeImgSize
     */
    public static void fillImageList(final Context context, final Content status, DisplayImageOptions options, final int position, final SubsamplingScaleImageView longImg, final ImageView norImg, final GifImageView gifImg, final ImageView imageLabel, ImageSize mSingleImageSize, ImageSize mDoubleImgSize, ImageSize mThreeImgSize) {
        final ArrayList<String> urllist =status.getPhoto();
        /*for (int i=0;i<urllist.size();i++){
            LogUtil.e("图片地址     "+urllist.get(i));
        }*/
        if (urllist.size() == 1) {
            ImageLoader.getInstance().loadImage(urllist.get(position), mSingleImageSize, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    setLabelForGif(urllist.get(position), imageLabel);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    File file = DiskCacheUtils.findInCache(urllist.get(position), ImageLoader.getInstance().getDiskCache());
                    if (imageUri.endsWith(".gif")) {
                        gifImg.setVisibility(View.VISIBLE);
                        longImg.setVisibility(View.INVISIBLE);
                        norImg.setVisibility(View.INVISIBLE);
                        displayGif(file, gifImg, imageLabel);
                    } else if (isLongImg(file, bitmap)) {
                        longImg.setVisibility(View.VISIBLE);
                        gifImg.setVisibility(View.INVISIBLE);
                        norImg.setVisibility(View.INVISIBLE);
                        displayLongPic(file, bitmap, longImg, imageLabel);
                    } else {
                        norImg.setVisibility(View.VISIBLE);
                        longImg.setVisibility(View.INVISIBLE);
                        gifImg.setVisibility(View.INVISIBLE);
                        displayNorImg(file, bitmap, norImg, imageLabel);
                    }
                }
            });
        }else if (urllist.size() == 2 || urllist.size() == 4){
            ImageLoader.getInstance().loadImage(urllist.get(position), mDoubleImgSize, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    setLabelForGif(urllist.get(position), imageLabel);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    File file = DiskCacheUtils.findInCache(urllist.get(position), ImageLoader.getInstance().getDiskCache());
                    if (imageUri.endsWith(".gif")) {
                        gifImg.setVisibility(View.VISIBLE);
                        longImg.setVisibility(View.INVISIBLE);
                        norImg.setVisibility(View.INVISIBLE);
                        displayGif(file, gifImg, imageLabel);
                    } else if (isLongImg(file, bitmap)) {
                        longImg.setVisibility(View.VISIBLE);
                        gifImg.setVisibility(View.INVISIBLE);
                        norImg.setVisibility(View.INVISIBLE);
                        displayLongPic(file, bitmap, longImg, imageLabel);
                    } else {
                        norImg.setVisibility(View.VISIBLE);
                        longImg.setVisibility(View.INVISIBLE);
                        gifImg.setVisibility(View.INVISIBLE);
                        displayNorImg(file, bitmap, norImg, imageLabel);
                    }
                }
            });
        }else if (urllist.size()== 3 || urllist.size() >= 5){
            ImageLoader.getInstance().loadImage(urllist.get(position), mThreeImgSize, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    setLabelForGif(urllist.get(position), imageLabel);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    File file = DiskCacheUtils.findInCache(urllist.get(position), ImageLoader.getInstance().getDiskCache());
                    if (imageUri.endsWith(".gif")) {
                        gifImg.setVisibility(View.VISIBLE);
                        longImg.setVisibility(View.INVISIBLE);
                        norImg.setVisibility(View.INVISIBLE);
                        displayGif(file, gifImg, imageLabel);
                    } else if (isLongImg(file, bitmap)) {
                        longImg.setVisibility(View.VISIBLE);
                        gifImg.setVisibility(View.INVISIBLE);
                        norImg.setVisibility(View.INVISIBLE);
                        displayLongPic(file, bitmap, longImg, imageLabel);
                    } else {
                        norImg.setVisibility(View.VISIBLE);
                        longImg.setVisibility(View.INVISIBLE);
                        gifImg.setVisibility(View.INVISIBLE);
                        displayNorImg(file, bitmap, norImg, imageLabel);
                    }
                }
            });
        }else {
            ImageLoader.getInstance().loadImage(urllist.get(position), options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    setLabelForGif(urllist.get(position), imageLabel);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    File file = DiskCacheUtils.findInCache(urllist.get(position), ImageLoader.getInstance().getDiskCache());
                    if (imageUri.endsWith(".gif")) {
                        gifImg.setVisibility(View.VISIBLE);
                        longImg.setVisibility(View.INVISIBLE);
                        norImg.setVisibility(View.INVISIBLE);
                        displayGif(file, gifImg, imageLabel);
                    } else if (isLongImg(file, bitmap)) {
                        longImg.setVisibility(View.VISIBLE);
                        gifImg.setVisibility(View.INVISIBLE);
                        norImg.setVisibility(View.INVISIBLE);
                        displayLongPic(file, bitmap, longImg, imageLabel);
                    } else {
                        norImg.setVisibility(View.VISIBLE);
                        longImg.setVisibility(View.INVISIBLE);
                        gifImg.setVisibility(View.INVISIBLE);
                        displayNorImg(file, bitmap, norImg, imageLabel);
                    }
                }
            });
        }

        longImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("imagelist_url", status.getPhoto());
                intent.putExtra("image_position", position);
                context.startActivity(intent);
            }
        });
        gifImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("imagelist_url", status.getPhoto());
                intent.putExtra("image_position", position);
                context.startActivity(intent);
            }
        });

        norImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("imagelist_url", status.getPhoto());
                intent.putExtra("image_position", position);
                context.startActivity(intent);
            }
        });
    }

    private static void displayLongPic(File file, Bitmap bitmap, SubsamplingScaleImageView longImg, ImageView imageLable) {
        imageLable.setVisibility(View.VISIBLE);
        imageLable.setImageResource(R.drawable.timeline_image_longimage);
        longImg.setZoomEnabled(false);
        longImg.setPanEnabled(false);
        longImg.setQuickScaleEnabled(false);
        longImg.setImage(ImageSource.uri(file.getAbsolutePath()), new ImageViewState(0, new PointF(0, 0), 0));
        longImg.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
    }

    public static void displayNorImg(File file, Bitmap bitmap, ImageView norImg, ImageView imageLable) {
        imageLable.setVisibility(View.GONE);
        norImg.setImageBitmap(bitmap);
        norImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public static void displayGif(File file, GifImageView gifImageView, ImageView imageLable) {
        imageLable.setVisibility(View.VISIBLE);
        imageLable.setImageResource(R.drawable.timeline_image_gif);
        try {
            GifDrawable gifDrawable = new GifDrawable(file);
            gifImageView.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            Log.e("wenming", e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isLongImg(File file, Bitmap bitmap) {
        //TODO file.length()的判断，需要根据OS的版本号做动态调整大小
        if (file == null || file.length() == 0) {
            return false;
        }
        if (bitmap.getHeight() > bitmap.getWidth() * 3 || file.length() >= 0.5 * 1024 * 1024) {
            return true;
        }
        return false;
    }

    /**
     * 为Gif图设置图标，根据url来决定是否设置
     *
     * @param url
     * @param imageLabel
     */
    private static void setLabelForGif(String url, ImageView imageLabel) {
        if (url.endsWith(".gif")) {
            imageLabel.setVisibility(View.VISIBLE);
            imageLabel.setImageResource(R.drawable.timeline_image_gif);
        } else {
            imageLabel.setVisibility(View.GONE);
        }
    }

//    ---------------------------填充评论列表
    /**
     * 设置头像的认证icon，记住要手动刷新icon，不然icon会被recycleriview重用，导致显示出错
     *
     * @param user
     * @param profile_img
     */
    public static void fillProfileImg(final Context context, final User user, final ImageView profile_img) {

        ImageLoader.getInstance().displayImage(user.profile_image_url, profile_img, mAvatorOptions);

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("screenName", user.screen_name);
                context.startActivity(intent);*/
            }
        });
    }
    public static void setWeiBoName(TextView textView, User user) {
        if (user.id != null && user.id.length() > 0) {
            textView.setText(user.id);
        } else {
            textView.setText(user.id);
        }
    }

    public static void setWeiBoTime(Context context, TextView textView,Content status) {
        Date data = DateUtils.parseDate(status.getTag()+"", DateUtils.WeiBo_ITEM_DATE_FORMAT);
        TimeUtils timeUtils = TimeUtils.instance(context);
        textView.setText(timeUtils.buildTimeString(data.getTime()) + "   ");
    }

    public static void setWeiBoTime(Context context, TextView textView, Comment comment) {
        Date data = DateUtils.parseDate(comment.created_at, DateUtils.WeiBo_ITEM_DATE_FORMAT);
        TimeUtils timeUtils = TimeUtils.instance(context);
        textView.setText(timeUtils.buildTimeString(data.getTime()) + "   ");
    }

    public static void fillDetailButtonBar(final Context context, final Content status, LinearLayout bottombar_retweet, LinearLayout bottombar_comment, LinearLayout bottombar_attitude) {
        //如果转发的内容已经被删除,则不允许转发
        if (false) {
            bottombar_retweet.setEnabled(false);
        } else {
            bottombar_retweet.setEnabled(true);
        }

        bottombar_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IdeaActivity.class);
                intent.putExtra("ideaType", PostService.POST_SERVICE_COMMENT_STATUS);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });

        bottombar_retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IdeaActivity.class);
                intent.putExtra("ideaType", PostService.POST_SERVICE_REPOST_STATUS);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });

        bottombar_attitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞动画
            }
        });
    }

    public static void refreshNoneView(Context context, int type, int repostss_count, int comments_count, View noneView) {
        TextView textView = (TextView) noneView.findViewById(R.id.tv_normal_refresh_footer_status);
        if (NetUtil.isConnected(context)) {
            switch (type) {
                case StatusDetailModelImp.COMMENT_PAGE:
                    if (comments_count > 0) {
                        noneView.setVisibility(View.GONE);
                    } else if (comments_count == 0) {
                        noneView.setVisibility(View.VISIBLE);
                        textView.setText("还没有人评论");
                    }
                    break;

                case StatusDetailModelImp.REPOST_PAGE:
                    if (repostss_count > 0) {
                        noneView.setVisibility(View.GONE);
                    } else if (repostss_count == 0) {
                        noneView.setVisibility(View.VISIBLE);
                        textView.setText("还没有人转发");
                    }
                    break;
            }

        } else {
            noneView.setVisibility(View.VISIBLE);
            textView.setText("网络出错啦");
        }

    }
    /**
     * 决定是否隐藏转发，评论，赞的底部的bar，进入weibodetail的时候隐藏他
     *
     * @param visible
     * @param layout
     */
    public static void showButtonBar(int visible, LinearLayout layout) {
        if (visible == View.VISIBLE) {
            layout.setVisibility(View.VISIBLE);
        } else if (visible == View.GONE) {
            layout.setVisibility(View.GONE);
        } else if (visible == View.INVISIBLE) {
            layout.setVisibility(View.INVISIBLE);
        }
    }


}
