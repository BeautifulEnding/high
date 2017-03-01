package com.example.asus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.he.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //    item布局
    private View mView;
    private Context mContext;
    private ArrayList<String> topics;
    public TopicAdapter(Context context,ArrayList<String> topics){
        mContext=context;
        this.topics=topics;
    }
    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView= LayoutInflater.from(mContext).inflate(R.layout.topic_layout,null);
        TopicViewHolder viewHolder=new TopicViewHolder(mView);
        return viewHolder;
    }
    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((TopicViewHolder)holder).topicName.setText(topics.get(position));
    }
    @Override
    public int getItemCount() {
        if (topics!=null){
            return topics.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    public void setData(ArrayList<String> data){
        topics=data;
    }
    public static class TopicViewHolder extends RecyclerView.ViewHolder{
        public TextView topicName;
        public View divider;
        public TopicViewHolder(View view){
            super(view);
            topicName=(TextView) view.findViewById(R.id.topic);
            divider=view.findViewById(R.id.divider);
        }
    }
}
