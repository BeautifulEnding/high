
package com.example.asus.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 微博结构体。
 *
 * @author SINA
 * @since 2013-11-22
 */
public class Status implements Parcelable {

    /**
     * 微博创建时间
     */
    public String created_at;
    /**
     * 正文ID，由时间决定
     */
    public String id;

    /**
     * 微博文本内容长度
     */
    public int textLength;
    /**
     * 微博信息内容
     */
    public String text;
    /**
     * 是否是超过140个字的长微博
     */
    public boolean isLongText;

    /**
     * 微博来源
     */
    public String source;

    /**
     * 缩略图片地址（小图），没有时不返回此字段
     */
    public String small_pic;
    /**
     * 中等尺寸图片地址（中图），没有时不返回此字段
     */
    public String middle_pic;
    /**
     * 原始图片地址（原图），没有时不返回此字段
     */
    public String original_pic;

    /**
     * 微博作者的用户信息字段
     */
    public User user;
    /**
     * 被转发的原微博信息字段，当该微博为转发微博时返回
     */
    public Status retweeted_status;
    /**
     * 转发数
     */
    public int reposts_count;
    /**
     * 评论数
     */
    public int comments_count;
    /**
     * 表态数，点赞数
     */
    public int attitudes_count;

    /**
     * 微博来源是否允许点击，如果允许
     */
    public int source_allowclick;
    /**
     * 微博图片字段
     */
    public ArrayList<PicUrlsBean> pic_urls;

    /**
     * 缩略图的url，本地私有的字段，服务器不会返回此字段，在gson赋值完成后，需要手动为此字段赋值
     */

    public ArrayList<String> small_pic_urls = new ArrayList<>();

    /**
     * 中等质量图片的url，本地私有的字段，服务器不会返回此字段，在gson赋值完成后，需要手动为此字段赋值
     */
    public ArrayList<String> middle_pic_urls = new ArrayList<>();

    /**
     * 原图的url，本地私有的字段，服务器不会返回此字段，在gson赋值完成后，需要手动为此字段赋值
     */
    public ArrayList<String> origin_pic_urls = new ArrayList<>();

    /**
     * 单张微博的尺寸，本地私有的字段，服务器不会返回此字段，在gson赋值完成后，需要手动为此字段赋值
     */
    public String singleImgSizeType;

    public static class PicUrlsBean implements Parcelable {

        public String small_pic;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.small_pic);
        }

        public PicUrlsBean() {
        }

        protected PicUrlsBean(Parcel in) {
            this.small_pic = in.readString();
        }

        public static final Creator<PicUrlsBean> CREATOR = new Creator<PicUrlsBean>() {
            @Override
            public PicUrlsBean createFromParcel(Parcel source) {
                return new PicUrlsBean(source);
            }

            @Override
            public PicUrlsBean[] newArray(int size) {
                return new PicUrlsBean[size];
            }
        };
    }

    public Status() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.created_at);
        dest.writeString(this.id);
        dest.writeInt(this.textLength);
        dest.writeString(this.text);
        dest.writeByte(this.isLongText ? (byte) 1 : (byte) 0);
        dest.writeString(this.source);
        dest.writeString(this.small_pic);
        dest.writeString(this.middle_pic);
        dest.writeString(this.original_pic);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.retweeted_status, flags);
        dest.writeInt(this.reposts_count);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.attitudes_count);
        dest.writeInt(this.source_allowclick);
        dest.writeTypedList(this.pic_urls);
        dest.writeStringList(this.small_pic_urls);
        dest.writeStringList(this.middle_pic_urls);
        dest.writeStringList(this.origin_pic_urls);
        dest.writeString(this.singleImgSizeType);
    }

    protected Status(Parcel in) {
        this.created_at = in.readString();
        this.id = in.readString();
        this.textLength = in.readInt();
        this.text = in.readString();
        this.isLongText = in.readByte() != 0;
        this.source = in.readString();
        this.small_pic = in.readString();
        this.middle_pic = in.readString();
        this.original_pic = in.readString();
        this.retweeted_status = in.readParcelable(Status.class.getClassLoader());
        this.reposts_count = in.readInt();
        this.comments_count = in.readInt();
        this.attitudes_count = in.readInt();
        this.source_allowclick = in.readInt();
        this.pic_urls = in.createTypedArrayList(PicUrlsBean.CREATOR);
        this.small_pic_urls = in.createStringArrayList();
        this.middle_pic_urls = in.createStringArrayList();
        this.origin_pic_urls = in.createStringArrayList();
        this.singleImgSizeType = in.readString();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel source) {
            return new Status(source);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
}
