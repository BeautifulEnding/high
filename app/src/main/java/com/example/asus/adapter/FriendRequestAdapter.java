package com.example.asus.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageType;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.fragment.NewsFragment;
import com.example.asus.he.R;
import com.example.asus.util.CacUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class FriendRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private  List<User> users=new ArrayList<>();
    private Context mContext;
    private View mView;
    private FriendRequestAdapter adapter;
    private SimpleAdapter friendAdapter;
    private NewsFragment fragment;
    private List<Map<String,Object>> listItems=new ArrayList<>();
    public FriendRequestAdapter(Context context,SimpleAdapter friendAdapter,List<Map<String,Object>> listItems){
        mContext=context;
        adapter=this;
        this.friendAdapter=friendAdapter;
        this.listItems=listItems;
    }
    public FriendRequestAdapter(List<User> users,Context context,SimpleAdapter friendAdapter,List<Map<String,Object>> listItems){
        this.users=users;
        mContext=context;
        adapter=this;
        this.friendAdapter=friendAdapter;
        this.listItems=listItems;
    }
    public FriendRequestAdapter(Context context,NewsFragment fragment){
        mContext=context;
        this.fragment=fragment;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView= LayoutInflater.from(mContext).inflate(R.layout.friend_request_layout,parent,false);
        FriendViewHolder viewHolder=new FriendViewHolder(mView);
        return viewHolder;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Glide.with(mContext).load(Constant.USER_PHOTO+users.get(position).getId()).placeholder(R.drawable.example_profileimg).dontAnimate().dontTransform().into(((FriendViewHolder)holder).user_photo);
        ((FriendViewHolder)holder).user_id.setText(users.get(position).getId());
        ((FriendViewHolder)holder).addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.updateListView(users.get(position));
                CacUtil.cacheDelete(users.get(position).getId());
                adapter.remove(users.get(position));
                adapter.notifyDataSetChanged();
//                发送消息到服务器通知对方已同意加为好友
                Message message=new Message();
                message.setContent("agreeFriend");
                User selfUser= CacUtil.cacheLoad("selfMessage",mContext);
                message.setSender_id(selfUser.getId());
                message.setReceiver_id(users.get(position).getId());
                message.setType(MessageType.AGREE_FRIEND);
                message.setSendTime(System.currentTimeMillis());
                Intent intent=new Intent();
                intent.setAction("add.friend.message");
                intent.putExtra("message",message);
                mContext.sendBroadcast(intent);
            }
        });

        ((FriendViewHolder)holder).refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacUtil.cacheDelete(users.get(position).getId());
                adapter.remove(users.get(position));
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (users!=null){
            return users.size();
        }
        return 0;
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
    public User getUser(int position){
        return users.get(position);
    }
    public static class FriendViewHolder extends RecyclerView.ViewHolder{
        public ImageView user_photo;
        public TextView user_id;
        public Button addButton;
        public Button refuseButton;
        public FriendViewHolder(View view){
            super(view);
            user_photo=(ImageView)view.findViewById(R.id.user_photo);
            user_id=(TextView)view.findViewById(R.id.user_id);
            addButton=(Button)view.findViewById(R.id.add);
            refuseButton=(Button)view.findViewById(R.id.refuse);
        }
    }

}
