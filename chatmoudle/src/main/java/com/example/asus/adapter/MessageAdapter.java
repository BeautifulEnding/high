package com.example.asus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.example.asus.R;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageList;
import com.example.asus.client.entity.MessageType;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.util.DateUtils;
import com.example.asus.util.SDCardUtil;
import com.example.asus.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class MessageAdapter extends BaseAdapter{
    private List<MessageList> messageList=new ArrayList<>();
    private Context mContext;
    private View mView;
    public MessageAdapter(List<MessageList> messageList, Context context){
        this.messageList=messageList;
        mContext=context;
    }
    public MessageAdapter(){

    }
    @Override
    public int getCount() {
        if (messageList!=null){
            return messageList.size();
        }
        return 0;
    }
    @Override
    public MessageList getItem(int position) {
        if (messageList!=null){
            return messageList.get(position);
        }
        return null;
    }
    public void setMessageList(List<MessageList> messageList){
        this.messageList=messageList;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageHolder holder;
        if (convertView==null){
            holder=new MessageHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.message_item_layout,null);
            holder.newIcon=(ImageView)convertView.findViewById(R.id.new_icon);
            holder.newDate=(TextView)convertView.findViewById(R.id.new_date);
            holder.newTitle=(TextView)convertView.findViewById(R.id.new_title);
            holder.newContent=(TextView)convertView.findViewById(R.id.new_content);
            convertView.setTag(holder);
        }else {
            holder=(MessageHolder) convertView.getTag();
        }
        User user= UserUtil.getUser(messageList.get(position).getTitle(),mContext);
        Glide.with(mContext).load(Constant.USER_PHOTO+user.getProfile_image_url()).
                placeholder(R.drawable.example_profileimg).dontAnimate().dontTransform().
                into(holder.newIcon);
        holder.newDate.setText(DateUtils.translateDate(messageList.get(position).getLastTime(),System.currentTimeMillis()));
        holder.newTitle.setText(messageList.get(position).getTitle());
        if (messageList.get(position).getType()== MessageType.VOICE_MESSAGE){
            holder.newContent.setText("点击查看详情");
        }else{
            if (messageList.get(position).getType()==MessageType.PICTURE_MESSAGE){
                holder.newContent.setText("点击查看大图");
            }else {
                holder.newContent.setText(messageList.get(position).getContent());
            }
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    public void setMessage(MessageList message){
        messageList.add(message);
    }
    public void remove(MessageList message){
        messageList.remove(message);
    }
    public static class MessageHolder{
        public ImageView newIcon;
        public TextView  newDate;
        private TextView newTitle;
        private TextView newContent;
    }

}
