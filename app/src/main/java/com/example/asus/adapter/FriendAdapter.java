package com.example.asus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.fragment.NewsFragment;
import com.example.asus.he.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class FriendAdapter extends BaseAdapter{
    private  List<User> users=new ArrayList<>();
    private Context mContext;
    private View mView;
    private NewsFragment fragment;
    public FriendAdapter(List<User> user, Context context){
        this.users=user;
        mContext=context;
    }
    @Override
    public int getCount() {
        if (users!=null){
            return users.size();
        }
        return 0;
    }
    @Override
    public User getItem(int position) {
        if (users!=null){
            return users.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendViewHolder holder;
        if (convertView==null){
            holder=new FriendViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.friend_list_item,null);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.header=(ImageView) convertView.findViewById(R.id.header);
            convertView.setTag(holder);
        }else {
            holder=(FriendViewHolder) convertView.getTag();
        }
        holder.name.setText(users.get(position).getScreen_name());
        Glide.with(mContext).load(Constant.USER_PHOTO+users.get(position).getProfile_image_url()).
                placeholder(R.drawable.example_profileimg).dontAnimate().dontTransform().
                into(holder.header);
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    public void setUser(User user){
        users.add(user);
    }
    public void remove(User user){
        users.remove(user);
    }
    public static class FriendViewHolder{
        public ImageView header;
        public TextView name;
    }

}
