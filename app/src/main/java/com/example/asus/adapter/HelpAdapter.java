package com.example.asus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.asus.client.entity.MessageList;
import com.example.asus.entity.ContentList;
import com.example.asus.he.R;
import com.example.asus.util.CacheUtil;

/**
 * Created by Administrator on 2017/2/28 0028.
 */
public class HelpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//    private  List<User> users=new ArrayList<>();
    private Context mContext;
    private MessageList messageList;
    private View mView;
    private HelpAdapter adapter;
    public HelpAdapter(MessageList messageList, Context context){
        this.messageList=messageList;
        mContext=context;
        adapter=this;
    }
    public HelpAdapter(Context context){
        mContext=context;
        adapter=this;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
//            未处理时的布局
            mView= LayoutInflater.from(mContext).inflate(R.layout.friend_request_layout,parent,false);
            HelpViewHolder helpViewHolder=new HelpViewHolder(mView);
            return helpViewHolder;
        }else{
            //处理后的布局
            mView=LayoutInflater.from(mContext).inflate(R.layout.resovedlayout,parent,false);
            ResolvedViewHolder resolvedViewHolder=new ResolvedViewHolder(mView);
            return resolvedViewHolder;
        }
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HelpViewHolder){
            ((HelpViewHolder)holder).user_photo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.example_profileimg));
            ((HelpViewHolder)holder).user_id.setText(messageList.getMessage().get(position).getSender_id());
            ((HelpViewHolder)holder).addButton.setText("同意");
            ((HelpViewHolder)holder).addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                改变状态，不删除记录
                    messageList.getMessage().get(position).setMessageType(1);
                    messageList.getMessage().get(position).setMessageState(0);
//                    更改该条内容在客户端显示的状态
                    String temp=messageList.getMessage().get(position).getContent();
                    String tempTag=temp.substring(0,temp.lastIndexOf('@'));
                    String tempType=temp.substring(temp.lastIndexOf('@')+1);
                   Long tag= Long.parseLong(tempTag);
                    int type=Integer.parseInt(tempType);
//                    通過tag唯一確定一條話題内容,
                    ContentList contentList= CacheUtil.cacheLoad(type,mContext);
                    if (contentList!=null){
                        for (int i=0;i<contentList.getContents().size();i++){
                            if (contentList.getContents().get(i).getTag()==tag){
                                contentList.getContents().get(i).setTopic(contentList.getContents().get(i).getType()+1);
                                CacheUtil.cacheSave(type,mContext,contentList);
                                break;
                            }
                        }
                    }
                    contentList=CacheUtil.cacheLoad(4,mContext);
                    if (contentList!=null){
                        for (int i=0;i<contentList.getContents().size();i++){
                            if (contentList.getContents().get(i).getTag()==tag){
                                contentList.getContents().get(i).setTopic(contentList.getContents().get(i).getType()+1);
                                CacheUtil.cacheSave(4,mContext,contentList);
                                break;
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();

                }
            });
            ((HelpViewHolder)holder).addButton.setText("拒绝");
            ((HelpViewHolder)holder).addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                改变状态，不删除记录
                    messageList.getMessage().get(position).setMessageType(1);
                    messageList.getMessage().get(position).setMessageState(1);
                    adapter.notifyDataSetChanged();
                }
            });
        }else{
            if (messageList.getMessage().get(position).getMessageState()==0){
                ((ResolvedViewHolder)holder).description.setText("您已同意了"
                        +messageList.getMessage().get(position)
                        +"的帮助");
            }else {
                ((ResolvedViewHolder)holder).description.setText("您已拒绝了"
                        +messageList.getMessage().get(position)
                        +"的帮助");
            }
        }

    }

    @Override
    public int getItemCount() {
        if (messageList!=null){
            return messageList.getMessage().size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.getMessage().get(position).getMessageType();
    }
    public static class HelpViewHolder extends RecyclerView.ViewHolder{
        public ImageView user_photo;
        public TextView user_id;
        public Button addButton;
        public Button refuseButton;
        public HelpViewHolder(View view){
            super(view);
            user_photo=(ImageView)view.findViewById(R.id.user_photo);
            user_id=(TextView)view.findViewById(R.id.user_id);
            addButton=(Button)view.findViewById(R.id.add);
            refuseButton=(Button)view.findViewById(R.id.refuse);
        }
    }

    public static class ResolvedViewHolder extends RecyclerView.ViewHolder{
        public TextView description;
        public ResolvedViewHolder(View view){
            super(view);
            description=(TextView)view.findViewById(R.id.description);
        }
    }

}
