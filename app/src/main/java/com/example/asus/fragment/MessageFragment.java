package com.example.asus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.activity.ChatActivity;
import com.example.asus.activity.MainActivity;
import com.example.asus.adapter.MessageAdapter;
import com.example.asus.client.entity.MessageList;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.entity.Message;
import com.example.asus.he.R;
import com.example.asus.util.CacUtil;
import com.example.asus.util.CacheUtil;
import com.example.asus.util.LogUtil;
import com.example.asus.util.SDCardUtil;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends BaseFragment {
    private ListView messageListview;
    private View rootView;
    private TextView textView;
    private MessageAdapter adapter;
    private List<MessageList> messageList=new ArrayList<>();
    private MessageList message;
    private String[] allMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//        其中的container为fragment所依附的容器，当fragment在activity中出现时，container就不会为空，在这container
//        为mainLayout中的frameLayout
//        container不为空时，false，表示以container的width和height来为资源布局
//若为true，则是将布局作为view添加到container中
        rootView=inflater.inflate(R.layout.message_fragment_layout,container,false);
        Constant.CHAT_PATH=Constant.SD_PATH+"chat";
        initView();
        initEvent();
       return rootView;
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.currFragTag =Constant.FRAGMENT_FLAG_MESSAGE;
    }
    private void initView(){
        messageListview=(ListView) rootView.findViewById(R.id.message_list);
        textView=(TextView)rootView.findViewById(R.id.nomessage);
        allMessage= SDCardUtil.getAllFile(Constant.CHAT_PATH);
        if (allMessage!=null && allMessage.length!=0){
            messageList.clear();
            for (int i=0;i<allMessage.length;i++){
                String file=allMessage[i].substring(0,allMessage[i].lastIndexOf('.'));
                message=MessageList.parse(CacUtil.cacheLoad(Constant.LOAD_MESSAGELIST,getActivity(),file));
//                messageList= MessageList.parse(CacUtil.cacheLoad(Constant.LOAD_MESSAGELIST,this,friends.getId()));
                messageList.add(message);
            }
            if (messageListview.getVisibility()==View.GONE){
                messageListview.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }
            if (messageList!=null){
                adapter=new MessageAdapter(messageList,getActivity());
            }

        }else {
            adapter=new MessageAdapter();
        }
        messageListview.setAdapter(adapter);
    }
    private void initEvent(){
        messageListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.e("messageListview被单击");
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("messageList",messageList.get(position));
                User user=CacUtil.cacheLoad(messageList.get(position).getTitle(),getActivity());
                intent.putExtra("friends",user);
                getActivity().startActivity(intent);
            }
        });
    }
}
