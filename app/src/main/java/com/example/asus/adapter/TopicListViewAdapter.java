package com.example.asus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.he.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class TopicListViewAdapter extends BaseAdapter{
    //    item布局
    private View mView;
    private Context mContext;
    private ArrayList<String> topics;
    private String[] arrayTopics;
    public TopicListViewAdapter(Context context,ArrayList<String> topics){
        mContext=context;
        this.topics=topics;
    }
    public TopicListViewAdapter(Context context,String[] topics){
        mContext=context;
        this.arrayTopics=topics;
    }
    @Override
    public int getCount() {
        if (topics!=null){
            return topics.size();
        }else {
            if (arrayTopics!=null){
                return arrayTopics.length;
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        // Get the row id associated with the specified position in the list.(取在列表中与指定索引对应的行id)
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopicViewHolder holder;
        if (convertView==null){
            holder=new TopicViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.topic_layout,null);
            holder.topicName=(TextView)convertView.findViewById(R.id.topic);
            holder.divider=convertView.findViewById(R.id.divider);
            convertView.setTag(holder);
        }else {
            holder=(TopicViewHolder) convertView.getTag();
        }
        holder.topicName.setText(topics.get(position));
        return convertView;
    }


    public static class TopicViewHolder{
        public TextView topicName;
        public View divider;
    }
}
