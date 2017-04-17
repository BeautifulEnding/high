package com.example.asus.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.R;
import com.example.asus.activity.ImageDetailsActivity;
import com.example.asus.activity.chatmanage.BitmapCommon;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageList;
import com.example.asus.client.entity.MessageType;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.util.CacUtil;
import com.example.asus.util.CommonUtils;
import com.example.asus.util.DateUtils;
import com.example.asus.util.FaceTextUtils;
import com.example.asus.util.LogUtil;
import com.example.asus.util.SDCardUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class MessageListAdapter extends BaseAdapter {
    private List<Message> items;
    private Context context;
    private ListView adapterList;
    private LayoutInflater inflater;
    private static final String PHOTO_PATH = Constant.SD_PATH+"image/";
    private static final String VOICE_PATH = Constant.SD_PATH+"Record/";
    private Bitmap bp;
    private User user;
    private MessageList messageList;
    MediaPlayer mediaPlayer;
    public MessageListAdapter(Context context, List<Message> items,
                              ListView adapterList,User user) {
        this.context = context;
        this.items = items;
        this.adapterList = adapterList;
        this.user=user;
    }
    public MessageListAdapter(Context context, MessageList messageList,
                              ListView adapterList,User user) {
        this.context = context;
        this.adapterList = adapterList;
        this.user=user;
        this.items=messageList.getMessage();
    }
    public void refreshList(List<Message> items) {
        this.items = items;
        this.notifyDataSetChanged();
        adapterList.setSelection(items.size() - 1);
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Message message = items.get(position);
            String[] strarray;
            String voiceFile;
            String voiceTime;
            String path;
            final Intent intent= new Intent(context, ImageDetailsActivity.class);
            boolean result = false;
            List<String> list = CommonUtils.getImagePathFromSD();
            int msgViewTime;
            if (message.getMessageType() == 1) {
                if (message.getType()==MessageType.PICTURE_MESSAGE) {
                    convertView = this.inflater.inflate(
//                            接收到图片布局
                            R.layout.chat_rce_picture, null);
                } else {
                    convertView = this.inflater.inflate(
//                            接收到普通消息布局
                            R.layout.formclient_chat_in, null);
                }
            } else {
                if (message.getType()==MessageType.PICTURE_MESSAGE) {
                    convertView = this.inflater.inflate(
//                            发送图片消息布局
                            R.layout.chat_send_picture, null);
                } else {
                    convertView = this.inflater.inflate(
//                            发送普通消息布局
                            R.layout.formclient_chat_out, null);
                }
            }
            ImageView imageMsg = (ImageView) convertView
                    .findViewById(R.id.send_picture);
            TextView useridView = (TextView) convertView
                    .findViewById(R.id.formclient_row_userid);
            TextView dateView = (TextView) convertView
                    .findViewById(R.id.formclient_row_date);
            TextView msgView = (TextView) convertView
                    .findViewById(R.id.formclient_row_msg);
            TextView voiceTimeView = (TextView) convertView
                    .findViewById(R.id.voice_time);
            dateView.setText(DateUtils.translateDate(message.getSendTime(),System.currentTimeMillis()));
            if (message.getMessageType()==1) {
                if (message.getType()==MessageType.PICTURE_MESSAGE) {
                    final String dir = message.getContent();
                    Glide.with(context).load(Constant.USER_PHOTO+message.getContent()).placeholder(R.color.gray).dontAnimate().dontTransform().into(imageMsg);
                    imageMsg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent.putExtra("dir",dir);
                            context.startActivity(intent);
                        }
                    }
                    );
                } else if (message.getType()==MessageType.VOICE_MESSAGE) {
                    voiceFile = message.getContent().substring(0,message.getContent().lastIndexOf("&"));
                    voiceTime = message.getContent().substring(message.getContent().lastIndexOf("&")+1);
                    try{
                        msgViewTime = Integer.parseInt(voiceTime);
                    }catch (Exception e){
                        msgViewTime=5;
                    }
                    String msgViewLength = "";
                    voiceTimeView.setVisibility(View.VISIBLE);
                    voiceTimeView.setText(voiceTime + "\"");
                    for (int i = 0; i < msgViewTime; i++) {
                        msgViewLength += "  ";
                    }
                    final String voiceFileDir=VOICE_PATH+message.getSendTime()+".amr";
                    SDCardUtil.put(context,VOICE_PATH,message.getSendTime()+".amr",message.getContent());
                    msgView.setText(msgViewLength);
                    msgView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.chatto_voice_playing, 0);
                    msgView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (voiceFileDir.contains(".amr")) {
//                                播放录音的路径应该是什么路径？
                                CommonUtils.playMusic(voiceFileDir);
                                mediaPlayer=MediaPlayer.create(context, Uri.parse(message.getContent()));
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mediaPlayer.start();
                                    }
                                });
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mediaPlayer.stop();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    SpannableString spannableString = FaceTextUtils
                            .toSpannableString(context, message.getContent());
                    msgView.setText(spannableString);
                }
                if (null == user) {
                    useridView.setText("朋友");
                } else {
                    useridView.setText(user.getId());
                }

            } else {
                if (message.getType()==MessageType.PICTURE_MESSAGE) {
                    final String imgFilePath = message.getLocalPhoto();
                    LogUtil.e("何群芳imgFilePath:"+imgFilePath);
                    try {
                        // 实例化Bitmap
                        bp = BitmapCommon.setBitmapSize(imgFilePath);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                    imageMsg.setImageBitmap(bp);
                    imageMsg.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            intent.putExtra("dir",imgFilePath);
                            context.startActivity(intent);
                        }
                    });
                }

                else if (message.getType()==MessageType.VOICE_MESSAGE) {
//                    strarray = message.getContent().split("&");
//                    voiceFile = message.getContent().substring(0,message.getContent().lastIndexOf("&"));
                    voiceTime = message.getContent().substring(message.getContent().lastIndexOf("&")+1);
                    try{
                        msgViewTime = Integer.parseInt(voiceTime);
                    }catch (Exception e){
                        msgViewTime=5;
                    }
                    String msgViewLength = "";
                    voiceTimeView.setVisibility(View.VISIBLE);
                    voiceTimeView.setText(voiceTime + "\"");
                    for (int i = 0; i < msgViewTime; i++) {
                        msgViewLength += "  ";
                    }
                    final String voiceFileDir=message.getLocalVoice();
                    msgView.setText(msgViewLength);
                    msgView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.chatto_voice_playing, 0);
                    msgView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            LogUtil.e("何群芳voiceFileDir:"+voiceFileDir);
                            if (voiceFileDir.contains(".amr")) {
                                CommonUtils.playMusic(voiceFileDir);
                            }
                        }
                    });
                } else {
                    SpannableString spannableString = FaceTextUtils
                            .toSpannableString(context, message.getContent());
                    msgView.setText(spannableString);
                }
                useridView.setText("我");
            }
        return convertView;
    }

}